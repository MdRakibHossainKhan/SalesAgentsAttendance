package com.rakib.sales_agents_attendance.utilities

import android.app.AlertDialog
import android.content.Context
import com.rakib.sales_agents_attendance.R

object SuccessAlertDialog {
    fun showAlertDialog(context: Context, alertTitle: String, alertMessage: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)

        alertDialogBuilder.setTitle(alertTitle)
        alertDialogBuilder.setMessage(alertMessage)
        alertDialogBuilder.setIcon(R.drawable.check)
        alertDialogBuilder.setPositiveButton("Okay") { _, _ ->
        }
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.show()
    }
}