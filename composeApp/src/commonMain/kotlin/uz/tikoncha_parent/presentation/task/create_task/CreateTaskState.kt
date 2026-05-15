package uz.tikoncha_parent.presentation.task.create_task

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.task.model.ImportanceType
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class CreateTaskState(
    // Form
    val title: String = "",
    val desc: String = "",
    val date: LocalDate? = null,
    val time: LocalTime? = null,
    val importance: ImportanceType = ImportanceType.NONE,
    val completed: Boolean? = null,

    // Child
    val selectedChild: UserInfo? = null,
    val childrenList: List<UserInfo> = emptyList(),

    // Coins
    val availableCoins: Int = 0,
    val selectedChips: Set<Int> = emptySet(),
    val extraCoin: Int = 0,

    // Editing
    val isEditing: Boolean = false,
    val editingTaskId: String? = null,
    val editingTaskCreatedAt: Long? = null,

    // Response
    val taskResponseState: ResponseState<Nothing> = ResponseState.Idle,
) {
    val totalCoin: Int get() = selectedChips.sum() + extraCoin
    val remainingCoins: Int get() = (availableCoins - totalCoin).coerceAtLeast(0)
}