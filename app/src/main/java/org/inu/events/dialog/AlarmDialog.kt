package org.inu.events.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import org.inu.events.DetailActivity
import org.inu.events.R

class AlarmDialog {

    fun showDialog(context: Context,title:String, content: String) {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customLayout = inflater.inflate(R.layout.alarm_dialog, null)
        val build = AlertDialog.Builder(context).apply {
            setView(customLayout)
        }
        val dialog = build.create()
        val inset = InsetDrawable(ColorDrawable(Color.TRANSPARENT), 20)
        with(dialog) {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            with(window!!) {
                setBackgroundDrawableResource(R.drawable.drawable_alarm_dialog_background)
                setBackgroundDrawable(inset)

            }
            show()
        }
        customLayout.findViewById<TextView>(R.id.alarm_dialog_title).text = title
        customLayout.findViewById<TextView>(R.id.alarm_dialog_content).text = content
        customLayout.findViewById<AppCompatButton>(R.id.alarm_dialog_button).setOnClickListener {
            dialog.dismiss()
        }
    }
}