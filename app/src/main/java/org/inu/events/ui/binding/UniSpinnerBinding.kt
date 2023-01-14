package org.inu.events.ui.binding

import androidx.databinding.BindingAdapter
import org.inu.events.lib.spinner.UniSpinner

@BindingAdapter("setOnItemClick")
fun bindSetOnItemClick(view: UniSpinner, onClick: (position: Int) -> Unit) {
    view.setOnItemClick { position ->
        onClick(position)
    }
}