package id.ruangopini.ui.comment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ruangopini.R
import id.ruangopini.data.model.Comment
import id.ruangopini.data.model.Post
import id.ruangopini.data.repo.remote.firebase.firestore.user.FirestoreUserHelpers
import id.ruangopini.data.repo.remote.firebase.storage.StorageUserHelpers
import id.ruangopini.databinding.ItemCommentPageBinding
import id.ruangopini.ui.post.detail.DetailPostActivity
import id.ruangopini.utils.COLLECTION
import id.ruangopini.utils.DateFormat
import id.ruangopini.utils.Helpers.formatDate
import id.ruangopini.utils.Helpers.toTimeAgo
import id.ruangopini.utils.STORAGE

class CommentPageAdapter(
    private val context: Context,
    private val listComment: List<Comment>
) : RecyclerView.Adapter<CommentPageAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemCommentPageBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemCommentPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = listComment[position]
        with(binding) {

            FirestoreUserHelpers.getUserById(comment.userId ?: "") {
                tvUsername.text = it?.username
                it?.photoUrl.let { photo ->
                    if (photo != null) {
                        if (photo.subSequence(0, 3) != "AVA") ivAvatar.load(photo.toUri()) {
                            crossfade(true)
                            transformations(CircleCropTransformation())
                        }
                        else StorageUserHelpers.getImgUrl(
                            STORAGE.ROOT_AVA.plus(comment.userId ?: "").plus("/").plus(photo)
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
            tvTime.text = comment.createdAt?.toTimeAgo()
            tvComment.text = comment.comment

            Firebase.firestore.collection(COLLECTION.POST).document(comment.postId ?: "")
                .get().addOnCompleteListener { task ->
                    task.result?.toObject(Post::class.java)?.let { post ->
                        tvContentText.text = post.content
                        tvTimePost.text = post.createdAt?.formatDate(DateFormat.POST)

                        FirestoreUserHelpers.getUserById(post.userId ?: "") {
                            tvUsernamePost.text = it?.username
                            it?.photoUrl.let { photo ->
                                if (photo != null) {
                                    if (photo.subSequence(0, 3) != "AVA") ivAvatar
                                        .load(photo.toUri()) {
                                            crossfade(true)
                                            transformations(CircleCropTransformation())
                                        }
                                    else StorageUserHelpers.getImgUrl(
                                        STORAGE.ROOT_AVA.plus(it?.userId).plus("/").plus(photo)
                                    ) { uri ->
                                        ivAvatarPost.load(uri) {
                                            crossfade(true)
                                            transformations(CircleCropTransformation())
                                        }
                                    }
                                } else ivAvatarPost.apply {
                                    load(R.drawable.ic_person) {
                                        crossfade(true)
                                        imageTintList =
                                            ContextCompat.getColorStateList(
                                                context,
                                                R.color.primary
                                            )
                                        transformations(CircleCropTransformation())
                                    }
                                }
                            }
                        }
                        contentPost.setOnClickListener {
                            context.let {
                                it.startActivity(Intent(it, DetailPostActivity::class.java)
                                    .apply { putExtra(DetailPostActivity.EXTRA_POST, post) })
                            }
                        }
                    }
                }

        }
    }

    override fun getItemCount(): Int = listComment.size
}