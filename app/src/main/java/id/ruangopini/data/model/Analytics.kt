package id.ruangopini.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import id.ruangopini.utils.Helpers

data class CategoryAnalytics(
    val name: String? = null,
    var discussion: Int? = 0,
    var join: Int? = 0,
)

data class DiscussionAnalytics(
    val join: Int? = 0,
    val comment: Int? = 0,
    val post: Int? = 0,
    @DocumentId
    val discussionId: String? = null
)

data class PostAnalytics(
    val voteUp: Int? = 0,
    val voteDown: Int? = 0,
    val comment: Int? = 0,
    @DocumentId
    val postId: String? = null,
)

data class Analytics(
    val createdAt: Timestamp? = Helpers.getTodayTime(),
    @DocumentId
    val time: String? = null
)