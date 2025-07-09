package org.example.project.presentation.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class HomeViewModel: ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onEvent(event: HomeEvent){
        when(event){
            is HomeEvent.GetUsageList -> {

            }

            HomeEvent.OnChildSelectClicked -> {

            }
            is HomeEvent.OnChildSelected -> {
                _state.update {
                    it.copy(selectedChildren = event.child)
                }
            }
        }
    }
}