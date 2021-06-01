package id.ruangopini.ui.base.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ruangopini.databinding.ItemTrendingBinding
import id.ruangopini.ui.policy.trending.DetailTrendingPolicyActivity

class TrendingAdapter(
    private val context: Context,
    private val listOfTrending: List<String>
) : RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemTrendingBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemTrendingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trending = listOfTrending[position]
        with(binding) {
            tvTrendingName.text = trending
            contentTending.setOnClickListener {
                context.startActivity(Intent(context, DetailTrendingPolicyActivity::class.java)
                    .apply { putExtra(DetailTrendingPolicyActivity.EXTRA_TRENDING, trending) })
            }
        }
    }

    override fun getItemCount(): Int = listOfTrending.size
}