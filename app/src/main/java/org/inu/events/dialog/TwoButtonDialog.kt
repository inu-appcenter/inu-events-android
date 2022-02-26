package org.inu.events.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import org.inu.events.R

class TwoButtonDialog(val title: String, val onOk: () -> Unit) {
    fun show(context: Context) {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customLayout = inflater.inflate(R.layout.dialog_two_buttons, null)
        val build = AlertDialog.Builder(context).apply {
            setView(customLayout)
        }
        val dialog = build.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.show()

        val buttonTitle = customLayout.findViewById<TextView>(R.id.title)
        buttonTitle.text = title

        val btnOk = customLayout.findViewById<Button>(R.id.ok)
        btnOk.setOnClickListener {
            onOk()
            dialog.dismiss()
        }
        val btnCancel = customLayout.findViewById<Button>(R.id.cancel)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}