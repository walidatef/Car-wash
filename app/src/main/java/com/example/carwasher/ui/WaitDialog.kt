package com.example.carwasher.ui

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.example.carwasher.R

class WaitDialog(private val activity: Activity) {
    private lateinit var dialog: AlertDialog
    private val builder = AlertDialog.Builder(activity)
    private val inflater = activity.layoutInflater

    fun startWaitingDialog() {
        builder.setView(inflater.inflate(R.layout.wait_dialog, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun showSuccessLayout() {
        dialog.findViewById<LinearLayout>(R.id.waitLayout).visibility = View.GONE
        val successLayout = dialog.findViewById<LinearLayout>(R.id.successSendOrderLayout)
        successLayout.visibility = View.VISIBLE
        dialog.setCancelable(true)
        successLayout.findViewById<Button>(R.id.ok_btn).setOnClickListener {
            dialog.dismiss()
        }
    }
}