package id.ruangopini.data.model

import com.google.firebase.firestore.DocumentId

data class Issue(
    val name: String? = null,
    @DocumentId
    val id: String? = null
)
