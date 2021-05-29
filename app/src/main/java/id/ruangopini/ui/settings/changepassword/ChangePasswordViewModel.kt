package id.ruangopini.ui.settings.changepassword

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.auth.AuthRepository
import id.ruangopini.utils.DialogHelpers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isUpdateSuccess = MutableLiveData<Boolean>()
    val isUpdateSuccess: LiveData<Boolean> get() = _isUpdateSuccess

    private var _isComplete = MutableLiveData<Boolean>()
    private val currentProgress = mutableListOf(false, false)
    val isComplete: LiveData<Boolean> get() = _isComplete

    fun setProgress(index: Int, state: Boolean) = viewModelScope.launch {
        currentProgress[index] = state
        _isComplete.value = currentProgress.count { it } == currentProgress.size
    }

    fun updatePassword(
        activity: Activity,
        oldPassword: String, newPassword: String
    ) = viewModelScope.launch {
        val email = Firebase.auth.currentUser?.email ?: ""
        authRepository.updatePassword(email, oldPassword, newPassword).collect {
            when (it) {
                is State.Loading -> DialogHelpers.showLoadingDialog(activity, "Mengubah Password")
                is State.Success -> {
                    DialogHelpers.hideLoadingDialog()
                    it.data.let { data -> _isUpdateSuccess.value = data }
                }
                is State.Failed -> {
                    DialogHelpers.hideLoadingDialog()
                    Log.d("TAG", "updatePassword: failed = ${it.message}")
                    _isUpdateSuccess.value = false
                }
            }
        }
    }
}