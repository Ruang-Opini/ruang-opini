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
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var skeleton: Skeleton
    private val model: HomeViewModel by viewModel()
    private val concatAdapter = ConcatAdapter()

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

        model.getTrending()


        model.trending.observe(viewLifecycleOwner, {
            Log.d("TAG", "trending-twit: $it")
            if (it.first() != "upstream request timeout") {
                val header = HeaderAdapter("#OnTrendingTwitter")
                val trending = TrendingAdapter(requireContext(), it)
                concatAdapter.addAdapter(header)
                concatAdapter.addAdapter(trending)
                updateAdapter()
            }


            val headerTwo = HeaderAdapter("\nYang Trending Lainnya")
            // TODO: 6/1/2021 get data from trending from category
            val anotherPop = TrendingCategoryAdapter(
                requireContext(), listOf(
                    "Korupsi", "Medis", "Transportasi", "BUMN"
                )
            )
            concatAdapter.addAdapter(headerTwo)
            concatAdapter.addAdapter(anotherPop)
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
        else skeleton.showOriginal()
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}