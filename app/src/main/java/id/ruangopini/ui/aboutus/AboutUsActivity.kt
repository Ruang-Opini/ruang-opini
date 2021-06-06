package id.ruangopini.ui.aboutus

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import coil.load
import id.ruangopini.R
import id.ruangopini.databinding.ActivityAboutUsBinding
import id.ruangopini.utils.DataHelpers
import id.ruangopini.utils.Helpers.isDarkMode

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

        with(binding) {
            ivLogo.load(if (isDarkMode()) R.drawable.ic_logo_night else R.drawable.ic_logo) {
                crossfade(true)
            }
            rvAbout.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = AboutUsAdapter(DataHelpers.dataAboutUs)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}