package org.inu.events.lib.actionsheet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.inu.events.R

/**
 * iOS 스타일 액션 시트입니다.
 * 다음 요소를 지원합니다:
 * - description 텍스트
 * - action 버튼
 * - 취소 버튼
 */
class UniActionSheet(private val context: Context) {
    private val options: MutableList<UniActionSheetOption> = mutableListOf()

    /**
     * 텍스트를 추가합니다.
     */
    fun addText(text: String): UniActionSheet {
        options.add(
            UniActionSheetOption.UniActionSheetText(text)
        )

        return this
    }

    /**
     * 액션 버튼을 추가합니다.
     * @param onClick 버튼이 눌렸을 때 호출될 콜백입니다. 콜백의 첫번째 인자에 dismiss 함수를 담아서 호출합니다.
     */
    fun addAction(text: String, onClick: (dismiss: () -> Unit) -> Unit): UniActionSheet {
        options.add(
            UniActionSheetOption.UniActionSheetAction(text, onClick)
        )

        return this
    }

    /**
     * 다이얼로그를 표시합니다.
     * @param autoClose 버튼이 눌렸을 때, 다이얼로그를 먼저 닫을지 여부입니다. 기본 true입니다.
     */
    fun show(autoClose: Boolean = true) {
        val layout = LayoutInflater.from(context).inflate(R.layout.uni_action_sheet, null)

        val dialog = BottomSheetDialog(context).apply {
            setContentView(layout)
            setCanceledOnTouchOutside(false)
            window
                ?.findViewById<View>(R.id.design_bottom_sheet)
                ?.setBackgroundResource(android.R.color.transparent)
        }

        with(layout.findViewById<RecyclerView>(R.id.options)) {
            adapter = UniActionSheetOptionsAdapter(options, dialog, autoClose)
            addItemDecoration(DividerItemDecorator(ContextCompat.getDrawable(context, R.drawable.uni_action_sheet_item_divider)))
        }

        with(layout.findViewById<Button>(R.id.cancel_button)) {
            setOnClickListener { dialog.dismiss() }
        }

        dialog.show()
    }
}