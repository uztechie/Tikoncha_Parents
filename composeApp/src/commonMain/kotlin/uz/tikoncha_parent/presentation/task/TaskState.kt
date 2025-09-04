package uz.tikoncha_parent.presentation.task

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.data.remote.model.TodoDto
import uz.tikoncha_parent.domain.model.UserInfo


data class TaskState(
    var title: String = "",
    var desc: String = "",
    var date: LocalDate? = null,
    var time: LocalTime? = null,
    var importance: ImportanceType = ImportanceType.NONE,
    var isSaveButtonEnabled: Boolean = false,
    var completed: Boolean? = null,
    var showMineAll: Boolean = false,

    val childrenList: List<UserInfo> = emptyList(),
    val selectedChildren: UserInfo? = null,
    val genderIndex: Int = 0,

    val taskSuccess: Boolean = false,
    val taskLoading: Boolean = false,
    val taskError: String = "",

    val childrenLoading: Boolean = false,
    val childrenError: String = "",

    val listLoading: Boolean = false,
    val listError: String = "",


    val selectedTaskList: List<Task> = emptyList(),


    val childrenTaskList: List<Task> = emptyList(),
    val parentTaskList: List<Task> = emptyList(),
    val allTaskList: List<TodoDto> = emptyList(),


    val parentCompletedTaskList: List<Task> = emptyList(),
    val childrenCompletedTaskList: List<Task> = emptyList(),
    val selectedCompletedTaskList: List<Task> = emptyList(),
)
