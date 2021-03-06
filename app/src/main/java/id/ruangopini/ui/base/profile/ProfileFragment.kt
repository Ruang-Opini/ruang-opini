package id.ruangopini.ui.base.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.R
import id.ruangopini.databinding.FragmentProfileBinding
import id.ruangopini.ui.comment.CommentFragment
import id.ruangopini.ui.editprofile.EditProfileActivity
import id.ruangopini.ui.post.fragment.PostFragment
import id.ruangopini.ui.settings.SettingsActivity
import id.ruangopini.utils.DateFormat
import id.ruangopini.utils.Helpers
import id.ruangopini.utils.Helpers.formatDate
import id.ruangopini.utils.Helpers.getColorFromAttr
import id.ruangopini.utils.Helpers.hideView
import id.ruangopini.utils.Helpers.showView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val model: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.getUserData()
        with(binding) {
            viewPager.adapter = SectionPagerAdapter(requireActivity())
            TabLayoutMediator(tabs, viewPager) { tabs, position ->
                tabs.text = listOf("Post", "Comment")[position]
            }.attach()
        }

        with(binding.layoutUser) {
            model.user.observe(viewLifecycleOwner, {
                tvName.text = it.name
                tvUsername.text = it.username
                it.bio.let { bio ->
                    if (bio != null) tvBio.apply {
                        text = bio
                        showView()
                    } else tvBio.hideView()
                }
                tvJoinDate.text =
                    "Bergabung pada ".plus(it.joinedIn?.formatDate(DateFormat.PROFILE))
                it.name?.let { name -> setupToolbar(name) }

                btnEditProfile.setOnClickListener { _ ->
                    startActivity(Intent(requireContext(), EditProfileActivity::class.java)
                        .apply { putExtra(EditProfileActivity.EXTRA_USER, it) })
                }
            })
            model.photoUrl.observe(viewLifecycleOwner, {
                Log.d("TAG", "photoUrl: $it")
                if (it != null) ivAva.load(it) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                    error(R.drawable.ic_person)
                    placeholder(R.drawable.ic_person)
                } else setDefaultPhoto()
            })

            model.headerUrl.observe(viewLifecycleOwner) {
                if (it != null) ivBanner.load(it) { crossfade(true) }
            }

        }
    }

    private fun setDefaultPhoto() {
        binding.layoutUser.ivAva.apply {
            load(R.drawable.ic_person) {
                crossfade(true)
                imageTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary)
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun setupToolbar(name: String) {
        var scrollRange = -1
        binding.appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
                if (scrollRange == -1) scrollRange = barLayout?.totalScrollRange!!
                (scrollRange.plus(verticalOffset) == 0).let {
                    binding.toolbar.apply {
                        binding.tvToolbarTitle.text = if (it) name else ""
                        setBackgroundColor(
                            if (it) context.getColorFromAttr(R.attr.colorOnPrimary)
                            else ContextCompat.getColor(
                                requireContext(),
                                android.R.color.transparent
                            )
                        )
                    }
                }
            })

        binding.toolbar.setOnMenuItemClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionSettings) Helpers.showToast(requireContext(), "Settings")
        return super.onOptionsItemSelected(item)
    }

    @ExperimentalCoroutinesApi
    private inner class SectionPagerAdapter(
        fa: FragmentActivity
    ) : FragmentStateAdapter(fa) {
        val currentUserId = Firebase.auth.currentUser?.uid ?: ""
        val fragments = listOf(
            PostFragment.newInstance(PostFragment.Companion.GetPost.USER_ID, currentUserId),
            CommentFragment.newInstance()
        )

        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment = fragments[position]

    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}