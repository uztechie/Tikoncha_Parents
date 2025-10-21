package uz.tikoncha_parent.presentation.home.schedule

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uz.tikoncha_parent.data.mapper.AppCategoryUi

class ScheduleViewModel : ViewModel() {

    private val _state = MutableStateFlow(ScheduleState(isLoading = true))
    val state = _state.asStateFlow()

    fun onEvent(event: ScheduleEvent) {
        when (event) {
            is ScheduleEvent.OnGenderSelected -> {
                _state.update {
                    it.copy(
                        genderIndex = event.genderIndex
                    )
                }
            }
        }
    }

    fun toggleCategory(categoryId: String) {
        _state.update {
            it.copy(
                categories = state.value.categories.map { category ->
                    if (category.id == categoryId) {
                        category.copy(
                            expanded = !category.expanded
                        )
                    } else {
                        category
                    }
                }
            )
        }
    }

    fun appsCheck(categoryId: String, appId: String, newChange: Boolean) {
        _state.update {
            it.copy(
                categories = state.value.categories.map { category ->
                    if(category.id != categoryId){
                        category
                    } else {
                        category.copy(
                            apps = category.apps.map { app ->
                                if (app.id == appId){
                                    app.copy(
                                        checked = newChange
                                    )
                                } else {
                                    app
                                }
                            }
                        )
                    }
                }
            )
        }
    }

    fun relaceAll(newList: List<AppCategoryUi>){
        _state.update {
            it.copy(
                categories = newList,
                isLoading = false,
                error = null
            )
        }
    }
}