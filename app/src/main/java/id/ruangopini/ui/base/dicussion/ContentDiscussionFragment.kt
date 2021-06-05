package id.ruangopini.ui.base.dicussion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import id.ruangopini.R
import id.ruangopini.databinding.FragmentContentDiscussionBinding
import id.ruangopini.utils.Helpers.initSkeleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContentDiscussionFragment : Fragment() {

    private lateinit var binding: FragmentContentDiscussionBinding
    private lateinit var skeleton: Skeleton
    private val model: DiscussionViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentDiscussionBinding.inflate(inflater)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skeleton = binding.rvDiscussionRoom
            .applySkeleton(R.layout.item_discussion_room, 20)
            .apply { initSkeleton(requireContext()) }
        arguments?.getInt(POS_ARG, 1).let {
            binding.swipeRefresh.apply {
                setOnRefreshListener {
                    if (it == 1) {
                        // TODO: 5/26/2021 change to getTrendingDiscussion
                        model.getLatestDiscussion(requireContext())
                    } else model.getLatestDiscussion(requireContext())
                }
                isRefreshing = false
            }
            if (it == 1) {
                // TODO: 5/26/2021 change to getTrendingDiscussion
                model.getLatestDiscussion(requireContext())
            } else model.getLatestDiscussion(requireContext())
        }

        model.discussion.observe(viewLifecycleOwner, {
            binding.rvDiscussionRoom.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = DiscussionRoomAdapter(requireContext(), it)
            }
        })

        model.isLoading.observe(viewLifecycleOwner, { populateLoading(it) })

    }

    private fun populateLoading(it: Boolean) {
        if (it) skeleton.showSkeleton()
        else skeleton.showOriginal()
    }

    companion object {

        private const val POS_ARG = "post_arg"

        @JvmStatic
        fun newInstance(position: Int) = ContentDiscussionFragment().apply {
            arguments = Bundle().apply { putInt(POS_ARG, position) }
        }
    }
}