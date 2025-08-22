package org.example.project.presentation.child_confirm_cod

import androidx.compose.ui.text.input.TextFieldValue

sealed class ChildConfirmEvent {
    data class SetConfirmCode(val code: String): ChildConfirmEvent()
}