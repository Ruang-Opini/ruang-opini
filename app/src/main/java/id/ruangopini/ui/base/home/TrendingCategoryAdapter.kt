package id.ruangopini.ui.base.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ruangopini.data.model.CategoryAnalytics
import id.ruangopini.databinding.ItemCategoryTrendingBinding

class TrendingCategoryAdapter(
    private val context: Context
) : RecyclerView.Adapter<TrendingCategoryAdapter.ViewHolder>() {

    private val listOfTrending = mutableListOf<CategoryAnalytics>()
    fun addAll(list: List<CategoryAnalytics>) {
        listOfTrending.clear()
        listOfTrending.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemCategoryTrendingBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemCategoryTrendingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = listOfTrending[position]
        with(binding) {
            tvTrendingCategory.text = category.name
            tvAmountOfTrending.text = buildString {
                append(category.join).append(" Orang membicarakan ini")
            }
        }

    }

    override fun getItemCount(): Int = listOfTrending.size
}