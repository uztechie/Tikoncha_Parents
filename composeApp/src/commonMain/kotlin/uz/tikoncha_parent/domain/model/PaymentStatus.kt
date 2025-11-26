package uz.tikoncha_parent.domain.model

enum class PaymentStatus {
    PENDING, COMPLETED, CANCELLED, FAILED, START;

    companion object{
        fun fromString(value: String): PaymentStatus = when(value){
            "PENDING" -> PENDING
            "COMPLETED" -> COMPLETED
            "CANCELLED" -> CANCELLED
            "FAILED" -> FAILED
            "START" -> START
            else -> START
        }
    }
}