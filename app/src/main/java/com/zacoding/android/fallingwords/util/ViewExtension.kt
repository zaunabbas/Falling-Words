package com.zacoding.android.fallingwords.util

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.zacoding.android.fallingwords.R

fun Activity.makeStatusBarTransparent() {
    window.apply {

        //WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightNavigationBars = true
        windowInsetsController.isAppearanceLightStatusBars = true
        statusBarColor = ContextCompat.getColor(this.context, R.color.colorWhite)
    }
}

fun View.setMarginTop(marginTop: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, marginTop, 0, 0)
    this.layoutParams = menuLayoutParams
}

fun Activity.showErrorMessageInDialog(
    heading: String? = "Error",
    errorMessage: String
) {
    this.let { callingContext ->

        AlertDialog.Builder(callingContext)
            .setTitle(heading)
            .setMessage(errorMessage)
            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(
                callingContext.getString(android.R.string.ok), null
            )
            .show()
    }
}

fun Activity.showToast(
    toastMessage: String
) {
    this.let { callingContext ->

        Toast.makeText(callingContext, toastMessage, Toast.LENGTH_LONG).show()
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}



