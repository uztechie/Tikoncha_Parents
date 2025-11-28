package uz.tikoncha_parent.domain.service

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import uz.tikoncha_parent.domain.util.buildClickUrl

class IOSPaymentService : PaymentService {
    override fun openClickPayment(
        serviceId: String,
        merchantId: String,
        amount: String,
        transactionId: String
    ) {
        val url = NSURL(string = buildClickUrl(serviceId, merchantId, amount, transactionId))
        UIApplication.sharedApplication.openURL(url)

    }
}