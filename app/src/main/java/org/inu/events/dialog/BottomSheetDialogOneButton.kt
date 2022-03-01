package org.inu.events.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import org.inu.events.R

class BottomSheetDialogOneButton(val context: Context, private val titleText: String) {

    fun show(firstText:String, onOne: () -> Unit, onCancel: () -> Unit) {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customLayout = inflater.inflate(R.layout.bottom_sheet_dialog_one, null)
        val build = AlertDialog.Builder(context).apply {
            setView(customLayout)
        }

        val dialog = build.create()
        with(dialog) {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            with(window!!) {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(Gravity.BOTTOM)
            }
            show()
        }

        customLayout.findViewById<TextView>(R.id.title).text = titleText

        val btnOne = customLayout.findViewById<AppCompatButton>(R.id.oneButton).apply {
            text = firstText
        }

        btnOne.setOnClickListener {
            onOne()
            dialog.dismiss()
        }
        val btnCancel = customLayout.findViewById<AppCompatButton>(R.id.cancelButton)
        btnCancel.setOnClickListener {
            onCancel()
            dialog.dismiss()
        }

        dialog.show()
    }
}