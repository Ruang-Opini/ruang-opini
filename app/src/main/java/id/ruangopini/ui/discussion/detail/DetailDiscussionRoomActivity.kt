package id.ruangopini.ui.discussion.detail

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.Discussion
import id.ruangopini.databinding.ActivityDetailDiscussionRoomBinding
import id.ruangopini.ui.post.create.CreatePostActivity
import id.ruangopini.ui.post.fragment.PostFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class DetailDiscussionRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDiscussionRoomBinding
    private lateinit var currentDiscussionId: String
    private val model: DetailDiscussionViewModel by viewModel()
    private var isAlreadyJoin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiscussionRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        with(binding) {
            btnCreatePost.hide()

            intent.getParcelableExtra<Discussion>(EXTRA_DISCUSSION)?.let { discussion ->
                currentDiscussionId = discussion.discussionId.toString()
                layoutActionbar.title.text = discussion.name
                model.getDiscussion(discussion.discussionId ?: "")

                val userId = Firebase.auth.currentUser?.uid ?: ""
                isAlreadyJoin = discussion.members?.find { it == userId }?.isNotEmpty() ?: false

                layoutActionbar.btnJoin.setOnClickListener {
                    if (isAlreadyJoin) model.leaveDiscussion(discussion)
                    else model.joinDiscussion(discussion)
                }

                viewPager.adapter = SectionPagerAdapter(this@DetailDiscussionRoomActivity)
                viewPager.isUserInputEnabled = false
                TabLayoutMediator(tabs, viewPager) { tabs, position ->
                    tabs.text = listOf("Populer", "Terbaru")[position]
                }.attach()


                btnCreatePost.setOnClickListener {
                    startActivity(Intent(applicationContext, CreatePostActivity::class.java)
                        .apply {
                            putExtra(
                                CreatePostActivity.EXTRA_DISCUSSION,
                                discussion.discussionId
                            )
                        })
                }
            }

            model.isJoin.observe(this@DetailDiscussionRoomActivity, {
                isAlreadyJoin = it
                if (it) {
                    layoutActionbar.btnJoin.text = "Keluar"
                    btnCreatePost.show()
                } else {
                    layoutActionbar.btnJoin.text = "Gabung"
                    btnCreatePost.hide()
                }
            })
        }
    }

    private inner class SectionPagerAdapter(
        fa: FragmentActivity
    ) : FragmentStateAdapter(fa) {
        val pageType = listOf(
            PostFragment.Companion.GetPost.DISCUSSION_POPULAR,
            PostFragment.Companion.GetPost.DISCUSSION_ID_NEW
        )

        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment =
            PostFragment.newInstance(pageType[position], currentDiscussionId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_DISCUSSION = "extra_discussion"
    }
}