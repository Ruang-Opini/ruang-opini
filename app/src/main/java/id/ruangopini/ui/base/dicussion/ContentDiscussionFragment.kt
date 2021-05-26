package id.ruangopini.ui.base.dicussion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import id.ruangopini.databinding.FragmentContentDiscussionBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ContentDiscussionFragment : Fragment() {

    private lateinit var binding: FragmentContentDiscussionBinding
    private val model: DiscussionViewModel by viewModels()

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

        arguments?.getInt(POS_ARG, 1).let {
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


    }

    companion object {

        private const val POS_ARG = "post_arg"

        @JvmStatic
        fun newInstance(position: Int) = ContentDiscussionFragment().apply {
            arguments = Bundle().apply { putInt(POS_ARG, position) }
        }
    }
}