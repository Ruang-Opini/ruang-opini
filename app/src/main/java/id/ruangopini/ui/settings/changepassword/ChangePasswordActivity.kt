package id.ruangopini.ui.settings.changepassword

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import id.ruangopini.databinding.ActivityChangePasswordBinding
import id.ruangopini.utils.Helpers
import id.ruangopini.utils.Helpers.afterTextChanged
import id.ruangopini.utils.Helpers.getPlainText
import id.ruangopini.utils.Helpers.showError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private val model: ChangePasswordViewModel by viewModel()

    @FlowPreview
    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Ubah Password"
            setDisplayHomeAsUpEnabled(true)
        }

        model.isComplete.observe(this, { binding.btnChangePassword.isEnabled = it })

        with(binding) {
            lifecycleScope.launch {
                edtOldPassword.afterTextChanged { validatePassword(it, tilOldPassword, 0) }
            }
            lifecycleScope.launch {
                edtNewPassword.afterTextChanged { validatePassword(it, tilNewPassword, 1) }
            }

            btnChangePassword.setOnClickListener {
                model.updatePassword(
                    this@ChangePasswordActivity,
                    edtOldPassword.getPlainText(),
                    edtNewPassword.getPlainText()
                )
            }
        }

        model.isUpdateSuccess.observe(this, {
            if (it) finish()
            else binding.tilOldPassword.showError("Password Salah")
        })


    }

    private fun validatePassword(it: String, til: TextInputLayout, progress: Int) {
        val valid = it.isEmpty()
        model.setProgress(progress, !valid)
        if (valid) til.showError()
        else Helpers.validateError(til)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}