package id.ruangopini.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Buzzer(
    val buzzer: Int,
    val nonBuzzer: Int
) : Parcelable
