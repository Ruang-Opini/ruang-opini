package id.ruangopini.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.ruangopini.R
import id.ruangopini.databinding.DialogForgotPasswordBinding
import id.ruangopini.utils.Helpers.afterTextChanged
import id.ruangopini.utils.Helpers.getPlainText
import id.ruangopini.utils.Helpers.showError
import id.ruangopini.utils.Helpers.validateError
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    fun createBottomSheetDialog(
        context: Context,
        layout: Int,
        onViewCreated: (view: View, dialog: BottomSheetDialog) -> Unit
    ) {
        val view = LayoutInflater.from(context).inflate(layout, null)
        val dialog = BottomSheetDialog(context, R.style.CustomBottomSheetDialogTheme)
        dialog.setContentView(view)
        BottomSheetBehavior.from(view.parent as View)
            .apply {
                peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
                expandedOffset = 56
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        onViewCreated(view, dialog)
    }

    fun showForgotPassword(
        dialogForgotPasswordBinding: DialogForgotPasswordBinding,
        dialog: BottomSheetDialog,
        onClick: (email: String) -> Unit
    ) {
        with(dialogForgotPasswordBinding) {
            btnClose.setOnClickListener { dialog.dismiss() }

            GlobalScope.launch {
                edtEmail.afterTextChanged { validateError(tilEmail) }
            }

            btnSave.setOnClickListener {
                if (!edtEmail.text.isNullOrEmpty()) {
                    onClick(edtEmail.getPlainText())
                } else tilEmail.showError()
            }
        }
        dialog.show()
    }
}