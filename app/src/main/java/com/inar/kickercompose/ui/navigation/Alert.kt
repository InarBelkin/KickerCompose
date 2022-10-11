package com.inar.kickercompose.ui.navigation

import android.app.AlertDialog
import android.content.Context
import java.lang.Exception

fun showAlert(message: String, context: Context) {    //TODO: Make alert
    AlertDialog.Builder(context)
        .setTitle("Error")
        .setMessage(message)
        .setNegativeButton("No", null)
        .show();
}

fun showAlert(exception: Exception, context: Context) {
    AlertDialog.Builder(context)
        .setTitle("Error")
        .setMessage(exception.message)
        .setNegativeButton("No", null)
        .show();
}