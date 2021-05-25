package id.ruangopini.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.core.net.toUri
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

object Helpers {
    fun Activity.isDarkMode(): Boolean =
        this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    fun TextInputLayout.showError(message: String? = "Tidak boleh kosong") {
        this.apply {
            error = message
            isErrorEnabled = true
        }
    }

    fun EditText.getPlainText() = this.text.toString()

    fun validateError(vararg inputLayouts: TextInputLayout) {
        for (layout in inputLayouts) layout.isErrorEnabled = false
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @FlowPreview
    suspend fun EditText.afterTextChanged(afterTextChanged: suspend (String) -> Unit) {
        val watcher = object : TextWatcher {

            private val channel = BroadcastChannel<String>(Channel.CONFLATED)

            override fun afterTextChanged(editable: Editable?) {
                channel.trySend(editable.toString()).isSuccess
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            fun asFlow(): Flow<String> = channel.asFlow()
        }

        this.addTextChangedListener(watcher)

        watcher.asFlow()
            .debounce(500)
            .distinctUntilChanged()
            .collect { afterTextChanged(it) }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun ActivityResult.handleImagePicker(
        context: Context,
        onResult: (Uri) -> Unit
    ) {
        when (this.resultCode) {
            Activity.RESULT_OK -> onResult(this.data?.data ?: "".toUri())
            ImagePicker.RESULT_ERROR -> showToast(context, ImagePicker.getError(this.data))
            else -> showToast(context, "Batal")
        }
    }

}