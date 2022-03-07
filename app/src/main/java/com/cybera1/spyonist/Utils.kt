package com.cybera1.spyonist

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.view.View
import android.widget.ImageView
import java.text.SimpleDateFormat
import java.util.*

fun Activity.goToActivity(newActivity: Class<*>?) {
    this.startActivity(Intent(this, newActivity))
}

fun Activity.openBrowser(URL: String) {
    this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL)))
}

fun Activity.openSharingScreen(text: String) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "plain/text"
    }
    this.startActivity(intent)
}

fun View.updateSize(size: Int){
    requestLayout()
    layoutParams.height = size
    layoutParams.width = size
}

fun ImageView.setViewTint(hexColor: String) {
    setColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.SRC_IN)
}

fun View.updateOpacity(opacity: Float) {
    requestLayout()
    alpha = opacity
}

fun Long.toTime(): String {
    val cal = Calendar.getInstance()
    cal.timeInMillis = this
    val formatter = SimpleDateFormat("hh:mm a")
    return formatter.format(cal.time).toString().toUpperCase()
}

fun Long.toDate(): String {
    val cal = Calendar.getInstance()
    cal.timeInMillis = this
    val formatter = SimpleDateFormat("dd MMM yyyy")
    return formatter.format(cal.time).toString()
}