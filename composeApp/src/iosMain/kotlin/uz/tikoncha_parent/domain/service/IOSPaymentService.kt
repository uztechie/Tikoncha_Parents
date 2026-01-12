package uz.tikoncha_parent.domain.service

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import uz.tikoncha_parent.domain.util.buildClickUrl

class IOSPaymentService : PaymentService {

    override fun openClickPayment(
        serviceId: String,
        merchantId: String,
        amount: String,
        transactionId: String
    ) {
        val urlString = buildClickUrl(serviceId, merchantId, amount, transactionId)
        val url = NSURL(string = urlString) ?: return

        // UI faqat main threadda ochilishi kerak
        dispatch_async(dispatch_get_main_queue()) {
            val app = UIApplication.sharedApplication

            if (app.canOpenURL(url)) {
                app.openURL(
                    url = url,
                    options = emptyMap<Any?, Any?>(),
                    completionHandler = { success ->
                        if (!success) {
                            println("❌ URL ochilmadi: $urlString")
                        }
                    }
                )
            } else {
                println("❌ canOpenURL = false: $urlString")
            }
        }
    }
}