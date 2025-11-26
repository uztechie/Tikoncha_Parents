package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.SubscriptionPurchaseRequest
import uz.tikoncha_parent.data.remote.model.SubscriptionPurchaseResponse

class PaymentApiService(private val client: HttpClient) {



    suspend fun subscriptionPurchase(subscriptionPurchaseRequest: SubscriptionPurchaseRequest): SubscriptionPurchaseResponse =
        client.safeRequest(
            method = HttpMethod.Companion.Post,
            url = "/subscriptions/purchase-intent",
            block = {
                setBody(subscriptionPurchaseRequest)
            }
        )


}