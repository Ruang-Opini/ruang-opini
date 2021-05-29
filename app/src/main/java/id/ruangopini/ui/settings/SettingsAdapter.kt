package id.ruangopini.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.MainActivity
import id.ruangopini.data.model.ItemSetting
import id.ruangopini.databinding.ItemSettingsBinding
import id.ruangopini.ui.register.createaccount.CreateAccountActivity
import id.ruangopini.utils.DialogHelpers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                    DialogHelpers.showLoadingDialog(context as Activity, "Keluar...")
                    GlobalScope.launch {
                        delay(2000)
                        Firebase.auth.signOut()
                        context.let {
                            it.startActivity(Intent(it, CreateAccountActivity::class.java))
                            it.finishAffinity()
                        }
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int = listSettings.size
}