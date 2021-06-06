package id.ruangopini.ui.help

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.SimpleItemAnimator
import coil.load
import id.ruangopini.R
import id.ruangopini.data.model.ItemHelp
import id.ruangopini.databinding.ActivityHelpBinding
import id.ruangopini.ui.base.reference.adapter.HeaderAdapter
import id.ruangopini.utils.DataHelpers
import id.ruangopini.utils.Helpers.isDarkMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class HelpActivity : AppCompatActivity(), HelpListener {

    private lateinit var binding: ActivityHelpBinding
    private val model: HelpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Bantuan"
            setDisplayHomeAsUpEnabled(true)
        }

        binding.ivLogo.load(if (isDarkMode()) R.drawable.ic_logo_night else R.drawable.ic_logo) {
            crossfade(true)
        }

        val manageAccount = HeaderAdapter("Mengelola Akun")
        val dataManageAccount = HelpAdapter(DataHelpers.dataHelpManageAccount, this)
        val discussionRoom = HeaderAdapter("Ruang Diskusi")
        val dataDiscussionRoom = HelpAdapter(DataHelpers.dataHelpDiscussionRoom, this)
        val reference = HeaderAdapter("Referensi")
        val dataReference = HelpAdapter(DataHelpers.dataHelpReference, this)
        val report = HeaderAdapter("Aduan")
        val dataReport = HelpAdapter(DataHelpers.dataHelpReport, this)

        val concat = ConcatAdapter(
            manageAccount, dataManageAccount,
            discussionRoom, dataDiscussionRoom,
            reference, dataReference,
            report, dataReport
        )

        binding.rvHelp.apply {
            itemAnimator = DefaultItemAnimator()
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            adapter = concat
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onRespondHelper(itemHelp: ItemHelp, isHelp: Boolean) {
        model.onRespond(itemHelp, isHelp)
    }
}