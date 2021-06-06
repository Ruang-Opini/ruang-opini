package id.ruangopini.ui.help

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ruangopini.data.model.ItemHelp
import id.ruangopini.databinding.ItemHelpBinding
import id.ruangopini.utils.DataHelpers
import id.ruangopini.utils.Helpers.addImage
import id.ruangopini.utils.Helpers.hideView
import id.ruangopini.utils.Helpers.showView

class HelpAdapter(
    private val listHelp: List<ItemHelp>,
    private val listener: HelpListener
) : RecyclerView.Adapter<HelpAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemHelpBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemHelpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listHelp[position]
        binding.apply {
            tvTitleHelp.text = data.question
            var isExpanded = data.isExpand ?: false
            contentHelp.setOnClickListener {
                if (isExpanded) contentAnswer.hideView() else contentAnswer.showView()
                isExpanded = !isExpanded
                data.isExpand = isExpanded
                ivDropDown.animate().setDuration(500).rotationBy(180f).start()
            }

            tvYes.setOnClickListener {
                listener.onRespondHelper(data, true)
                tvNo.hideView()
            }
            tvNo.setOnClickListener {
                listener.onRespondHelper(data, false)
                tvYes.hideView()
            }

            val content = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(data.answer, Html.FROM_HTML_MODE_COMPACT)
            else Html.fromHtml(data.answer)
            tvContentHelp.apply {
                text = content
                data.icons.forEach {
                    addImage(it, DataHelpers.listIcon[it] ?: 0)
                }
            }
        }
    }

    override fun getItemCount(): Int = listHelp.size
}

interface HelpListener {
    fun onRespondHelper(itemHelp: ItemHelp, isHelp: Boolean)
}