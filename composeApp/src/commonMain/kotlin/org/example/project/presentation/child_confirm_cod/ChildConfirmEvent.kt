package org.example.project.presentation.child_confirm_cod

import androidx.compose.ui.text.input.TextFieldValue

sealed class ChildConfirmEvent {
    data class OnNumberCod(val codNumber: TextFieldValue): ChildConfirmEvent()
}