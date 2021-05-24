package id.ruangopini.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.MainActivity
import id.ruangopini.R
import id.ruangopini.databinding.ActivitySplashBinding
import id.ruangopini.ui.register.createaccount.CreateAccountActivity
import id.ruangopini.utils.Helpers.isDarkMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

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
                if (it != null) startActivity(Intent(this, MainActivity::class.java))
                // TODO: 5/23/2021 directed to login activity
                else startActivity(Intent(this, CreateAccountActivity::class.java))
                finish()
            }
        }, 2000)
    }
}