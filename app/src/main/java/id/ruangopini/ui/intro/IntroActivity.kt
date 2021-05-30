package id.ruangopini.ui.intro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import id.ruangopini.data.model.Intro
import id.ruangopini.data.repo.local.LocalShared
import id.ruangopini.databinding.ActivityIntroBinding
import id.ruangopini.ui.login.LoginActivity
import id.ruangopini.utils.DataHelpers
import id.ruangopini.utils.Helpers.hideView
import id.ruangopini.utils.Helpers.showView
import kotlinx.coroutines.launch

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            viewPager.apply {
                adapter = SectionPagerAdapter(this@IntroActivity, DataHelpers.listIntro)
                registerOnPageChangeCallback(onPageChange)
            }
            btnNext.setOnClickListener {
                viewPager.currentItem += 1
            }
            btnStart.setOnClickListener {
                lifecycleScope.launch { LocalShared.setAlreadyWatchIntro(this@IntroActivity) }
                startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
                finish()
            }
        }

    }

    private val onPageChange = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            populateChange(position)
        }
    }

    private fun populateChange(position: Int) = with(binding) {
        when (position) {
            0 -> arrowPrev.hideView()
            DataHelpers.listIntro.size - 1 -> {
                arrowPrev.hideView()
                arrowNext.hideView()
                btnNext.hideView(true)
                btnStart.show()
            }
            else -> {
                btnStart.hide()
                arrowNext.showView()
                arrowPrev.showView()
                btnNext.showView()
            }
        }
    }

    private inner class SectionPagerAdapter(
        fa: FragmentActivity, private val listIntro: List<Intro>
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = listIntro.size
        override fun createFragment(position: Int): Fragment =
            IntroFragment.newInstance(listIntro[position])
    }
}