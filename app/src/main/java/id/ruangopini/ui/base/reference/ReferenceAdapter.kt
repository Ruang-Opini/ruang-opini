package id.ruangopini.ui.base.reference

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ruangopini.data.model.PolicyCategory
import id.ruangopini.databinding.ItemPolicyCategoryBinding
import id.ruangopini.ui.policy.detail.DetailPolicyActivity

class ReferenceAdapter(
    private val context: Context,
    private val categories: List<PolicyCategory>,
    private val itemType: Int,
    private val isNeedResult: Boolean? = false
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
        with(binding) {
            tvCategoryName.text = item.name
            contentCategory.setOnClickListener {
                val intent = Intent(context, DetailPolicyActivity::class.java).apply {
                    putExtra(DetailPolicyActivity.EXTRA_CATEGORY, item)
                    putExtra(DetailPolicyActivity.EXTRA_TYPE, itemType)
                }
                if (isNeedResult == true) (context as Activity).startActivityForResult(
                    intent,
                    REQ_CODE
                )
                else context.startActivity(intent)

            }
        }
    }

    override fun getItemCount(): Int = categories.size

    companion object {
        const val REQ_CODE = 101
    }

}