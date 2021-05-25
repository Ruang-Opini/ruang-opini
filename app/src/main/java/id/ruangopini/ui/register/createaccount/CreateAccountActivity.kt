package id.ruangopini.ui.register.createaccount

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import id.ruangopini.data.model.User
import id.ruangopini.databinding.ActivityCreateAccountBinding
import id.ruangopini.ui.register.uploadphoto.UploadPhotoActivity
import id.ruangopini.utils.Helpers.afterTextChanged
import id.ruangopini.utils.Helpers.getPlainText
import id.ruangopini.utils.Helpers.showError
import id.ruangopini.utils.Helpers.validateError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@FlowPreview
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private val model: CreateAccountViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model.isComplete.observe(this, { binding.btnCreateAccount.isEnabled = it })

        with(binding) {
            lifecycleScope.launch {
                edtName.afterTextChanged { validateName(it) }
            }

            lifecycleScope.launch {
                edtUsername.afterTextChanged { validateUserName(it) }
            }

            lifecycleScope.launch {
                edtEmail.afterTextChanged { validateEmail(it) }
            }

            lifecycleScope.launch {
                edtPassword.afterTextChanged { validatePassword(it) }
            }

            lifecycleScope.launch {
                edtConfirmPassword.afterTextChanged { validateConfirmPassword(it) }
            }

            btnCreateAccount.setOnClickListener {
                val user = User(
                    edtName.getPlainText(), edtUsername.getPlainText(), edtEmail.getPlainText()
                )
                Log.d("TAG", "create user: $user")
                model.createAccount(
                    user, binding.edtPassword.getPlainText(), this@CreateAccountActivity
                ) {
                    startActivity(Intent(applicationContext, UploadPhotoActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun validateConfirmPassword(it: String) = with(binding) {
        val valid = it == edtPassword.getPlainText()
        model.setProgress(4, valid)
        if (!valid) tilConfirmPassword.showError("Password Tidak Sama")
        else validateError(tilConfirmPassword)
    }

    private fun validatePassword(it: String) = with(binding) {
        val valid = it.isEmpty()
        model.setProgress(3, !valid)
        if (valid) tilPassword.showError()
        else validateError(tilPassword)
    }

    private fun validateEmail(it: String) = with(binding) {
        val valid = Patterns.EMAIL_ADDRESS.matcher(it).matches()
        model.setProgress(2, valid)
        if (!valid) tilEmail.showError("Email tidak valid")
        else validateError(tilEmail)
    }

    private fun validateUserName(it: String) = with(binding) {
        val valid = it.split(" ").size == 1 && noSpecialChar(it)
        model.setProgress(1, valid)
        if (valid) validateError(tilUsername) else tilUsername.showError("Username tidak valid")
    }

    private fun validateName(it: String) = with(binding) {
        val valid = it.isEmpty()
        model.setProgress(0, !valid)
        if (valid) tilName.showError()
        else validateError(tilName)
    }

    private fun noSpecialChar(it: String): Boolean {
        val pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        return !pattern.matcher(it).find()
    }
}