package id.ruangopini.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String? = null,
    val username: String? = null,
    val email: String? = null,
    var bio: String? = null,
    var photoUrl: String? = null,
    var headerUrl: String? = null,
    val postAmount: Int? = null,
    val commentAmount: Int? = null,
    val joinedIn: Timestamp? = Timestamp.now(),
    var updatedAt: Timestamp? = Timestamp.now(),
    @DocumentId
    val userId: String? = null
) : Parcelable
