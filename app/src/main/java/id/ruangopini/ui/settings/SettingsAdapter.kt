package id.ruangopini.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ruangopini.MainActivity
import id.ruangopini.data.model.ItemSetting
import id.ruangopini.data.repo.remote.firebase.auth.AuthHelpers
import id.ruangopini.databinding.ItemSettingsBinding
import id.ruangopini.ui.login.LoginActivity
import id.ruangopini.utils.DialogHelpers
import kotlinx.coroutines.*

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class SettingsAdapter(
    private val context: Context,
    private val listSettings: List<ItemSetting>
) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemSettingsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val setting = listSettings[position]
        with(binding) {
            tvSettings.text = setting.name
            contentSettings.setOnClickListener {
                setting.target?.let { target ->
                    if (target != MainActivity::class.java)
                        context.startActivity(Intent(context, target))
                }
                if (setting.target == null) {
                    if (setting.name == "Keluar") {
                        DialogHelpers.showLoadingDialog(context as Activity, "Keluar...")
                        GlobalScope.launch {
                            delay(2000)
                            AuthHelpers.signOut(context)
                            context.let {
                                it.startActivity(Intent(it, LoginActivity::class.java))
                                it.finishAffinity()
                            }
                        }
                    } else context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/ePQ8SSSgmn8e9YL3A"))
                    )
                }
            }

        }
    }

    override fun getItemCount(): Int = listSettings.size
}