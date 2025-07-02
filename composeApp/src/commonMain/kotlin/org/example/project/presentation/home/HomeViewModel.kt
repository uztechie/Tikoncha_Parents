package org.example.project.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.domain.TikonchaRepository
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Single



class HomeViewModel(val repository: TikonchaRepository): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onEvent(event: HomeEvent){
        when(event){
            HomeEvent.Count -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        counter = _state.value.counter + 1,
                        list = repository.login()
                    )
                }


            }
        }
    }
}