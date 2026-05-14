package uz.tikoncha_parent.presentation.profile.payment_history

sealed interface PaymentHistoryEvent {
    /** Pull-to-refresh — offset reset, items tozalanadi */
    data object Refresh : PaymentHistoryEvent

    /** Pagination — keyingi sahifa, items'ga qo'shiladi */
    data object LoadNext : PaymentHistoryEvent

    /** Initial error'dan keyin qayta urinish */
    data object RetryInitial : PaymentHistoryEvent

    /** Pagination error'dan keyin qayta urinish */
    data object RetryPagination : PaymentHistoryEvent
}