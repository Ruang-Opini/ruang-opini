package id.ruangopini.ui.base.reference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import id.ruangopini.R
import id.ruangopini.data.model.Category
import id.ruangopini.databinding.FragmentReferenceBinding
import id.ruangopini.ui.base.reference.adapter.HeaderAdapter
import id.ruangopini.ui.base.reference.adapter.PolicyAdapter
import id.ruangopini.utils.Helpers.initSkeleton
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReferenceFragment : Fragment() {

    private lateinit var binding: FragmentReferenceBinding
    private lateinit var skeleton: Skeleton
    private val model: ReferenceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReferenceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skeleton = binding.rvReference
            .applySkeleton(R.layout.item_policy_category, 20)
            .apply { initSkeleton(requireContext()) }
        binding.swipeRefresh.apply {
            setOnRefreshListener {
                model.getAllPolicyCategory()
                isRefreshing = false
            }
        }
        model.getAllPolicyCategory()
        model.policyCategory.observe(viewLifecycleOwner, { populateData(it) })
        model.isLoading.observe(viewLifecycleOwner, { populateLoading(it) })
    }

    private fun populateData(it: Category) {
        val isNeedResult = arguments?.getBoolean(RESULT_ARG, false)
        val firstAdapter = PolicyAdapter(requireContext(), it.listType, 1, isNeedResult)
        val secondAdapter = PolicyAdapter(requireContext(), it.listCategory, 2, isNeedResult)

        val mAdapter = ConcatAdapter(
            HeaderAdapter("Jenis Kebijakan"), firstAdapter,
            HeaderAdapter("Kategori Kebijakan"), secondAdapter
        )

        binding.rvReference.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }
    }

    private fun populateLoading(isLoading: Boolean) {
        if (isLoading) skeleton.showSkeleton()
        else skeleton.showOriginal()
    }

    companion object {

        private const val RESULT_ARG = "result_arg"

        @JvmStatic
        fun newInstance(needResult: Boolean? = false) = ReferenceFragment().apply {
            arguments = Bundle().apply {
                putBoolean(RESULT_ARG, needResult ?: false)
            }
        }
    }
}