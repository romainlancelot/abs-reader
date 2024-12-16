package com.absreader.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialog {
    companion object {
        fun alert(context: Context, message: String) {
            val dialog: AlertDialog = MaterialAlertDialogBuilder(context)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .create()
            dialog.show()
        }
    }
}