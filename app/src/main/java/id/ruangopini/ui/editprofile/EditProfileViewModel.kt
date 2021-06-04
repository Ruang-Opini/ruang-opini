package id.ruangopini.ui.editprofile

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ruangopini.data.model.User
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.firestore.user.FirestoreUserRepository
import id.ruangopini.data.repo.remote.firebase.storage.StorageUserRepository
import id.ruangopini.utils.DialogHelpers
import id.ruangopini.utils.PATH
import id.ruangopini.utils.STORAGE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val storageUserRepository: StorageUserRepository,
    private val userRepo: FirestoreUserRepository
) : ViewModel() {

    private val _photoUrl = MutableLiveData<Uri>()
    val photoUrl: LiveData<Uri> get() = _photoUrl

    private val _headerUrl = MutableLiveData<Uri>()
    val headerUrl: LiveData<Uri> get() = _headerUrl

    fun loadAva(user: User) = viewModelScope.launch {
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

    fun loadBanner(user: User) = viewModelScope.launch {
        user.headerUrl.let { photo ->
            storageUserRepository.getImageUrl(
                STORAGE.ROOT_BANNER.plus(user.userId).plus("/").plus(photo)
            ).collect {
                when (it) {
                    is State.Loading -> {
                    }
                    is State.Success -> {
                        it.data.let { data -> _headerUrl.value = data }
                    }
                    is State.Failed -> {
                        Log.d("TAG", "loadBanner: failed = ${it.message}")
                    }
                }
            }
        }
    }

    fun updateUser(user: User, isAvaUpdate: Boolean, isBannerUpdate: Boolean, activity: Activity) =
        viewModelScope.launch {
            DialogHelpers.showLoadingDialog(activity, "Memperbaharui profil ...")
            val uid = user.userId ?: ""
            val pathAva = PATH.AVA.plus(uid)
            val pathBanner = PATH.BANNER.plus(uid)

            if (isAvaUpdate || isBannerUpdate) {
                updatePhotoAva(user, uid, pathAva) {
                    user.photoUrl = pathAva
                    if (isBannerUpdate) updatePhotoBanner(user, uid, pathBanner) {
                        user.headerUrl = pathBanner
                        updateData(user, activity)
                    } else updateData(user, activity)
                }
            } else updateData(user, activity)

        }

    private fun updateData(user: User, activity: Activity) = viewModelScope.launch {
        userRepo.updateUser(user).collect {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    DialogHelpers.hideLoadingDialog()
                    activity.finish()
                }
                is State.Failed -> {
                    DialogHelpers.hideLoadingDialog()
                    Log.d("TAG", "updateUser: failed = ${it.message}")
                }
            }
        }
    }

    private fun updatePhotoBanner(
        user: User, uid: String, pathBanner: String,
        isSuccess: (Boolean) -> Unit
    ) = viewModelScope.launch {
        storageUserRepository.uploadPhoto(
            user.headerUrl?.toUri() ?: "".toUri(),
            buildString {
                append(STORAGE.ROOT_BANNER).append(uid).append("/").append(pathBanner)
            }).collect {
            if (it !is State.Loading) isSuccess(it is State.Success)
            if (it is State.Failed) Log.d("TAG", "updatePhotoBanner: failed = ${it.message}")
        }
    }

    private fun updatePhotoAva(
        user: User, uid: String, pathAva: String,
        isSuccess: (Boolean) -> Unit
    ) = viewModelScope.launch {
        storageUserRepository.uploadPhoto(
            user.photoUrl?.toUri() ?: "".toUri(),
            buildString {
                append(STORAGE.ROOT_AVA).append(uid).append("/").append(pathAva)
            }).collect { if (it !is State.Loading) isSuccess(it is State.Success) }
    }
}