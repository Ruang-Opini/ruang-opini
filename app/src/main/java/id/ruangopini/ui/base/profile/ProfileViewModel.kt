package id.ruangopini.ui.base.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.User
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.firestore.user.FirestoreUserRepository
import id.ruangopini.data.repo.remote.firebase.storage.StorageUserRepository
import id.ruangopini.utils.STORAGE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: FirestoreUserRepository,
    private val storageUserRepository: StorageUserRepository
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

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
                    it.data.let { data ->
                        _user.value = data
                        loadAva(data)
                    }
                }
                is State.Failed -> {
                    Log.d("TAG", "getUserData: failed = ${it.message}")
                }
            }
        }
    }

    private fun loadAva(user: User) = viewModelScope.launch {
        storageUserRepository.getImageUrl(
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