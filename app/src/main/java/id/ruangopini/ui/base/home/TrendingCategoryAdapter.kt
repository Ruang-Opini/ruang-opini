package id.ruangopini.ui.base.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ruangopini.databinding.ItemCategoryTrendingBinding

class TrendingCategoryAdapter(
    private val context: Context,
    private val listOfTrending: List<String>
) : RecyclerView.Adapter<TrendingCategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemCategoryTrendingBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemCategoryTrendingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.tvTrendingCategory.text = listOfTrending[position]
    }

    override fun getItemCount(): Int = listOfTrending.size
}