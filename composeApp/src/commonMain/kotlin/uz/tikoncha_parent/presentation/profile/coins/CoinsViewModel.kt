package uz.tikoncha_parent.presentation.profile.coins

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.common.Util.toCurrency
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.ChildRepository
import uz.tikoncha_parent.domain.use_case.chat.GetMyCoinsUseCase
import uz.tikoncha_parent.domain.use_case.payment.GetCoinPackageListUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class CoinsViewModel(
    private val getMyCoinsUseCase: GetMyCoinsUseCase,
    private val coinsPackageListUseCase: GetCoinPackageListUseCase,
    private val childRepository: ChildRepository,
): ScreenModel {

    private val _state = MutableStateFlow(CoinsState())
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

            CoinsEvent.LoadCoinList -> {
                getCoinPackagesList()
            }

            is CoinsEvent.OnCoinsChanged -> {
                _state.update {
                    it.copy(
                        coinsToBuy = event.value,
                        totalPrice = event.value * it.coinPrice
                    )
                }
            }

            is CoinsEvent.OnPackageSelected -> {
                _state.update {
                    it.copy(
                        selectedPackageIndex = event.index,
                        coinsToBuy = it.coinPackageList[event.index].coins,
                        totalPrice = it.coinPackageList[event.index].priceWithDiscount.toInt()
                    )
                }
            }
        }
    }

    fun getCoinPackagesList() {
        screenModelScope.launch {
            _state.update {
                it.copy(isLoading = true, error = null)
            }
            when (val result = coinsPackageListUseCase()) {
                is Resource.Success -> {
                    val coinPrice = result.data.coin_price
                    val list = result.data.coin_packages.map {
                        val originalPrice = if (it.discount_percent in 1..99) {
                            (it.price * 100L / (100 - it.discount_percent))
                        } else {
                            it.price.toLong()
                        }
                        val discountAmount = (originalPrice - it.price).coerceAtLeast(0)

                        CoinPackageUi(
                            coins = it.coins,
                            price = originalPrice,
                            priceWithDiscount = it.price.toLong(),
                            discountedPrice = discountAmount,
                            discountPercent = it.discount_percent,
                            priceInString = "${originalPrice.toCurrency()} UZS",
                            priceWithDiscountInString = "${it.price.toLong().toCurrency()} UZS",
                        )
                    }
                    _state.update {
                        it.copy(
                            error = null,
                            isLoading = false,
                            coinPrice = coinPrice,
                            coinPackageList = list
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = result.message)
                    }
                }

                is Resource.Loading -> {}
            }
        }
    }

    fun load() {
        if (_state.value.isLoading) return

        _state.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
        screenModelScope.launch {
            when(val response = getMyCoinsUseCase()){
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            myCoins = response.data.coins,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            myCoins = 0,
                            isLoading = false,
                            error = response.message
                        )
                    }
                }
                is Resource.Loading<*> -> {}
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

            when (val res = childRepository.children()) {
                is Outcome.Failure -> _state.update {
                    it.copy(childrenResponseState = ResponseState.Error(failure = res))
                }

                is Outcome.Success -> {
                    _state.update {
                        it.copy(
                            childrenResponseState = ResponseState.Success(),
                            childrenList = res.data,
                            selectedChild = AppSettings.selectedChild
                        )
                    }
                    AppSettings.children = res.data
                    if (AppSettings.selectedChild == null) {
                        AppSettings.selectedChild = AppSettings.children.firstOrNull()
                    }
                }
            }
        }
    }
}