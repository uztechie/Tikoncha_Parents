package uz.tikoncha_parent.domain.model.policy

import kotlinx.serialization.Serializable

@Serializable
enum class PolicyAction {
    ALLOW, DENY;

    companion object{
        fun valueToPolicyAction(value: String): PolicyAction{
            return when(value){
                "ALLOW" -> PolicyAction.ALLOW
                "DENY" -> PolicyAction.DENY
                else -> PolicyAction.DENY
            }
        }


    }
}