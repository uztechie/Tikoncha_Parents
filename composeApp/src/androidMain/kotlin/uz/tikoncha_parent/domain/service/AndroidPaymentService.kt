package uz.tikoncha_parent.domain.service

import android.content.Context
import android.content.Intent
import uz.tikoncha_parent.domain.util.buildClickUrl
import androidx.core.net.toUri

class AndroidPaymentService(
    private val context: Context
): PaymentService {
    override fun openClickPayment(
        serviceId: String,
        merchantTransId: String,
        amount: String,
        transactionId: String
    ) {
        val url = buildClickUrl(serviceId, merchantTransId, amount, transactionId)
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}