package uz.tikoncha_parent.presentation.profile.coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.CoinPackage
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.GetCoinPackagesUseCase
import uz.tikoncha_parent.domain.use_case.chat.MyCoinsUseCase

class MyCoinsViewModel(
    private val useCase: MyCoinsUseCase,
    private val getCoinPackagesUseCase: GetCoinPackagesUseCase
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

    fun loadCoinsPackages(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            val result = getCoinPackagesUseCase()
            when (result){
                is Resource.Success -> {
                    val list = result.data

                    _state.update {
                        it.copy(
                            isLoading = false,
                            packages = list.map { coinPackage ->
                                CoinPackage(
                                    coins = coinPackage.coins,
                                    price = coinPackage.price,
                                    discountPercent = coinPackage.discountPercent
                                )
                            }
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }
}