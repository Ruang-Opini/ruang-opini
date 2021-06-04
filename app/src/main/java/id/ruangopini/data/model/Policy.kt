package id.ruangopini.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Policy(
    val name: String,
    val policyNum: String,
    var url: String, // for category
    var documents: PolicyDocument,
    val type: String, // for category
    val year: String // for category
) : Parcelable

@Parcelize
data class PolicyDocument(
    val documents: List<String>
) : Parcelable


@Parcelize
data class PolicyFirebase(
    val name: String? = null,
    val policyNum: String? = null,
    var url: String? = null,
    var documents: PolicyDocumentFirebase? = null,
    val type: String? = null, // for category
    val year: String? = null // for category
) : Parcelable

@Parcelize
data class PolicyDocumentFirebase(
    val documents: List<String>? = null
) : Parcelable


fun Policy.forFirebase(): PolicyFirebase =
    PolicyFirebase(
        this.name, this.policyNum, this.url,
        PolicyDocumentFirebase(this.documents.documents), this.type, this.year
    )

fun PolicyFirebase.normalize(): Policy =
    Policy(
        this.name ?: "", this.policyNum ?: "", this.url ?: "",
        PolicyDocument(this.documents?.documents ?: listOf()),
        this.type ?: "", this.year ?: ""
    )
