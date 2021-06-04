package id.ruangopini.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Discussion(
    val name: String? = null,
    val desc: String? = null,
    val category: List<String>? = null,
    var issueName: String? = null,
    var issueId: String? = null,
    val members: List<String>? = null,
    val people: Int? = 0,
    val post: Int? = 0,
    val createdAt: Timestamp? = Timestamp.now(),
    @DocumentId
    val discussionId: String? = null,
) : Parcelable
