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
    var join: Int? = 0,
    var comment: Int? = 0,
    var post: Int? = 0,
    @DocumentId
    val discussionId: String? = null
)

data class PostAnalytics(
    var voteUp: Int? = 0,
    var voteDown: Int? = 0,
    var comment: Int? = 0,
    @DocumentId
    val postId: String? = null,
)

data class Analytics(
    val createdAt: Timestamp? = Helpers.getTodayTime(),
    @DocumentId
    val time: String? = null
)