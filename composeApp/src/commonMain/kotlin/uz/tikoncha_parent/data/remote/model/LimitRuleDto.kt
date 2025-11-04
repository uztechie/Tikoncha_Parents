package uz.tikoncha_parent.data.remote.model

data class LimitRuleDto(
    val limit_amount: Int,
    val limit_type: String,
    val days: List<Int>
)
