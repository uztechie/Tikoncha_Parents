package uz.tikoncha_parent.platform

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.native.concurrent.ThreadLocal
import platform.Foundation.NSError
import platform.StoreKit.*
import platform.darwin.NSObject
import uz.tikoncha_parent.domain.model.PurchaseResult

@ThreadLocal
private val storeKitManager = StoreKitPurchaseManager()

actual class PlatformPurchaseService actual constructor() {
    actual suspend fun purchase(productId: String): PurchaseResult {
        return storeKitManager.purchase(productId)
    }
}

private class StoreKitPurchaseManager : NSObject(),
    SKPaymentTransactionObserverProtocol,
    SKProductsRequestDelegateProtocol {

    private var purchaseCont: ((PurchaseResult) -> Unit)? = null
    private var productsCont: ((SKProduct?, String?) -> Unit)? = null

    private var inProgress = false
    private val cache = mutableMapOf<String, SKProduct>()

    // ✅ IMPORTANT: request'ni saqlab turamiz (GC bo‘lib ketmasin)
    private var productsRequest: SKProductsRequest? = null

    init {
        SKPaymentQueue.defaultQueue().addTransactionObserver(this)
    }

    suspend fun purchase(productId: String): PurchaseResult =
        suspendCoroutine { cont ->

            if (inProgress) {
                cont.resume(PurchaseResult.Error("Purchase already in progress"))
                return@suspendCoroutine
            }

            if (!SKPaymentQueue.canMakePayments()) {
                cont.resume(PurchaseResult.Error("Payments are disabled on this device"))
                return@suspendCoroutine
            }

            inProgress = true
            purchaseCont = { r ->
                inProgress = false
                cont.resume(r)
            }

            fetchProduct(productId) { product, error ->
                if (product == null) {
                    purchaseCont?.invoke(PurchaseResult.Error(error ?: "Product not found"))
                    purchaseCont = null
                    return@fetchProduct
                }

                val payment = SKPayment.paymentWithProduct(product)
                SKPaymentQueue.defaultQueue().addPayment(payment)
            }
        }

    private fun fetchProduct(productId: String, cb: (SKProduct?, String?) -> Unit) {
        cache[productId]?.let { cached ->
            cb(cached, null)
            return
        }

        productsCont = cb

        // ✅ StoreKit expects Set<String> — setOf(productId) to‘g‘ri
        val req = SKProductsRequest(productIdentifiers = setOf(productId))
        productsRequest = req
        req.delegate = this
        req.start()
    }

    override fun productsRequest(request: SKProductsRequest, didReceiveResponse: SKProductsResponse) {
        // request tugadi — endi release qilamiz
        productsRequest = null

        val products = didReceiveResponse.products.filterIsInstance<SKProduct>()
        val invalid = didReceiveResponse.invalidProductIdentifiers
            ?.filterIsInstance<String>()
            ?: emptyList()

        // ✅ DEBUG LOG (Xcode console’da ko‘rasiz)
        println("IAP products returned: ${products.map { it.productIdentifier }}")
        println("IAP invalid ids: $invalid")

        val product = products.firstOrNull()
        if (product != null) {
            cache[product.productIdentifier] = product
            productsCont?.invoke(product, null)
        } else {
            val reason =
                if (invalid.isNotEmpty()) "Invalid product id(s): ${invalid.joinToString()}"
                else "No products returned. Check App Store Connect status / sandbox / bundleId / build."
            productsCont?.invoke(null, reason)
        }

        productsCont = null
    }

    override fun request(request: SKRequest, didFailWithError: NSError) {
        productsRequest = null
        productsCont?.invoke(null, didFailWithError.localizedDescription ?: "Products request failed")
        productsCont = null
    }

    override fun paymentQueue(queue: SKPaymentQueue, updatedTransactions: List<*>) {
        updatedTransactions
            .filterIsInstance<SKPaymentTransaction>()
            .forEach { tx ->
                when (tx.transactionState) {

                    SKPaymentTransactionState.SKPaymentTransactionStatePurchased -> {
                        purchaseCont?.invoke(PurchaseResult.Success)
                        purchaseCont = null
                        queue.finishTransaction(tx)
                    }

                    SKPaymentTransactionState.SKPaymentTransactionStateFailed -> {
                        val cancelled = (tx.error as? NSError)?.code == SKErrorCode.SKErrorPaymentCancelled.value
                        val msg = tx.error?.localizedDescription ?: "Purchase failed"

                        purchaseCont?.invoke(
                            if (cancelled) PurchaseResult.Cancelled
                            else PurchaseResult.Error(msg)
                        )
                        purchaseCont = null
                        queue.finishTransaction(tx)
                    }

                    SKPaymentTransactionState.SKPaymentTransactionStateDeferred -> {
                        purchaseCont?.invoke(PurchaseResult.Pending)
                        purchaseCont = null
                    }

                    else -> Unit
                }
            }
    }
}
