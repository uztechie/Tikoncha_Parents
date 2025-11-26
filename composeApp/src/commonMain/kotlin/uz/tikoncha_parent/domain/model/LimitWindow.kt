package uz.tikoncha_parent.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class LimitWindow{
    DAILY, HOURLY
}
