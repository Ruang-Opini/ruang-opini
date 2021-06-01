package id.ruangopini.ui.policy.trending

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.DefaultItemAnimator
import id.ruangopini.databinding.ActivityDetailTrendingPolicyBinding
import id.ruangopini.ui.base.dicussion.DiscussionRoomAdapter
import id.ruangopini.ui.base.dicussion.DiscussionViewModel
import id.ruangopini.ui.discussion.create.CreateDiscussionActivity
import id.ruangopini.utils.Helpers.reSize
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailTrendingPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTrendingPolicyBinding
    private val model: DiscussionViewModel by viewModel()
    private lateinit var policyName: String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTrendingPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        intent.extras?.getString(EXTRA_TRENDING)?.let {
            policyName = it
            supportActionBar?.apply {
                title = it
                setDisplayHomeAsUpEnabled(true)
            }
            binding.contentDetail.tvPolicyName.text = it
        }

        model.getLatestDiscussion(this)
        model.discussion.observe(this, {
            binding.contentDetail.rvDiscussionRoom.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = DiscussionRoomAdapter(this@DetailTrendingPolicyActivity, it)
            }
        })

        with(binding) {
            contentDetail.contentScroll.setOnScrollChangeListener { _, _, y, _, oldY ->
                if (y > oldY) btnCreateDiscussion.shrink() else btnCreateDiscussion.extend()
            }


            contentDetail.circularProgressIndicator.apply {
                doOnPreDraw {
                    indicatorSize = it.height.reSize(95)
                    trackThickness = it.height.reSize(30)
                }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_TRENDING = "extra_trending"
    }
}