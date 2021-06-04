package id.ruangopini.ui.base.reference.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import id.ruangopini.data.model.PolicyCategory
import id.ruangopini.databinding.ItemGridListBinding
import id.ruangopini.ui.base.reference.ReferenceAdapter

class PolicyAdapter(
    private val mContext: Context,
    private val categories: List<PolicyCategory>,
    private val itemType: Int, private val isNeedResult: Boolean? = false
) : RecyclerView.Adapter<PolicyAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemGridListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemGridListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.rvGridList.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = ReferenceAdapter(mContext, categories, itemType, isNeedResult)
        }
    }

    override fun getItemCount(): Int = 1
}