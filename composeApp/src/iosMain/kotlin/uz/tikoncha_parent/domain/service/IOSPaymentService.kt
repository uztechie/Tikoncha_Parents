package uz.tikoncha_parent.domain.service

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import uz.tikoncha_parent.domain.util.buildClickUrl

class IOSPaymentService : PaymentService {
    override fun openClickPayment(
        serviceId: String,
        merchantTransId: String,
        amount: String,
        transactionId: String
    ) {
        val url = NSURL(string = buildClickUrl(serviceId, merchantTransId, amount, transactionId))
        UIApplication.sharedApplication.openURL(url)

    }
}