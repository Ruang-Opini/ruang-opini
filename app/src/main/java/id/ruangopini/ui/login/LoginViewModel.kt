package id.ruangopini.ui.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.MainActivity
import id.ruangopini.data.model.User
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.auth.AuthRepository
import id.ruangopini.data.repo.remote.firebase.firestore.user.FirestoreUserRepository
import id.ruangopini.ui.register.createaccount.CreateAccountActivity
import id.ruangopini.utils.DialogHelpers
import id.ruangopini.utils.LoginState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@FlowPreview
class LoginViewModel(
    private val userRepository: FirestoreUserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _isComplete = MutableLiveData<Boolean>()
    private val currentProgress = mutableListOf(false, false)
    val isComplete: LiveData<Boolean> get() = _isComplete

    fun setProgress(index: Int, state: Boolean) = viewModelScope.launch {
        currentProgress[index] = state
        _isComplete.value = currentProgress.count { it } == currentProgress.size
    }

    private val _isLoginSuccess = MutableLiveData<LoginState>()
    val isLoginSuccess: LiveData<LoginState> get() = _isLoginSuccess

    fun login(username: String, password: String, activity: Activity) = viewModelScope.launch {
        userRepository.getUserByUsername(username).collect {
            when (it) {
                is State.Loading -> DialogHelpers.showLoadingDialog(activity, "Mencoba masuk...")
                is State.Success -> it.data.email?.let { email ->
                    loginWithEmailPassword(email, password)
                }
                is State.Failed -> {
                    DialogHelpers.hideLoadingDialog()
                    Log.d("TAG", "login: failed = ${it.message}")
                    _isLoginSuccess.value = LoginState.WRONG_USERNAME
                }
            }
        }
    }

    private fun loginWithEmailPassword(email: String, password: String) = viewModelScope.launch {
        authRepository.loginWithEmail(email, password).collect {
            when (it) {
                is State.Loading -> { /*do nothing*/
                }
                is State.Success -> {
                    DialogHelpers.hideLoadingDialog()
                    _isLoginSuccess.value = LoginState.SUCCESS
                }
                is State.Failed -> {
                    DialogHelpers.hideLoadingDialog()
                    Log.d("TAG", "login: failed = ${it.message}")
                    _isLoginSuccess.value = LoginState.WRONG_PASSWORD
                }
            }
        }
    }


    fun loginWithGoogle(account: GoogleSignInAccount, activity: Activity) = viewModelScope.launch {
        account.idToken?.let { token ->
            authRepository.loginWithGoogle(token, activity).collect {
                when (it) {
                    is State.Loading -> DialogHelpers.showLoadingDialog(activity)
                    is State.Success -> checkAccountWasExist(activity, account)
                    is State.Failed -> {
                        DialogHelpers.hideLoadingDialog()
                        Log.d("TAG", "googleLoginWithGoogle: failed = ${it.message}")
                    }
                }
            }
        }

    }

    private fun checkAccountWasExist(activity: Activity, account: GoogleSignInAccount) =
        viewModelScope.launch {
            val id = Firebase.auth.currentUser?.uid ?: ""
            userRepository.getUserById(id).collect {
                when (it) {
                    is State.Loading -> {
                        Log.d("TAG", "checkAccountWasExist: loading")
                    }
                    is State.Success -> {
                        DialogHelpers.hideLoadingDialog()
                        it.data.let { user ->
                            if (user.name != null) with(activity) {
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else with(activity) {
                                startActivity(Intent(
                                    this, CreateAccountActivity::class.java
                                ).apply {
                                    putExtra(
                                        CreateAccountActivity.EXTRA_ACCOUNT,
                                        User(name = account.displayName, email = account.email)
                                    )
                                })
                                finish()
                            }
                        }
                    }
                    is State.Failed -> {
                        DialogHelpers.hideLoadingDialog()
                        Log.d("TAG", "checkAccountWasExist: failed = ${it.message}")
                    }
                }
            }
        }

    fun forgotPassword(email: String, activity: Activity, isSuccess: (Boolean, String) -> Unit) =
        viewModelScope.launch {
            authRepository.forgotPassword(email).collect {
                when (it) {
                    is State.Loading -> DialogHelpers.showLoadingDialog(activity)
                    is State.Success -> {
                        DialogHelpers.hideLoadingDialog()
                        isSuccess(true, "Permintaan telah dikirim")
                    }
                    is State.Failed -> {
                        DialogHelpers.hideLoadingDialog()
                        isSuccess(false, "Akun tidak ditemukan")
                    }
                }
            }
        }
}