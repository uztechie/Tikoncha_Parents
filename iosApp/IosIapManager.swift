
import Foundation
import StoreKit

@objc(IosIapManager)
@objcMembers
public final class IosIapManager: NSObject {

    public static let shared = IosIapManager()

    private override init() {}

    // MARK: - Models (ObjC friendly)
    @objc(IosIapPlan)
    public final class Plan: NSObject {
        public let productId: String
        public let title: String
        public let price: String

        public init(productId: String, title: String, price: String) {
            self.productId = productId
            self.title = title
            self.price = price
        }
    }

    @objc(IosIapPurchaseResult)
    public final class PurchaseResult: NSObject {
        public let productId: String
        public let transactionId: String
        public let originalTransactionId: String
        public let purchasedAtMs: NSNumber
        public let expiresAtMs: NSNumber? // subscription uchun muhim

        public init(
            productId: String,
            transactionId: String,
            originalTransactionId: String,
            purchasedAtMs: NSNumber,
            expiresAtMs: NSNumber?
        ) {
            self.productId = productId
            self.transactionId = transactionId
            self.originalTransactionId = originalTransactionId
            self.purchasedAtMs = purchasedAtMs
            self.expiresAtMs = expiresAtMs
        }
    }

    // MARK: - Fetch products
    public func fetchPlans(productIds: [String], completion: @escaping ([Plan]?, String?) -> Void) {
        Task {
            do {
                let products = try await Product.products(for: productIds)
                let plans: [Plan] = products.map { p in
                    Plan(productId: p.id, title: p.displayName, price: p.displayPrice)
                }.sorted { $0.productId < $1.productId }
                completion(plans, nil)
            } catch {
                completion(nil, "fetchPlans error: \(error.localizedDescription)")
            }
        }
    }

    // MARK: - Purchase
    public func purchase(productId: String, completion: @escaping (PurchaseResult?, String?) -> Void) {
        Task {
            do {
                let products = try await Product.products(for: [productId])
                guard let product = products.first else {
                    completion(nil, "Product not found")
                    return
                }

                let result = try await product.purchase()

                switch result {
                case .success(let verification):
                    // VerificationResult<Transaction>
                    let transaction = try self.requireVerified(verification)
                    await transaction.finish()

                    let purchasedAtMs = NSNumber(value: Int64(transaction.purchaseDate.timeIntervalSince1970 * 1000))
                    let expiresAtMs: NSNumber?
                    if let exp = transaction.expirationDate {
                        expiresAtMs = NSNumber(value: Int64(exp.timeIntervalSince1970 * 1000))
                    } else {
                        expiresAtMs = nil
                    }

                    let payload = PurchaseResult(
                        productId: transaction.productID,
                        transactionId: String(transaction.id),
                        originalTransactionId: String(transaction.originalID),
                        purchasedAtMs: purchasedAtMs,
                        expiresAtMs: expiresAtMs
                    )

                    completion(payload, nil)

                case .userCancelled:
                    completion(nil, "USER_CANCELLED")

                case .pending:
                    completion(nil, "PENDING")

                @unknown default:
                    completion(nil, "UNKNOWN_RESULT")
                }
            } catch {
                completion(nil, "purchase error: \(error.localizedDescription)")
            }
        }
    }

    // MARK: - Restore (sync with App Store)
    public func restore(completion: @escaping (Bool, String?) -> Void) {
        Task {
            do {
                try await AppStore.sync()
                completion(true, nil)
            } catch {
                completion(false, "restore error: \(error.localizedDescription)")
            }
        }
    }

    // MARK: - Helpers
    private func requireVerified<T>(_ result: VerificationResult<T>) throws -> T {
        switch result {
        case .verified(let signed): return signed
        case .unverified(_, let error): throw error
        }
    }
}
