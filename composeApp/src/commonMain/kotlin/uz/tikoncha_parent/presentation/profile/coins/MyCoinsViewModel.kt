package uz.tikoncha_parent.presentation.profile.coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toUserInfo
import uz.tikoncha_parent.domain.model.CoinPackage
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.domain.use_case.GetCoinPackagesUseCase
import uz.tikoncha_parent.domain.use_case.chat.MyCoinsUseCase
import uz.tikoncha_parent.presentation.new_home.HomeEvent
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class MyCoinsViewModel(
    private val useCase: MyCoinsUseCase,
    private val getCoinPackagesUseCase: GetCoinPackagesUseCase,
    private val childrenUseCase: ChildrenUseCase,
): ScreenModel {

    private val _state = MutableStateFlow(MyCoinsState())
    val state = _state.asStateFlow()

    private var childrenJob: Job? = null


    fun onEvent(event: CoinsEvent){
        when(event){
            is CoinsEvent.OnChildSelected -> {
                _state.update {
                    it.copy(selectedChild = event.child)
                }
                AppSettings.selectedChildId = event.child.userId
                AppSettings.selectedChild = event.child
            }

            CoinsEvent.GetChildren -> {
                loadChildren()
            }
        }
    }

    fun load(){
        if (_state.value.isLoading) return

        _state.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        screenModelScope.launch {
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
        screenModelScope.launch {
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

    private fun loadChildren() {
        childrenJob?.cancel()
        childrenJob = screenModelScope.launch {
            _state.update {
                it.copy(
                    childrenResponseState = ResponseState.Loading
                )
            }

            val response = childrenUseCase.invoke()
            when (response) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            childrenResponseState = ResponseState.Error(
                                res = response.resId,
                                message = response.message
                            )
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            childrenResponseState = ResponseState.Success(),
                            childrenList = response.data.map { userInfoDto -> userInfoDto.toUserInfo() },
                            selectedChild = AppSettings.selectedChild
                        )
                    }
                    AppSettings.children = response.data.map { userInfoDto -> userInfoDto.toUserInfo() }
                    if (AppSettings.selectedChild == null){
                        AppSettings.selectedChild = AppSettings.children.firstOrNull()
                    }

                }
            }
        }
    }

}