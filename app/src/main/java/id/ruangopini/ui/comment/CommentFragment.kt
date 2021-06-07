package id.ruangopini.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import id.ruangopini.databinding.FragmentCommentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel


@ExperimentalCoroutinesApi
class CommentFragment : Fragment() {

    private lateinit var binding: FragmentCommentBinding
    private val model: CommentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.getCommentByUserId()
        model.comments.observe(viewLifecycleOwner) {
            binding.rvCommentPage.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = CommentPageAdapter(requireContext(), it)
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = CommentFragment()
    }
}