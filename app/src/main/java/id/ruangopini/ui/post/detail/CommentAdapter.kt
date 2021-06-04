package id.ruangopini.ui.post.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import id.ruangopini.R
import id.ruangopini.data.model.Comment
import id.ruangopini.data.repo.remote.firebase.firestore.user.FirestoreUserHelpers
import id.ruangopini.data.repo.remote.firebase.storage.StorageUserHelpers
import id.ruangopini.databinding.ItemCommentBinding
import id.ruangopini.utils.DateFormat
import id.ruangopini.utils.Helpers.formatDate
import id.ruangopini.utils.STORAGE

class CommentAdapter(
    private val listComment: List<Comment>
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemCommentBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            ivAvatar.load("https://avatars.githubusercontent.com/u/32610660?v=4") {
                crossfade(true)
                transformations(CircleCropTransformation())
            }


            tvTime.text = comment.createdAt?.formatDate(DateFormat.POST)
            tvComment.text = comment.comment
        }
    }

    override fun getItemCount(): Int = listComment.size
}