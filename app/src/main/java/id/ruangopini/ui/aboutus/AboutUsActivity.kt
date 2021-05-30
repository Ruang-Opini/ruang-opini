package id.ruangopini.ui.aboutus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.ruangopini.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Tentang Kami"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}