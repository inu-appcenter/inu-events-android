package org.inu.events.lib.actionsheet

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.R

class UniActionSheetOptionsAdapter(
    private val options: List<UniActionSheetOption>,
    private val dialog: Dialog,
    private val autoClose: Boolean
) : RecyclerView.Adapter<UniActionSheetOptionsAdapter.ViewHolder>() {
    override fun getItemCount() = options.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val resId = when (viewType) {
            1 -> R.layout.uni_action_sheet_text_item
            2 -> R.layout.uni_action_sheet_action_item
            else -> R.layout.uni_action_sheet_spacer_item // never happens
        }

        return ViewHolder(LayoutInflater.from(parent.context).inflate(resId, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return when (options[position]) {
            is UniActionSheetOption.UniActionSheetText -> 1
            is UniActionSheetOption.UniActionSheetAction -> 2
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(options, position, options[position])
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(
            allOptions: List<UniActionSheetOption>,
            position: Int,
            option: UniActionSheetOption,
        ) {
            setBackground(allOptions, position)
            setTextAndBehavior(option)
        }

        private fun setBackground(
            allOptions: List<UniActionSheetOption>,
            position: Int,
        ) {
            val isFirst = position == 0
            val isLast = position == allOptions.size - 1

            val backgroundRes = when {
                isFirst -> R.drawable.drawable_bottom_sheet_dialog_background_top
                isLast -> R.drawable.drawable_bottom_sheet_dialog_background_bottom
                else -> R.drawable.drawable_bottom_sheet_dialog_background_middle
            }

            view.setBackgroundResource(backgroundRes)
        }

        private fun setTextAndBehavior(option: UniActionSheetOption) {
            when (option) {
                is UniActionSheetOption.UniActionSheetText -> {
                    with(view.findViewById<TextView>(R.id.description_text)) {
                        text = option.text
                    }
                }
                is UniActionSheetOption.UniActionSheetAction -> {
                    with(view.findViewById<TextView>(R.id.action_button)) {
                        text = option.text
                        setOnClickListener {
                            if (autoClose) {
                                dialog.dismiss()
                            }

                            option.onClick { dialog.dismiss() }
                        }
                    }
                }
            }
        }
    }
}