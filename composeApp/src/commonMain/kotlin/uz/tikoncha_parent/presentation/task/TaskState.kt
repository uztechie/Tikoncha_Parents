package uz.tikoncha_parent.presentation.task

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.data.remote.model.TodoDto
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.ui_state.ResponseState


data class TaskState(
    var title: String = "",
    var desc: String = "",
    var date: LocalDate? = null,
    var time: LocalTime? = null,
    var importance: ImportanceType = ImportanceType.NONE,
    var isSaveButtonEnabled: Boolean = false,
    var completed: Boolean? = null,
    var showMineAll: Boolean = false,
    var showChildrenAll: Boolean = false,

    val childrenList: List<UserInfo> = emptyList(),
    val selectedChildren: UserInfo? = null,
    val genderIndex: Int = 0,

    val taskResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val childrenResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val listResponseState: ResponseState<Nothing> = ResponseState.Idle,


    val taskId: String? = null,
    val isEditing: Boolean = false,
    val editingTaskId: String? = null,

    val selectedTaskList: List<Task> = emptyList(),


    val childrenTaskList: List<Task> = emptyList(),
    val parentTaskList: List<Task> = emptyList(),
    val allTaskList: List<TodoDto> = emptyList(),


    val parentCompletedTaskList: List<Task> = emptyList(),
    val childrenCompletedTaskList: List<Task> = emptyList(),
    val selectedCompletedTaskList: List<Task> = emptyList(),
)
