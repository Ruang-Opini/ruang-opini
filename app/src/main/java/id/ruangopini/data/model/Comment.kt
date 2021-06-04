package id.ruangopini.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Comment(
    val comment: String? = null,
    val userId: String? = null,
    val postId: String? = null,
    val createdAt: Timestamp? = Timestamp.now(),
    @DocumentId
    val commentId: String? = null
)