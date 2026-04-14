package uz.tikoncha_parent.platform

object PaymentUtil {

    fun openClickUrl(
        serviceId: String,
        merchantId: String,
        amount: String,
        transactionId: String
    ): String {
        return "https://my.click.uz/services/pay/" +
                "?service_id=$serviceId" +
                "&merchant_id=$merchantId" +
                "&amount=$amount" +
                "&transaction_param=$transactionId"
    }

    fun openPaymeUrl(
        merchantId: String,
        amount: Long,
        transactionId: String
    ): String {
        return "https://checkout.paycom.uz/" +
                "?m=$merchantId" +
                "&ac.order_id=$transactionId" +
                "&a=$amount"
    }
}