package id.ruangopini.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trend(
    var name: String,
    var respond: Respond,
    var buzzer: Buzzer
) : Parcelable
