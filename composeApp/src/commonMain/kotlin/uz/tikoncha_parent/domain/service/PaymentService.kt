package uz.tikoncha_parent.domain.service

interface PaymentService {
    fun openClickPayment(
        serviceId: String,
        merchantTransId: String,
        amount: String,
        transactionId: String
    )
}
