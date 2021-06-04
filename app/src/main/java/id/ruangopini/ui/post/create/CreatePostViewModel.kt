package id.ruangopini.ui.post.create

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.Post
import id.ruangopini.data.model.User
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.firestore.post.FirestorePostRepository
import id.ruangopini.data.repo.remote.firebase.firestore.user.FirestoreUserRepository
import id.ruangopini.data.repo.remote.firebase.storage.StorageUserRepository
import id.ruangopini.utils.COLLECTION
import id.ruangopini.utils.DialogHelpers
import id.ruangopini.utils.PATH
import id.ruangopini.utils.STORAGE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreatePostViewModel(
    private val postRepository: FirestorePostRepository,
    private val storageUserRepository: StorageUserRepository,
    private val userRepository: FirestoreUserRepository
) : ViewModel() {

    private val currentImage = mutableListOf<String>()
    private val _image = MutableLiveData<List<String>>()
    val image: LiveData<List<String>> get() = _image

    fun addImage(img: Uri) = viewModelScope.launch {
        currentImage.add(img.toString())
        _image.value = currentImage
    }

    fun getCurrentImage(): List<String> = currentImage

    fun removeImage(position: Int) = viewModelScope.launch {
        currentImage.removeAt(position)
        _image.value = currentImage
    }

    fun createNewPost(post: Post, activity: Activity) = viewModelScope.launch {
        post.userId = Firebase.auth.currentUser?.uid ?: ""
        val postId = Firebase.firestore.collection(COLLECTION.POST).document().id

        val listPhotoId = mutableListOf<String>()
        (0 until (post.photos?.size ?: 0)).forEach {
            listPhotoId.add(PATH.POST.plus(postId).plus(it))
        }

        post.photos.let { photos ->
            if (photos?.isNotEmpty() == true) photos.forEachIndexed { index, it ->
                storageUserRepository.uploadPhoto(it.toUri(), buildString {
                    append(STORAGE.ROOT_POST).append(postId).append("/").append(listPhotoId[index])
                }).collect {
                    when (it) {
                        is State.Loading -> DialogHelpers.showLoadingDialog(
                            activity, "Membuat postingan"
                        )
                        is State.Success -> if (index == (listPhotoId.size - 1)) {
                            post.photos = listPhotoId
                            createPost(post, postId, activity)
                        }
                        is State.Failed -> {
                            DialogHelpers.hideLoadingDialog()
                            Log.d("TAG", "uploadPhoto: failed-$index= ${it.message}")
                        }
                    }
                }
            } else {
                post.photos = null
                createPost(post, postId, activity, true)
            }
        }

    }

    private fun createPost(
        post: Post, postId: String, activity: Activity, isNonPhoto: Boolean? = false
    ) = viewModelScope.launch {
        postRepository.createNewPost(post, postId)
            .collect {
                when (it) {
                    is State.Loading -> if (isNonPhoto == true) {
                        DialogHelpers.showLoadingDialog(activity, "Membuat postingan")
                    }
                    is State.Success -> {
                        Firebase.firestore.collection(COLLECTION.DISCUSSION)
                            .document(post.discussionId ?: "")
                            .update("post", FieldValue.increment(1))
                            .addOnSuccessListener {
                                DialogHelpers.hideLoadingDialog()
                                activity.finish()
                            }
                    }
                    is State.Failed -> {
                        DialogHelpers.hideLoadingDialog()
                        Log.d("TAG", "createPost: failed = ${it.message}")
                    }
                }
            }
    }

    private val _photoUrl = MutableLiveData<Uri>()
    val photoUrl: LiveData<Uri> get() = _photoUrl


    @ExperimentalCoroutinesApi
    fun getUserData() = viewModelScope.launch {
        val id = Firebase.auth.currentUser?.uid ?: ""
        userRepository.getUserById(id).collect {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    loadAva(it.data)
                }
                is State.Failed -> {
                    Log.d("TAG", "getUserData: failed = ${it.message}")
                }
            }
        }
    }

    private fun loadAva(user: User) = viewModelScope.launch {
        user.photoUrl.let { photo ->
            if (photo?.subSequence(0, 3) != "AVA") _photoUrl.value = photo?.toUri()
            else storageUserRepository.getImageUrl(
                STORAGE.ROOT_AVA.plus(user.userId).plus("/").plus(user.photoUrl)
            ).collect {
                when (it) {
                    is State.Loading -> {
                    }
                    is State.Success -> {
                        it.data.let { data -> _photoUrl.value = data }
                    }
                    is State.Failed -> {
                        Log.d("TAG", "loadAva: failed = ${it.message}")
                    }
                }
            }
        }
    }
}