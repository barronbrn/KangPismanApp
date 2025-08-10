package com.example.kangpismanapp.util

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogUtils {
    fun showHelpDialog(context: Context, title: String, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Mengerti") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}