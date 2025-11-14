package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.PaymentApiService
import uz.tikoncha_parent.data.remote.RulesApiService
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequestTemp
import uz.tikoncha_parent.data.remote.model.CreatePolicyResponseTemp
import uz.tikoncha_parent.data.remote.model.CreateRuleRequest
import uz.tikoncha_parent.data.remote.model.CreateRuleResponse
import uz.tikoncha_parent.data.remote.model.GetRulesResponse
import uz.tikoncha_parent.data.remote.model.SubscriptionPurchaseRequest
import uz.tikoncha_parent.data.remote.model.SubscriptionPurchaseResponse
import uz.tikoncha_parent.data.remote.model.UpsertRuleRequest
import uz.tikoncha_parent.data.remote.model.UpsertRuleResponse
import uz.tikoncha_parent.domain.repository.PaymentRepository
import uz.tikoncha_parent.domain.repository.RulesRepository

class PaymentRepositoryImpl(private val api: PaymentApiService): PaymentRepository {
    override suspend fun subscriptionPurchase(subscriptionPurchaseRequest: SubscriptionPurchaseRequest): SubscriptionPurchaseResponse {
        return api.subscriptionPurchase(subscriptionPurchaseRequest)
    }


}