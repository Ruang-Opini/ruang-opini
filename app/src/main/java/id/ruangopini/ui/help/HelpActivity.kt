package id.ruangopini.ui.help

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.ruangopini.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Bantuan"
            setDisplayHomeAsUpEnabled(true)
        }
    }


}