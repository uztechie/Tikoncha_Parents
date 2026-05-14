package uz.tikoncha_parent.presentation.profile.payment_history

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.use_case.payment.GetPaymentTransactionsUseCase

class PaymentHistoryScreenModel(
    private val getTransactions: GetPaymentTransactionsUseCase,
) : ScreenModel {

    private val _state = MutableStateFlow(PaymentHistoryState())
    val state = _state.asStateFlow()

    init {
        loadInitial()
    }

    fun onEvent(event: PaymentHistoryEvent) {
        when (event) {
            PaymentHistoryEvent.Refresh -> refresh()
            PaymentHistoryEvent.LoadNext -> loadNext()
            PaymentHistoryEvent.RetryInitial -> loadInitial()
            PaymentHistoryEvent.RetryPagination -> loadNext()
        }
    }

    private fun loadInitial() {
        val current = _state.value
        if (current.isInitialLoading) return

        screenModelScope.launch {
            _state.update {
                it.copy(
                    isInitialLoading = true,
                    initialError = null,
                    paginationError = null,
                )
            }

            getTransactions(limit = PaymentHistoryState.PAGE_SIZE, offset = 0)
                .onSuccess { page ->
                    _state.update {
                        it.copy(
                            items = page.items,
                            hasNext = page.hasNext,
                            isInitialLoading = false,
                            hasLoadedOnce = true,
                            initialError = null,
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isInitialLoading = false,
                            initialError = e.message ?: "Xatolik yuz berdi",
                            hasLoadedOnce = true,
                        )
                    }
                }
        }
    }

    private fun refresh() {
        val current = _state.value
        // Birinchi yuklash davom etayotgan bo'lsa — refresh bermaymiz
        if (current.isRefreshing || current.isInitialLoading) return

        screenModelScope.launch {
            _state.update {
                it.copy(isRefreshing = true, paginationError = null)
            }

            getTransactions(limit = PaymentHistoryState.PAGE_SIZE, offset = 0)
                .onSuccess { page ->
                    _state.update {
                        it.copy(
                            items = page.items,
                            hasNext = page.hasNext,
                            isRefreshing = false,
                            initialError = null,
                            paginationError = null,
                            hasLoadedOnce = true,
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                            paginationError = e.message ?: "Yangilashda xatolik",
                        )
                    }
                }
        }
    }

    private fun loadNext() {
        val current = _state.value

        // 4 ta xavfsizlik tekshiruvi — silliq pagination uchun
        if (current.isLoadingNext) return                    // qayta so'rovni to'sadi
        if (current.isInitialLoading || current.isRefreshing) return
        if (!current.hasNext) return                          // server "tamom" dedi
        if (current.items.isEmpty()) return                   // sodda xavfsizlik

        screenModelScope.launch {
            _state.update {
                it.copy(isLoadingNext = true, paginationError = null)
            }

            // Offset = hozir bizda mavjud items soni
            // Bu has_next != items.size scenariyda ham to'g'ri ishlaydi
            val nextOffset = current.items.size

            getTransactions(limit = current.limit, offset = nextOffset)
                .onSuccess { page ->
                    _state.update {
                        it.copy(
                            items = it.items + page.items,
                            hasNext = page.hasNext,
                            isLoadingNext = false,
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoadingNext = false,
                            paginationError = e.message ?: "Yuklashda xatolik",
                        )
                    }
                }
        }
    }
}