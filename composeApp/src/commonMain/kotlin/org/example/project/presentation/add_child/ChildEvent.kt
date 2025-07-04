package org.example.project.presentation.add_child

sealed class ChildEvent {
    data class OnNumberInsert(val number: String): ChildEvent()
    object OnConfirmClicked: ChildEvent()
}