package id.ruangopini.ui.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import id.ruangopini.data.model.ItemSetting
import id.ruangopini.databinding.ActivitySettingsBinding
import id.ruangopini.ui.aboutus.AboutUsActivity
import id.ruangopini.ui.help.HelpActivity
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
                    ItemSetting("Bantuan", HelpActivity::class.java),
                    ItemSetting("Ubah Password", ChangePasswordActivity::class.java),
                    ItemSetting("Tentang Kami", AboutUsActivity::class.java),
                    ItemSetting("Berikan Upan Balik"),
                    ItemSetting("Keluar"),
                )
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}