package org.inu.events.lib.spinner

import android.content.Context
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ListPopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import org.inu.events.R

class UniSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var items: Array<CharSequence>
    private lateinit var selectedText: TextView
    private val popUp = ListPopupWindow(context)
    private lateinit var adapter: ArrayAdapter<CharSequence>

    init {
        inflate(context, R.layout.uni_spinner, this)

        setAttrs(attrs)
        setAdapter()
        setPopup()
        setSelectedText()
    }

    private fun setAttrs(attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.UniSpinner).run {
            items = getTextArray(R.styleable.UniSpinner_items) ?: arrayOf("전체", "간식나눔", "공고")
            recycle()
        }
    }


    private fun setAdapter() {
        adapter = ArrayAdapter(context, R.layout.uni_spinner_item, items)
        popUp.setAdapter(adapter)
    }

    private fun measureWidth(): Int {
        var width = 0
        for (i in items.indices) {
            val view = adapter.getView(i, null, FrameLayout(context))
            view.measure(0, 0)
            width = width.coerceAtLeast(view.measuredWidth)
        }
        return width
    }

    private fun setSelectedText() {
        selectedText = findViewById<TextView>(R.id.selected_item).apply {
            text = items.first()
            width = popUp.width
            setOnClickListener {
                if (popUp.isShowing) {
                    popUp.dismiss()
                } else {
                    popUp.show()
                }
            }
        }
    }

    private fun setPopup() {
        popUp.apply {
            isModal = true
            anchorView = this@UniSpinner
            verticalOffset = this.height + 8
            setContentWidth(measureWidth() + 100)
        }

    }

    fun setOnItemClick(onClick: (pos:Int) -> Unit) {
        popUp.setOnItemClickListener { _, _, position, _ ->
            selectedText.text = items[position]
            popUp.dismiss()
            onClick(position)
        }
    }
}