package id.ruangopini.utils

import android.app.Activity
import android.content.res.Configuration

object Helpers {
    fun Activity.isDarkMode(): Boolean =
        this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}