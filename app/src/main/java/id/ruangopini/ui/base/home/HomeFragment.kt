package id.ruangopini.ui.base.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import id.ruangopini.R
import id.ruangopini.databinding.FragmentHomeBinding
import id.ruangopini.ui.base.reference.adapter.HeaderAdapter
import id.ruangopini.utils.Helpers.initSkeleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var skeleton: Skeleton
    private val model: HomeViewModel by viewModel()
    private var concatAdapter = ConcatAdapter()
    private var header = HeaderAdapter("#OnTrendingTwitter")
    private lateinit var trending: TrendingAdapter
    private var headerTwo = HeaderAdapter("\nApa lagi yang populer?")
    private lateinit var anotherPop: TrendingCategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skeleton = binding.rvHome
            .applySkeleton(R.layout.item_trending, 20)
            .apply { initSkeleton(requireContext()) }

        binding.swipeRefresh.setOnRefreshListener {
            model.getTrending()
            binding.swipeRefresh.isRefreshing = false
        }

        model.getTrending()
        model.getAnotherPopular()

        trending = TrendingAdapter(requireContext())
        anotherPop = TrendingCategoryAdapter(requireContext())
        concatAdapter = ConcatAdapter(header, trending, headerTwo, anotherPop)
        updateAdapter()

        model.category.observe(viewLifecycleOwner, {
            Log.d("TAG", "trending-another: $it")
            anotherPop.addAll(it)
        })

        model.trending.observe(viewLifecycleOwner, {
            Log.d("TAG", "trending-twit: $it")
            trending.addAll(it)
            updateAdapter()
        })

        model.loading.observe(viewLifecycleOwner, { populateLoading(it) })
    }

    private fun updateAdapter() {
        binding.rvHome.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = concatAdapter
        }
    }

    private fun populateLoading(it: Boolean) {
        if (it) skeleton.showSkeleton()
        else {
            skeleton.showOriginal()
            updateAdapter()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}