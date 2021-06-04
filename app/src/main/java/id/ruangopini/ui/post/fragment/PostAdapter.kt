package id.ruangopini.ui.post.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.R
import id.ruangopini.data.model.Post
import id.ruangopini.data.repo.remote.firebase.firestore.user.FirestoreUserHelpers
import id.ruangopini.data.repo.remote.firebase.storage.StorageUserHelpers
import id.ruangopini.databinding.ItemPostBinding
import id.ruangopini.ui.policy.detail.adapter.PolicyDocumentAdapter
import id.ruangopini.ui.post.detail.DetailPostActivity
import id.ruangopini.utils.DateFormat
import id.ruangopini.utils.Helpers.formatDate
import id.ruangopini.utils.Helpers.hideView
import id.ruangopini.utils.Helpers.isDarkMode
import id.ruangopini.utils.Helpers.showToast
import id.ruangopini.utils.STORAGE

class PostAdapter(
    private val context: Context,
    private val listPost: List<Post>,
    private val voteListener: VoteListener
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemPostBinding
    private var isTextExpanded: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = listPost[position]
        with(binding) {
            val userId = Firebase.auth.currentUser?.uid ?: ""

            val isVoteUp = post.peopleVoteUp?.find { it == userId }?.isNotEmpty() ?: false
            val isVoteDown = post.peopleVoteDown?.find { it == userId }?.isNotEmpty() ?: false

            btnVoteUp.apply {
                isChecked = isVoteUp
                buttonTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, getColorCheckButton(isVoteUp))
                )
            }
            btnVoteDown.apply {
                isChecked = isVoteDown
                buttonTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, getColorCheckButton(isVoteDown))
                )
            }

            btnComment.setOnClickListener {
                context.startActivity(Intent(context, DetailPostActivity::class.java)
                    .apply { putExtra(DetailPostActivity.EXTRA_POST, post) })
            }

            contentPost.setOnClickListener {
                context.startActivity(Intent(context, DetailPostActivity::class.java)
                    .apply { putExtra(DetailPostActivity.EXTRA_POST, post) })
            }

            tvTime.text = post.createdAt?.formatDate(DateFormat.POST)
            tvVoteUp.text = post.voteUp.toString()
            tvVoteDown.text = post.voteDown.toString()
            tvComment.text = post.comment.toString()

            FirestoreUserHelpers.getUserById(post.userId ?: "") {
                tvUsername.text = it?.username
                it?.photoUrl.let { photo ->
                    if (photo != null) {
                        if (photo.subSequence(0, 3) != "AVA") ivAvatar.load(photo.toUri()) {
                            crossfade(true)
                            transformations(CircleCropTransformation())
                        }
                        else StorageUserHelpers.getImgUrl(
                            STORAGE.ROOT_AVA.plus(it?.userId).plus("/").plus(photo)
                        ) { uri ->
                            ivAvatar.load(uri) {
                                crossfade(true)
                                transformations(CircleCropTransformation())
                            }
                        }
                    } else ivAvatar.apply {
                        load(R.drawable.ic_person) {
                            crossfade(true)
                            imageTintList =
                                ContextCompat.getColorStateList(context, R.color.primary)
                            transformations(CircleCropTransformation())
                        }
                    }
                }
            }

            tvContentText.apply {
                text = post.content
                setOnClickListener {
                    isTextExpanded = if (isTextExpanded) {
                        this.maxLines = 5
                        false
                    } else {
                        this.maxLines = Integer.MAX_VALUE
                        true
                    }
                }
            }

            post.photos.let { photos ->
                val listPhoto = mutableListOf<String>()
                if (photos != null) photos.forEachIndexed { index, s ->
                    StorageUserHelpers.getImgUrl(
                        STORAGE.ROOT_POST.plus(post.postId).plus("/").plus(s)
                    ) { uri ->
                        listPhoto.add(uri.toString())
                        if (index == photos.size - 1) rvPhotoPost.apply {
                            itemAnimator = DefaultItemAnimator()
                            adapter = ImagePostAdapter(context, listPhoto)
                        }
                    }
                } else rvPhotoPost.hideView()
            }

            post.reference.let {
                if (it != null) {
                    tvRefName.text = it.policyNum.plus("-").plus(it.name)
                    rvPolicyDocs.apply {
                        itemAnimator = DefaultItemAnimator()
                        adapter = it.documents?.documents?.let { docs ->
                            PolicyDocumentAdapter(context, docs)
                        }
                    }
                } else constraintLayout.hideView()
            }

            btnVoteUp.apply {
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        btnVoteDown.isChecked = !isChecked
                        voteListener.onVoteUp(1, post.postId ?: "")
                        showToast(context, "Kamu mendukung post ini")
                    } else voteListener.onVoteUp(-1, post.postId ?: "")

                    buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, getColorCheckButton(isChecked))
                    )
                }
            }
            btnVoteDown.apply {
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        btnVoteUp.isChecked = !isChecked
                        voteListener.onVoteDown(1, post.postId ?: "")
                        showToast(context, "Kamu tidak mendukung post ini")
                    } else voteListener.onVoteDown(-1, post.postId ?: "")

                    buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, getColorCheckButton(isChecked))
                    )
                }
            }
        }

    }

    private fun getColorCheckButton(checked: Boolean): Int {
        return if (checked) R.color.primary else {
            if ((context as Activity).isDarkMode()) R.color.white else R.color.black
        }
    }

    override fun getItemCount(): Int = listPost.size
}

interface VoteListener {
    fun onVoteUp(vote: Int, postId: String)
    fun onVoteDown(vote: Int, postId: String)
}