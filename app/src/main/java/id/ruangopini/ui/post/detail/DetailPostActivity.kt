package id.ruangopini.ui.post.detail

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.R
import id.ruangopini.data.model.Comment
import id.ruangopini.data.model.Post
import id.ruangopini.data.repo.remote.firebase.firestore.user.FirestoreUserHelpers
import id.ruangopini.data.repo.remote.firebase.storage.StorageUserHelpers
import id.ruangopini.databinding.ActivityDetailPostBinding
import id.ruangopini.ui.policy.detail.adapter.PolicyDocumentAdapter
import id.ruangopini.ui.post.fragment.ImagePostAdapter
import id.ruangopini.utils.Helpers
import id.ruangopini.utils.Helpers.afterTextChanged
import id.ruangopini.utils.Helpers.getPlainText
import id.ruangopini.utils.Helpers.hideView
import id.ruangopini.utils.Helpers.isDarkMode
import id.ruangopini.utils.STORAGE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class DetailPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPostBinding
    private val model: DetailPostViewModel by viewModel()
    private val userId = Firebase.auth.currentUser?.uid ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent.extras?.getParcelable<Post>(EXTRA_POST)?.let {
            model.getPostById(it.postId ?: "")
            model.getComments(it.postId ?: "")
            populateData(it)
        }

        model.post.observe(this, { post ->
            with(binding) {
                tvVoteUp.text = post.voteUp.toString()
                tvVoteDown.text = post.voteDown.toString()
                tvComment.text = post.comment.toString()
            }
        })

        model.comment.observe(this, {
            binding.rvComment.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = CommentAdapter(it)
            }
        })

        lifecycleScope.launch {
            binding.layoutCommentBar.edtComment.afterTextChanged {
                binding.layoutCommentBar.btnSend.apply {
                    isEnabled = it.isNotEmpty()
                    chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            applicationContext,
                            if (it.isNotEmpty()) R.color.primary else R.color.primary_20
                        )
                    )
                }
            }
        }
    }

    private fun populateData(post: Post) = with(binding) {
        tvContentText.text = post.content

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

        FirestoreUserHelpers.getUserById(post.userId ?: "") {
            tvUsername.text = it?.username
            populatePhoto(it?.photoUrl, ivAvatarPost, it?.userId ?: "")
        }

        FirestoreUserHelpers.getUserById(userId) {
            populatePhoto(it?.photoUrl, layoutCommentBar.ivAvatar, userId)
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
                    model.updateVoteUp(1, post.postId ?: "")
                    Helpers.showToast(context, "Kamu mendukung post ini")
                } else model.updateVoteUp(-1, post.postId ?: "")

                buttonTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, getColorCheckButton(isChecked))
                )
            }
        }
        btnVoteDown.apply {
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    btnVoteUp.isChecked = !isChecked
                    model.updateVoteDown(1, post.postId ?: "")
                    Helpers.showToast(context, "Kamu tidak mendukung post ini")
                } else model.updateVoteDown(-1, post.postId ?: "")

                buttonTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, getColorCheckButton(isChecked))
                )
            }
        }

        with(layoutCommentBar) {
            btnSend.setOnClickListener {
                val comment = Comment(edtComment.getPlainText(), userId, post.postId)
                model.postComment(comment)
                edtComment.setText("")
                btnSend.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(applicationContext, R.color.primary_20)
                )
            }
        }
    }

    private fun populatePhoto(photo: String?, imageView: ImageView, userId: String) {
        if (photo != null) {
            if (photo.subSequence(0, 3) != "AVA") imageView.load(photo.toUri()) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
            else StorageUserHelpers.getImgUrl(
                STORAGE.ROOT_AVA.plus(userId).plus("/").plus(photo)
            ) { uri ->
                imageView.load(uri) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        } else imageView.apply {
            load(R.drawable.ic_person) {
                crossfade(true)
                imageTintList =
                    ContextCompat.getColorStateList(context, R.color.primary)
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun getColorCheckButton(checked: Boolean): Int {
        return if (checked) R.color.primary else {
            if (this.isDarkMode()) R.color.white else R.color.black
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_POST = "extra_post"
    }
}