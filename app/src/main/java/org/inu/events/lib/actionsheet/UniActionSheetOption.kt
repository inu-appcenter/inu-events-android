package org.inu.events.lib.actionsheet

sealed class UniActionSheetOption {
    data class UniActionSheetText(
        val text: String
    ) : UniActionSheetOption()

    data class UniActionSheetAction(
        val text: String,
        val dimmed: Boolean,
        val onClick: (dismiss: () -> Unit) -> Unit
    ) : UniActionSheetOption()
}