package id.ruangopini.ui.base.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.ruangopini.data.model.Trend
import id.ruangopini.databinding.ItemTrendingBinding
import id.ruangopini.ui.policy.trending.DetailTrendingPolicyActivity
import id.ruangopini.utils.DataHelpers
import id.ruangopini.utils.Helpers.hideView

class TrendingAdapter(
    private val context: Context,
) : RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {

    private val listOfTrending = mutableListOf<Trend>()
    fun addAll(list: List<Trend>) {
        listOfTrending.clear()
        listOfTrending.addAll(list)
        notifyDataSetChanged()
    }

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

        val sentiment = trending.respond
        val totalSentiment = sentiment.negative.plus(sentiment.positive)
        val percentNegative =
            ((sentiment.negative.toFloat()).div(totalSentiment.toFloat())).times(100).toInt()
        val percentPositive = 100 - percentNegative

        val totalBuzzer = trending.buzzer.buzzer.plus(trending.buzzer.nonBuzzer)
        val percentBuzzer =
            ((trending.buzzer.buzzer.toFloat()).div(totalBuzzer.toFloat())).times(100).toInt()

        with(binding) {
            if (totalSentiment == 0) {
                titleRespond.hideView()
                progressIndicator.hideView()
                tvPrecentPositive.hideView()
                tvPercentNegative.hideView()
            }
            if (totalBuzzer == 0) buzzerView.hideView()
            tvTrendingName.text = trending.name
            progressIndicator.apply {
                progress = percentPositive
                max = 100
            }

            tvPercentNegative.text = buildString { append(percentNegative).append("% Negitf") }
            tvPrecentPositive.text = buildString { append(percentPositive).append("% Positif") }

            val colorBuzzer = DataHelpers.getColorBuzzer(percentBuzzer)
            buzzerPercentage.apply {
                text = buildString { append(percentBuzzer).append("%") }
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        DataHelpers.getTextColorBuzzer(colorBuzzer)
                    )
                )
            }
            ivBuzzer.imageTintList = ContextCompat.getColorStateList(context, colorBuzzer)
            tvBuzzerInfo.text = DataHelpers.getTextBuzzer(percentBuzzer)

            contentTending.setOnClickListener {
                context.startActivity(Intent(context, DetailTrendingPolicyActivity::class.java)
                    .apply {
                        putExtra(DetailTrendingPolicyActivity.EXTRA_TRENDING, trending)
                    })
            }
        }
    }

    override fun getItemCount(): Int = listOfTrending.size
}