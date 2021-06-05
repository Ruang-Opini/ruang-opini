package id.ruangopini

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import id.ruangopini.databinding.ActivityMainBinding
import id.ruangopini.ui.base.dicussion.DiscussionFragment
import id.ruangopini.ui.base.home.HomeFragment
import id.ruangopini.ui.base.profile.ProfileFragment
import id.ruangopini.ui.base.reference.ReferenceFragment
import id.ruangopini.ui.discussion.create.CreateDiscussionActivity
import id.ruangopini.utils.DataHelpers
import id.ruangopini.utils.Helpers.hideView
import id.ruangopini.utils.Helpers.showView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.btnAddDiscussion.apply {
            hide()
            setOnClickListener {
                startActivity(Intent(this@MainActivity, CreateDiscussionActivity::class.java))
            }
        }
        binding.viewPager.apply {
            adapter = SectionPagerAdapter(
                this@MainActivity,
                listOf(
                    HomeFragment.newInstance(),
                    DiscussionFragment.newInstance(),
                    ReferenceFragment.newInstance(),
                    ProfileFragment.newInstance()
                )
            )
            isUserInputEnabled = false
            registerOnPageChangeCallback(onPageChange)
        }

        binding.bottomNav.setNavigationChangeListener { _, position ->
            binding.viewPager.currentItem = position
        }
    }

    private val onPageChange = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            populateChangeListener(position)
        }
    }

    private fun populateChangeListener(position: Int) {
        binding.bottomNav.setCurrentActiveItem(position)
        supportActionBar?.title = DataHelpers.titleMainTab[position]
        binding.appBarLayout.apply {
            if (position == 1 || position == 3) this.hideView()
            else this.showView()
        }
        binding.btnAddDiscussion.apply {
            if (position == 1) show() else hide()
        }
    }

    private inner class SectionPagerAdapter(
        fa: FragmentActivity, private val fragments: List<Fragment>
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}