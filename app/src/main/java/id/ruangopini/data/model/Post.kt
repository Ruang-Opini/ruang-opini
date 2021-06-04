package id.ruangopini.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val content: String? = null,
    var photos: List<String>? = null,
    val reference: PolicyFirebase? = null,
    var userId: String? = null,
    var discussionId: String? = null,
    val createdAt: Timestamp? = Timestamp.now(),
    val voteUp: Int? = 0,
    val voteDown: Int? = 0,
    val comment: Int? = 0,
    val peopleVoteUp: List<String>? = null,
    val peopleVoteDown: List<String>? = null,
    @DocumentId
    val postId: String? = null,
) : Parcelable
