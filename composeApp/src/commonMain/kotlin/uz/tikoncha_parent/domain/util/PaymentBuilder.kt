package uz.tikoncha_parent.domain.util

fun buildClickUrl(
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
