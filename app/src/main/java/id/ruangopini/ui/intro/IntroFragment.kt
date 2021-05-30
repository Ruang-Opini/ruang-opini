package id.ruangopini.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import id.ruangopini.data.model.Intro
import id.ruangopini.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Intro>(INTRO_ARG)?.let {
            with(binding) {
                ivIntro.load(it.image) { crossfade(true) }
                tvIntroTitle.text = it.title
                tvIntroSubtitle.text = it.subTitle
            }
        }
    }

    companion object {

        private const val INTRO_ARG = "intro_arg"

        @JvmStatic
        fun newInstance(intro: Intro) =
            IntroFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(INTRO_ARG, intro)
                }
            }
    }
}