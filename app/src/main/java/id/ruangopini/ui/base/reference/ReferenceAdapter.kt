package id.ruangopini.ui.base.reference

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ruangopini.data.model.PolicyCategory
import id.ruangopini.databinding.ItemPolicyCategoryBinding

class ReferenceAdapter(
    private val context: Context,
    private val categories: List<PolicyCategory>
) : RecyclerView.Adapter<ReferenceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemPolicyCategoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemPolicyCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categories[position]
        binding.tvCategoryName.text = item.name
    }

    override fun getItemCount(): Int = categories.size

}