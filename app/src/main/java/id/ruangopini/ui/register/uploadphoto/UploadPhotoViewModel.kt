package id.ruangopini.ui.register.uploadphoto

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.firestore.user.FirestoreUserRepository
import id.ruangopini.data.repo.remote.firebase.storage.StorageUserRepository
import id.ruangopini.utils.DialogHelpers
import id.ruangopini.utils.Helpers
import id.ruangopini.utils.PATH
import id.ruangopini.utils.STORAGE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UploadPhotoViewModel : ViewModel() {

    // TODO: 5/24/2021 using koin dependency injection
    private val storageUser = StorageUserRepository()
    private val userRepo = FirestoreUserRepository()

    fun uploadPhoto(uri: Uri, activity: Activity, isSuccess: () -> Unit) = viewModelScope.launch {
        // TODO: 5/24/2021 handle if login with google
        val uid = Firebase.auth.currentUser?.uid ?: ""
        val path = PATH.AVA.plus(uid)
        storageUser.uploadPhoto(uri, buildString {
            append(STORAGE.ROOT_AVA).append(uid).append("/").append(path)
        }).collect {
            when (it) {
                is State.Loading -> DialogHelpers.showLoadingDialog(activity, "Mengunggah Foto")
                is State.Success -> updatePhoto(path, activity) { isSuccess() }
                is State.Failed -> {
                    DialogHelpers.hideLoadingDialog()
                    Helpers.showToast(activity, it.message)
                }
            }
        }
    }

    private fun updatePhoto(path: String, activity: Activity, isSuccess: () -> Unit) =
        viewModelScope.launch {
            userRepo.updatePhoto(path).collect {
                when (it) {
                    is State.Loading -> Log.d("TAG", "updatePhoto: loading")
                    is State.Success -> isSuccess()
                    is State.Failed -> {
                        DialogHelpers.hideLoadingDialog()
                        Helpers.showToast(activity, it.message)
                    }
                }
            }
        }

}