package uz.tikoncha_parent.presentation.add_child

data class ChildrenSnapshot(
    val success: Boolean = false,
    val count: Int = 0,
    val phones: Set<String> = emptySet()
)