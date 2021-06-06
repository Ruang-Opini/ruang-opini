package id.ruangopini.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.faltenreich.skeletonlayout.Skeleton
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.github.marlonlom.utilities.timeago.TimeAgoMessages
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import id.ruangopini.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

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

    fun View.hideView(isInvisible: Boolean? = false) {
        this.visibility = if (isInvisible == true) View.INVISIBLE else View.GONE
    }

    fun View.showView() {
        this.visibility = View.VISIBLE
    }

    @ColorInt
    fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }

    fun Skeleton.initSkeleton(context: Context) {
        this.apply {
            showShimmer = true
            shimmerColor = context.getColorFromAttr(R.attr.colorPrimaryVariant)
            maskColor = ContextCompat.getColor(context, R.color.outline_20)
            maskCornerRadius = 52f
        }
    }

    fun String.getUrlPath(): String {
        return this.subSequence("https://peraturan.go.id/".length, this.length).toString()
    }

    fun String.getPdfName(): String {
        return this.split("/").last()
    }

    fun String.toTitleCase(): String {
        return buildString {
            this@toTitleCase.split(" ").forEach {
                append(it.lowercase().replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
                }).append(" ")
            }
        }.trim()
    }

    fun Timestamp.formatDate(format: String): String? {
        return SimpleDateFormat(format, Locale.getDefault()).format(this.toDate().time)
    }

    fun String.toTimeStamp(format: String): Timestamp {
        return Timestamp(SimpleDateFormat(format, Locale.getDefault()).parse(this))
    }

    fun Int.reSize(percentage: Int) = (this.toFloat() * (percentage.toFloat().div(100.0))).toInt()

    fun TextView.addImage(atText: String, @DrawableRes imgSrc: Int) {
        val imgHeight = resources.getDimensionPixelOffset(R.dimen.dp_20)
        val imgWidth = resources.getDimensionPixelOffset(
            if (atText == "[icon-join]") R.dimen.dp_60 else R.dimen.dp_20
        )
        val ssb = SpannableStringBuilder(this.text)

        val drawable = ContextCompat.getDrawable(this.context, imgSrc) ?: return
        drawable.mutate()
        drawable.setBounds(0, 0, imgWidth, imgHeight)
        val start = text.indexOf(atText)
        ssb.setSpan(
            VerticalImageSpan(drawable),
            start,
            start + atText.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        this.setText(ssb, TextView.BufferType.SPANNABLE)
    }

    fun Timestamp.toTimeAgo(): String {
        val messages: TimeAgoMessages = TimeAgoMessages.Builder().withLocale(Locale("id")).build()
        return TimeAgo.using(this.toDate().time, messages)
    }

}