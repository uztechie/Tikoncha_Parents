package uz.tikoncha_parent.presentation.profile.coins

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.option.viewModelScopeFactory
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.chat.MyCoinsUseCase

class MyCoinsViewModel(
    private val useCase: MyCoinsUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(MyCoinsState())
    val state = _state.asStateFlow()

    fun load(){
        if (_state.value.isLoading) return

        _state.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        viewModelScope.launch {
            val response = useCase()
            when(response){
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            coins = response.data.coins,
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = response.message
                        )
                    }
                }
                is Resource.Loading<*> -> {}
            }
        }
    }
}