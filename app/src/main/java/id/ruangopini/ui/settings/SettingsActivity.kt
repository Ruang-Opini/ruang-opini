package id.ruangopini.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import id.ruangopini.MainActivity
import id.ruangopini.data.model.ItemSetting
import id.ruangopini.databinding.ActivitySettingsBinding
import id.ruangopini.ui.settings.changepassword.ChangePasswordActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Setelan"
            setDisplayHomeAsUpEnabled(true)
        }

        binding.rvSettings.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = SettingsAdapter(
                this@SettingsActivity,
                listOf(
                    // TODO: 5/28/2021 change with exact target
                    ItemSetting("Bantuan", MainActivity::class.java),
                    ItemSetting("Ubah Password", ChangePasswordActivity::class.java),
                    ItemSetting("Tentang Kami", MainActivity::class.java),
                    ItemSetting("Keluar"),
                )
            )
        }
    }
}