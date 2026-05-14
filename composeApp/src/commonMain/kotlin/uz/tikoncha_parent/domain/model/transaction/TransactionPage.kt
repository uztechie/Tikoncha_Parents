package uz.tikoncha_parent.domain.model.transaction

data class TransactionPage(
    val items: List<Transaction>,
    val total: Int,
    val limit: Int,
    val offset: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
)