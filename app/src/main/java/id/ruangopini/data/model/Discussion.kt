package id.ruangopini.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Discussion(
    val name: String? = null,
    val desc: String? = null,
    val issueName: String? = null,
    val issueId: String? = null,
    val people: Int? = null,
    val post: Int? = null,
    val createdAt: Timestamp? = null,
    @DocumentId
    val discussionId: String? = null,
)
