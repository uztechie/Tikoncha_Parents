package uz.tikoncha_parent.domain.service

interface PaymentService {
    fun openClickPayment(
        serviceId: String,
        merchantId: String,
        amount: String,
        transactionId: String
    )
}
