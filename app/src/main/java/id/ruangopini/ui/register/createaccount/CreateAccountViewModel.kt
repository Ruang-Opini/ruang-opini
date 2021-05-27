package id.ruangopini.ui.register.createaccount

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.User
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.auth.AuthRepository
import id.ruangopini.data.repo.remote.firebase.firestore.user.FirestoreUserRepository
import id.ruangopini.utils.DialogHelpers
import id.ruangopini.utils.Helpers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreateAccountViewModel(
    private val authRepository: AuthRepository,
    private val userRepo: FirestoreUserRepository
) : ViewModel() {

    private var _isComplete = MutableLiveData<Boolean>()
    private val currentProgress = mutableListOf(false, false, false, false, false)
    val isComplete: LiveData<Boolean> get() = _isComplete

    fun setProgress(index: Int, state: Boolean) = viewModelScope.launch {
        currentProgress[index] = state
        _isComplete.value = currentProgress.count { it } == currentProgress.size
    }

    fun createAccount(
        user: User, password: String, activity: Activity,
        isSuccess: () -> Unit
    ) = viewModelScope.launch {
        user.email?.let { mail ->
            authRepository.signUpWithEmail(mail, password).collect {
                when (it) {
                    is State.Loading -> DialogHelpers.showLoadingDialog(activity, "Membuat Akun")
                    is State.Success -> it.data.let { map ->
                        Log.d(
                            "TAG",
                            "isSuccess ${map.keys}: ${map.values}, id = ${Firebase.auth.currentUser?.uid}"
                        )
                        recordDataUser(user, activity) { isSuccess() }
                    }
                    is State.Failed -> {
                        DialogHelpers.hideLoadingDialog()
                        Helpers.showToast(activity, it.message)
                    }
                }
            }
        }

    }

    private fun recordDataUser(user: User, activity: Activity, isSuccess: () -> Unit) =
        viewModelScope.launch {
            userRepo.createNewUser(user).collect {
                when (it) {
                    is State.Loading -> Log.d("TAG", "loading: true")
                    is State.Success -> {
                        DialogHelpers.hideLoadingDialog()
                        isSuccess()
                    }
                    is State.Failed -> {
                        DialogHelpers.hideLoadingDialog()
                        Helpers.showToast(activity, it.message)
                    }
                }
            }
        }

}