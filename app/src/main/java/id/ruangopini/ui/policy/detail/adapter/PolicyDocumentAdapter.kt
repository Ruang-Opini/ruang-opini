package id.ruangopini.ui.policy.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ruangopini.databinding.ItemDocumentBinding
import id.ruangopini.utils.Helpers.getPdfName

class PolicyDocumentAdapter(
    private val context: Context,
    private val listDocument: List<String>
) : RecyclerView.Adapter<PolicyDocumentAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemDocumentBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemDocumentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val document = listDocument[position]
        binding.tvDocName.text = document.getPdfName()
    }

    override fun getItemCount(): Int = listDocument.size
}