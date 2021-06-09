package id.ruangopini.ui.post.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import id.ruangopini.databinding.FragmentPostBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class PostFragment : Fragment(), VoteListener {

    private lateinit var binding: FragmentPostBinding
    private val model: PostViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<GetPost>(POST_ARG)?.let { get ->
            val id = arguments?.getString(ID_ARG, "") ?: ""
            when (get) {
                GetPost.USER_ID -> model.getPostByUserId(id)
                GetPost.DISCUSSION_POPULAR -> model.getPopularPostInDiscussion(id)
                GetPost.DISCUSSION_ID_NEW -> model.getPostByDiscussionLatest(id)
            }
        }
        model.post.observe(viewLifecycleOwner, {
            binding.rvPost.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = PostAdapter(requireContext(), it, this@PostFragment)
            }
        })
    }

    companion object {
        @Parcelize
        enum class GetPost : Parcelable {
            USER_ID, DISCUSSION_ID_NEW, DISCUSSION_POPULAR
        }

        private const val POST_ARG = "post_arg"
        private const val ID_ARG = "id_arg"

        @JvmStatic
        fun newInstance(getPost: GetPost, id: String) = PostFragment().apply {
            arguments = Bundle().apply {
                putParcelable(POST_ARG, getPost)
                putString(ID_ARG, id)
            }
        }
    }

    override fun onVoteUp(vote: Int, postId: String) {
        model.updateVoteUp(vote, postId)
    }

    override fun onVoteDown(vote: Int, postId: String) {
        model.updateVoteDown(vote, postId)
    }
}