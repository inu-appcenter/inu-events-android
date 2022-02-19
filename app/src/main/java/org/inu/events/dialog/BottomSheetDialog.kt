package org.inu.events.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import org.inu.events.R

class BottomSheetDialog {

    fun show(context: Context, onFirst: () -> Unit, onSecond: () -> Unit, onCancel: () -> Unit) {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customLayout = inflater.inflate(R.layout.bottom_sheet_dialog, null)
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

        val btnFirst = customLayout.findViewById<AppCompatButton>(R.id.firstButton)
        btnFirst.setOnClickListener {
            onFirst()
            dialog.dismiss()
        }
        val btnSecond = customLayout.findViewById<AppCompatButton>(R.id.secondButton)
        btnSecond.setOnClickListener {
            onSecond()
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