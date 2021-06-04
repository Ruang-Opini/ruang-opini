package id.ruangopini.data.repo.remote.firebase.storage

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

object StorageUserHelpers {

    private val instance = Firebase.storage

    fun getImgUrl(path: String, onResult: (Uri) -> Unit) {
        instance.reference
            .child(path)
            .downloadUrl
            .addOnSuccessListener { onResult(it) }
    }
}