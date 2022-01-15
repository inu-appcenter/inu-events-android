package org.inu.events

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton

class CommentDialog {
    interface LoginDialog {
        fun onOk()
        fun onCancel()
    }

    fun show(context: Context, onOk: () -> Unit, onCancel: () -> Unit) {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customLayout = inflater.inflate(R.layout.login_dialog, null)
        val build = AlertDialog.Builder(context).apply {
            setView(customLayout)
        }
        val dialog = build.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.show()

        val btnOk = customLayout.findViewById<AppCompatButton>(R.id.loginDialogOk)
        btnOk.setOnClickListener {
            onOk()
            dialog.dismiss()
        }
        val btnCancel = customLayout.findViewById<AppCompatButton>(R.id.loginDialogCancel)
        btnCancel.setOnClickListener {
            onCancel()
            dialog.dismiss()
        }
        dialog.show()
    }
}