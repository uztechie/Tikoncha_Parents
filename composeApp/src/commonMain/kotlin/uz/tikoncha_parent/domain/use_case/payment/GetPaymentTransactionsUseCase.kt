package uz.tikoncha_parent.domain.use_case.payment

import uz.tikoncha_parent.domain.model.transaction.TransactionPage
import uz.tikoncha_parent.domain.repository.PaymentRepository

class GetPaymentTransactionsUseCase(
    private val repository: PaymentRepository,
) {

    suspend operator fun invoke(
        limit: Int = DEFAULT_PAGE_SIZE,
        offset: Int = 0,
    ): Result<TransactionPage> = runCatching {
        require(limit in MIN_LIMIT..MAX_LIMIT) {
            "limit must be in $MIN_LIMIT..$MAX_LIMIT (got $limit)"
        }
        require(offset >= 0) { "offset must be >= 0 (got $offset)" }

        repository.paymentTransactions(limit = limit, offset = offset)
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
        const val MIN_LIMIT = 1
        const val MAX_LIMIT = 200
    }
}