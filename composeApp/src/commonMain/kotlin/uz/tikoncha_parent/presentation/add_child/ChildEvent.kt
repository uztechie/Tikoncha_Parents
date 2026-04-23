package uz.tikoncha_parent.presentation.add_child

sealed class ChildEvent {
    data class OnNumberInsert(val number: String): ChildEvent()
    data object Reset: ChildEvent()
    data object Clear: ChildEvent()
    data object OnAddClicked: ChildEvent()
    data object StartWatching : ChildEvent()      // YANGI
    data object StopWatching : ChildEvent()       // YANGI
    data object OnSuccessDismissed : ChildEvent() // YANGI
}