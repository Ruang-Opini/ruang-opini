package id.ruangopini.data.repo.remote.firebase.firestore.user

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.User
import id.ruangopini.utils.COLLECTION

object FirestoreUserHelpers {

    private val instance = Firebase.firestore.collection(COLLECTION.USER)
    private const val TAG = "FirestoreUserHelpers"

    fun getUserById(userId: String, isLiveData: Boolean? = false, onResult: (User?) -> Unit) {
        val snapshot = instance.document(userId)
        if (isLiveData == true) {
            snapshot.addSnapshotListener { value, error ->
                if (error != null) Log.d(TAG, "getUserById: error = ${error.message}")
                val data = if (value != null && value.exists()) value.toObject(User::class.java)
                else User()
                onResult(data)
            }
        } else snapshot.get().addOnCompleteListener {
            val data = it.result?.toObject(User::class.java) ?: User()
            onResult(data)
        }.addOnFailureListener { Log.d(TAG, "getUserById: failed = ${it.message}") }
    }
}