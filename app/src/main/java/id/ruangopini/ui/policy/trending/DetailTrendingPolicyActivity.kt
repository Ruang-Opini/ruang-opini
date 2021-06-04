package id.ruangopini.ui.policy.trending

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.DefaultItemAnimator
import id.ruangopini.data.model.Buzzer
import id.ruangopini.data.model.Respond
import id.ruangopini.data.model.Trend
import id.ruangopini.databinding.ActivityDetailTrendingPolicyBinding
import id.ruangopini.ui.base.dicussion.DiscussionRoomAdapter
import id.ruangopini.ui.base.dicussion.DiscussionViewModel
import id.ruangopini.ui.discussion.create.CreateDiscussionActivity
import id.ruangopini.utils.DataHelpers
import id.ruangopini.utils.Helpers.hideView
import id.ruangopini.utils.Helpers.reSize
import id.ruangopini.utils.Helpers.showView
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailTrendingPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTrendingPolicyBinding
    private val model: DiscussionViewModel by viewModel()
    private val trendingModel: DetailTrendingPolicyViewModel by viewModel()
    private lateinit var policyName: String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTrendingPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        intent.extras?.getParcelable<Trend>(EXTRA_TRENDING)?.let {
            policyName = it.name
            model.getDiscussionByIssueName(it.name)
            supportActionBar?.apply {
                title = it.name
                setDisplayHomeAsUpEnabled(true)
            }
            binding.contentDetail.tvPolicyName.text = it.name
            populateBuzzer(it.buzzer)
            populateRespond(it.respond)

            if (it.buzzer.buzzer == 0) trendingModel.getBuzzer(it.name)
            if (it.respond.positive == 0) trendingModel.getSentiment(it.name)
        }

        model.discussion.observe(this, {
            binding.contentDetail.rvDiscussionRoom.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = DiscussionRoomAdapter(this@DetailTrendingPolicyActivity, it)
            }
        })

        trendingModel.respond.observe(this) { populateRespond(it) }
        trendingModel.buzzer.observe(this) { populateBuzzer(it) }

        with(binding) {
            contentDetail.contentScroll.setOnScrollChangeListener { _, _, y, _, oldY ->
                if (y > oldY) btnCreateDiscussion.shrink() else btnCreateDiscussion.extend()
            }

            btnCreateDiscussion.setOnClickListener {
                startActivity(
                    Intent(this@DetailTrendingPolicyActivity, CreateDiscussionActivity::class.java)
                        .apply { putExtra(CreateDiscussionActivity.EXTRA_POLICY, policyName) }
                )
            }
        }
        // TODO: 6/1/2021 get data from
    }

    private fun populateRespond(respond: Respond) = with(binding.contentDetail) {
        Log.d("TAG", "populateRespond: $respond")
        val totalSentiment = respond.negative.plus(respond.positive)
        val percentNegative =
            ((respond.negative.toFloat()).div(totalSentiment.toFloat())).times(100).toInt()
        val percentPositive = 100 - percentNegative

        listOf(
            titleRespond, circularProgressIndicator, tvTitlePositive, tvTitleNegative,
            positiveView, negativeView, tvPrecentPositive, tvPercentNegative,
            tvAmountPositive, tvAmountNegative
        ).forEach {
            if (totalSentiment == 0) it.hideView() else it.showView()
        }

        circularProgressIndicator.apply {
            progress = percentPositive
            max = 100
            if (totalSentiment != 0) doOnPreDraw {
                indicatorSize = it.height.reSize(95)
                trackThickness = it.height.reSize(30)
            }
        }
        tvPercentNegative.text = buildString { append(percentNegative).append("%") }
        tvPrecentPositive.text = buildString { append(percentPositive).append("%") }
        tvAmountPositive.text = buildString { append("(").append(respond.positive).append(")") }
        tvAmountNegative.text = buildString { append("(").append(respond.negative).append(")") }

    }

    private fun populateBuzzer(buzzer: Buzzer) = with(binding.contentDetail) {
        Log.d("TAG", "populateBuzzer: $buzzer")
        val totalBuzzer = buzzer.buzzer.plus(buzzer.nonBuzzer)
        val percentBuzzer =
            ((buzzer.buzzer.toFloat()).div(totalBuzzer.toFloat())).times(100).toInt()
        buzzerView.let { if (totalBuzzer == 0) it.hideView() else it.showView() }

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
        ivBuzzer.imageTintList = ContextCompat.getColorStateList(applicationContext, colorBuzzer)
        tvBuzzerInfo.text = DataHelpers.getTextBuzzer(percentBuzzer)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_TRENDING = "extra_trending"
    }
}