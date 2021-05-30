package id.ruangopini.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    val bio: String? = null,
    val photoUrl: String? = null,
    val headerUrl: String? = null,
    val postAmount: Int? = null,
    val commentAmount: Int? = null,
    val joinedIn: Timestamp? = Timestamp.now(),
    @DocumentId
    val userId: String? = null
) : Parcelable
