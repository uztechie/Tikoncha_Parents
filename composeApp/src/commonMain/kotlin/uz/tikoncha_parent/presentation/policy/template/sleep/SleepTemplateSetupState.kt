package uz.tikoncha_parent.presentation.policy.template.sleep

import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.presentation.policy.policy_list.PolicyItemUi
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class SleepTemplateSetupState(
    val startTime: LocalTime = LocalTime(22, 0),
    val endTime: LocalTime = LocalTime(7, 0),
    val isInitialized: Boolean = false,

    val editingPolicy: PolicyItemUi? = null,

    val createState: ResponseState<Nothing> = ResponseState.Idle,
    val updateState: ResponseState<Nothing> = ResponseState.Idle,
    val deleteState: ResponseState<Nothing> = ResponseState.Idle,
) {
    val isEditMode: Boolean get() = editingPolicy != null

    val isLoading: Boolean
        get() = createState is ResponseState.Loading
                || updateState is ResponseState.Loading
                || deleteState is ResponseState.Loading

    val intervalMinutes: Int
        get() {
            val s = startTime.hour * 60 + startTime.minute
            val e = endTime.hour * 60 + endTime.minute
            return if (e >= s) e - s else (24 * 60 - s) + e
        }
}