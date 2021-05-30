package id.ruangopini.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.MainActivity
import id.ruangopini.R
import id.ruangopini.data.model.User
import id.ruangopini.data.repo.local.LocalShared
import id.ruangopini.databinding.ActivitySplashBinding
import id.ruangopini.ui.base.profile.ProfileViewModel
import id.ruangopini.ui.intro.IntroActivity
import id.ruangopini.ui.login.LoginActivity
import id.ruangopini.ui.register.createaccount.CreateAccountActivity
import id.ruangopini.utils.Helpers.isDarkMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
@FlowPreview
@ObsoleteCoroutinesApi
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val model: ProfileViewModel by viewModel()

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivLogo.load(
            if (isDarkMode()) R.drawable.ic_logo_night else R.drawable.ic_logo
        ) { crossfade(true) }

        Handler(Looper.getMainLooper()).postDelayed({
            Firebase.auth.currentUser.let {
                if (it != null) {
                    model.getUserData()
                    model.user.observe(this, { user ->
                        if (user.username == null) toCreateAccount(it)
                        else toMain()
                    })
                } else checkNotLogin()
            }

        }, 2000)
    }

    private fun toMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun toCreateAccount(it: FirebaseUser) {
        startActivity(Intent(this, CreateAccountActivity::class.java).apply {
            putExtra(
                CreateAccountActivity.EXTRA_ACCOUNT,
                User(name = it.displayName, email = it.email)
            )
        })
        finish()
    }

    private fun checkNotLogin() = lifecycleScope.launch {
        LocalShared.isAlreadyWatchIntro(applicationContext).collect { isAlreadyWatch ->
            if (isAlreadyWatch) {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                finish()
            }
        }
    }
}