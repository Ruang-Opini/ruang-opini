package id.ruangopini.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PolicyCategory(
    val name: String,
    val url: String
) : Parcelable