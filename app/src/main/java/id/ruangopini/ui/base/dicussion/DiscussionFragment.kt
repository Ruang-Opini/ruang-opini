package id.ruangopini.ui.base.dicussion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import id.ruangopini.databinding.FragmentDiscussionBinding

class DiscussionFragment : Fragment() {

    private lateinit var binding: FragmentDiscussionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscussionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewPager.adapter = SectionPagerAdapter(requireActivity())
            TabLayoutMediator(tabs, viewPager) { tabs, position ->
                tabs.text = listOf("Populer", "Terbaru")[position]
            }.attach()
        }
    }

    private inner class SectionPagerAdapter(
        fa: FragmentActivity
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment =
            ContentDiscussionFragment.newInstance(position.plus(1))
    }

    companion object {

        @JvmStatic
        fun newInstance() = DiscussionFragment()
    }
}