package org.example.project.presentation.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class HomeViewModel: ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onEvent(event: HomeEvent){
        when(event){
            HomeEvent.Count -> {
                _state.value = _state.value.copy(
                    counter = _state.value.counter + 1
                )

            }
        }
    }
}