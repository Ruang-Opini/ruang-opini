package id.ruangopini.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import coil.load
import id.ruangopini.MainActivity
import id.ruangopini.R
import id.ruangopini.databinding.ActivitySplashBinding
import id.ruangopini.utils.Helpers.isDarkMode

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivLogo.load(
            if (isDarkMode()) R.drawable.ic_logo_night else R.drawable.ic_logo
        ) { crossfade(true) }

        Handler(Looper.getMainLooper()).postDelayed({
            // TODO: 5/23/2021 check if user already login or not, if not start activity to login
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}