package org.inu.events.ui.util.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import org.inu.events.R

class LoginDialog {
    interface LoginDialog {
        fun onOk()
        fun onCancel()
    }

    fun show(context: Context, onOk: () -> Unit, onCancel: () -> Unit, title:String = "로그인 하시겠습니까?",) {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customLayout = inflater.inflate(R.layout.login_dialog, null)
        val build = AlertDialog.Builder(context).apply {
            setView(customLayout)
        }
        val dialog = build.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.show()

        customLayout.findViewById<TextView>(R.id.title).text = title

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