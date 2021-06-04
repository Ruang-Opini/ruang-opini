package id.ruangopini.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Respond(
    val negative: Int,
    val positive: Int
) : Parcelable