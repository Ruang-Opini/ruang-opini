package id.ruangopini.utils

import android.annotation.SuppressLint
import android.app.Activity
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import id.ruangopini.R

object DialogHelpers {


    /**
     * this variable pDialog using for showLoadingDialog and hideLoadingDialog
     */
    @SuppressLint("StaticFieldLeak")
    private lateinit var pDialog: SweetAlertDialog

    /**
     * this method to show sweet alert loading dialog, need some param like
     * @param activity is using for instance the dialog
     * @param message to show message that will appear in dialog, have default value is "Loading"
     * for detail about library can visit here
     * https://github.com/pedant/sweet-alert-dialog
     */
    fun showLoadingDialog(activity: Activity, message: String = "Loading") {
        pDialog = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE).apply {
            progressHelper.barColor = ContextCompat.getColor(context, R.color.primary)
            titleText = message
            setCancelable(false)
            show()
        }
    }

    /**
     * this method to hide dialog
     */
    fun hideLoadingDialog() = pDialog.cancel()
}