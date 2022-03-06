package org.inu.events.lib.actionsheet

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.R
import org.inu.events.common.extension.setBackgroundTintResource

class UniActionSheetOptionsAdapter(
    private val options: List<UniActionSheetOption>,
    private val dialog: Dialog,
    private val autoClose: Boolean
) : RecyclerView.Adapter<UniActionSheetOptionsAdapter.ViewHolder>() {
    override fun getItemCount() = options.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val resId = when (viewType) {
            VIEW_TYPE_TEXT -> R.layout.uni_action_sheet_text_item
            VIEW_TYPE_ACTION -> R.layout.uni_action_sheet_action_item
            else -> throw RuntimeException("있을 수 없는 일이라며 난 울었어~ 내 심미성과 안정성을 모두 버려야 했기에~")
        }

        return ViewHolder(LayoutInflater.from(parent.context).inflate(resId, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return when (options[position]) {
            is UniActionSheetOption.UniActionSheetText -> VIEW_TYPE_TEXT
            is UniActionSheetOption.UniActionSheetAction -> VIEW_TYPE_ACTION
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
            setBackgroundShape(allOptions, position)
            setBackgroundTint(option)
            setTextAndBehavior(option)
        }

        private fun setBackgroundShape(
            allOptions: List<UniActionSheetOption>,
            position: Int,
        ) {
            val isFirst = position == 0
            val isLast = position == allOptions.size - 1

            val backgroundRes = when {
                isFirst && isLast -> R.drawable.drawable_bottom_sheet_dialog_background_alone
                isFirst && !isLast -> R.drawable.drawable_bottom_sheet_dialog_background_top
                !isFirst && isLast -> R.drawable.drawable_bottom_sheet_dialog_background_bottom
                !isFirst && !isLast -> R.drawable.drawable_bottom_sheet_dialog_background_middle
                else -> throw RuntimeException("어! 느새! 부터! when! 문! 은 안멋져!")
            }

            view.setBackgroundResource(backgroundRes)
        }

        private fun setBackgroundTint(option: UniActionSheetOption) {
            when (option) {
                is UniActionSheetOption.UniActionSheetText -> {
                    view.setBackgroundTintResource(R.color.white)
                }
                is UniActionSheetOption.UniActionSheetAction -> {
                    view.setBackgroundTintResource(
                        if (option.dimmed) R.color.black10
                        else R.color.white
                    )
                }
            }
        }

        private fun setTextAndBehavior(option: UniActionSheetOption) {
            when (option) {
                is UniActionSheetOption.UniActionSheetText -> {
                    with(view.findViewById<TextView>(R.id.description_text)) {
                        text = option.text
                    }
                }
                is UniActionSheetOption.UniActionSheetAction -> {
                    with(view.findViewById<AppCompatButton>(R.id.action_button)) {
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

    companion object {
        private const val VIEW_TYPE_TEXT = 0
        private const val VIEW_TYPE_ACTION = 1
    }
}