package id.ruangopini.ui.policy.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ruangopini.data.model.Policy
import id.ruangopini.databinding.ItemReferenceBinding
import id.ruangopini.utils.Helpers.hideView
import id.ruangopini.utils.Helpers.toTitleCase

class DetailPolicyAdapter(
    private val mContext: Context,
    private val listPolicy: List<Policy>,
    private val policyListener: PolicyListener? = null
) : RecyclerView.Adapter<DetailPolicyAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemReferenceBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemReferenceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val policy = listPolicy[position]
        with(binding) {
            if (policy.url.isBlank()) {
                tvTitlePolicyType.hideView()
                tvPolicyType.hideView()
                tvTitlePolicyNum.text = "Peraturan"
                tvTitlePolicyYear.hideView()
                tvPolicyYear.hideView()
            }

            tvPolicyType.text = policy.type
            tvPolicyNum.text = policy.policyNum
            tvPolicyName.text = policy.name.toTitleCase()
            tvPolicyYear.text = policy.year

            policy.documents.documents.let { populateDocument(it) }
            policyListener?.let { listener ->
                contentReference.setOnClickListener { listener.onPolicyCollect(policy) }
            }

        }
    }

    private fun populateDocument(it: List<String>) = with(binding) {
        if (it.isNotEmpty()) rvPolicyDocs.apply {
            if (it.size == 1) layoutManager = LinearLayoutManager(mContext)
            itemAnimator = DefaultItemAnimator()
            adapter = PolicyDocumentAdapter(mContext, it)
        }
    }

    override fun getItemCount(): Int = listPolicy.size
}

interface PolicyListener {
    fun onPolicyCollect(policy: Policy)
}