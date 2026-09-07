package uz.tikoncha_parent.presentation.profile.payment_history

import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.transaction.Transaction

data class PaymentHistoryState(
    val items: List<Transaction> = emptyList(),

    // 3 ta alohida loading flag — har xil UI ko'rsatishlari uchun
    val isInitialLoading: Boolean = false,   // birinchi yuklash, full-screen loader
    val isLoadingNext: Boolean = false,      // pagination, list pastida loader
    val isRefreshing: Boolean = false,       // pull-to-refresh, top indicator

    val limit: Int = PAGE_SIZE,
    val hasNext: Boolean = true,             // server'dan keladi
    val hasLoadedOnce: Boolean = false,      // hech bo'lmasa 1 marta urinish bo'ldimi

    // Xatolar ham alohida — pagination xatosi ro'yxatni o'chirmaydi
    val initialError: Outcome.Failure? = null,
    val paginationError: Outcome.Failure? = null,
) {
    /** Empty state'ni ko'rsatish vaqti */
    val showEmpty: Boolean
        get() = items.isEmpty() &&
                hasLoadedOnce &&
                !isInitialLoading &&
                !isRefreshing &&
                initialError == null

    /** Full-screen error (faqat birinchi yuklashda) */
    val showInitialError: Boolean
        get() = initialError != null && items.isEmpty()

    /** Pastdagi "list tugadi" indikatori */
    val showEndOfList: Boolean
        get() = items.isNotEmpty() &&
                !hasNext &&
                paginationError == null &&
                !isLoadingNext

    companion object {
        const val PAGE_SIZE = 20
        /** Oxiriga N item qolganda keyingi sahifani yuklaymiz */
        const val PREFETCH_THRESHOLD = 5
    }
}