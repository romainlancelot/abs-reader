package com.absreader.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.absreader.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialog {
    companion object {
        fun alert(context: Context, message: String) {
            val dialog: AlertDialog = MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.error_title))
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.ok_button), null)
                .create()
            dialog.show()
        }
    }
}
