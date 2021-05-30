package id.ruangopini.ui.editprofile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.ruangopini.databinding.ActivitySettingsBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Edit Profile"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}