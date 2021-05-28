package id.ruangopini.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PolicyCategory(
    val name: String,
    val url: String
) : Parcelable