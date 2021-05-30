package id.ruangopini.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Intro(
    val image: Int,
    val title: String,
    val subTitle: String,
) : Parcelable
