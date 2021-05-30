package id.ruangopini.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import id.ruangopini.MainActivity
import id.ruangopini.R
import id.ruangopini.data.repo.remote.firebase.auth.AuthHelpers
import id.ruangopini.databinding.ActivityLoginBinding
import id.ruangopini.databinding.DialogForgotPasswordBinding
import id.ruangopini.ui.register.createaccount.CreateAccountActivity
import id.ruangopini.utils.DialogHelpers
import id.ruangopini.utils.Helpers
import id.ruangopini.utils.Helpers.afterTextChanged
import id.ruangopini.utils.Helpers.getPlainText
import id.ruangopini.utils.Helpers.isDarkMode
import id.ruangopini.utils.Helpers.showError
import id.ruangopini.utils.Helpers.showToast
import id.ruangopini.utils.LoginState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Pattern

@FlowPreview
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val model: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.contentLogin.setBackgroundColor(
            ContextCompat.getColor(
                this, if (isDarkMode()) R.color.primary_20 else R.color.primary
            )
        )

        model.isComplete.observe(this, { binding.btnLogin.isEnabled = it })

        with(binding) {
            lifecycleScope.launch {
                edtUsername.afterTextChanged { validateUserName(it) }
            }
            lifecycleScope.launch {
                edtPassword.afterTextChanged { validatePassword(it) }
            }

            btnLogin.setOnClickListener {
                model.login(
                    edtUsername.getPlainText(),
                    edtPassword.getPlainText(),
                    this@LoginActivity
                )
            }

            btnForgotPassword.setOnClickListener {
                showForgetPasswordDialog()
            }

            btnRegisterNow.setOnClickListener {
                startActivity(Intent(this@LoginActivity, CreateAccountActivity::class.java))
            }

            btnGso.setOnClickListener {
                gsoResult.launch(AuthHelpers.getGoogleClient(this@LoginActivity).signInIntent)
            }
        }

        model.isLoginSuccess.observe(this, { state ->
            with(binding) {
                when (state) {
                    LoginState.SUCCESS -> {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    LoginState.WRONG_PASSWORD -> tilPassword.showError("Password salah")
                    LoginState.WRONG_USERNAME -> tilUsername.showError("Username tidak ditemukan")
                }
            }
        })
    }

    private fun showForgetPasswordDialog() {
        DialogHelpers.createBottomSheetDialog(
            this@LoginActivity, R.layout.dialog_forgot_password
        ) { view, dialog ->
            DialogHelpers.showForgotPassword(
                DialogForgotPasswordBinding.bind(view), dialog
            ) {
                model.forgotPassword(it, this@LoginActivity) { success, message ->
                    if (success) {
                        dialog.dismiss()
                        showToast(applicationContext, message)
                    } else showToast(applicationContext, message)
                }
            }
        }
    }

    private val gsoResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        task.getResult(ApiException::class.java)?.let {
            model.loginWithGoogle(it, this)
        }
    }

    private fun validateUserName(it: String) = with(binding) {
        val valid = it.split(" ").size == 1 && noSpecialChar(it)
        model.setProgress(0, valid)
        if (valid) Helpers.validateError(tilUsername) else tilUsername.showError("Username tidak valid")
    }

    private fun noSpecialChar(it: String): Boolean {
        val pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        return !pattern.matcher(it).find()
    }

    private fun validatePassword(it: String) = with(binding) {
        val valid = it.isEmpty()
        model.setProgress(1, !valid)
        if (valid) tilPassword.showError()
        else Helpers.validateError(tilPassword)
    }
}