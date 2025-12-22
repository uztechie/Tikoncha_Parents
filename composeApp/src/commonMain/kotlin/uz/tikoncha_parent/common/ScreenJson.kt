package uz.tikoncha_parent.common

import kotlinx.serialization.json.Json

object ScreenJson {
    val fmt: Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
        isLenient = true
        coerceInputValues = true
    }

    inline fun <reified T> encode(value: T): String = fmt.encodeToString(
        serializer = kotlinx.serialization.serializer(),
        value = value
    )

    inline fun <reified T> decode(json: String?): T? {
        if (json.isNullOrBlank()) return null
        return runCatching { fmt.decodeFromString<T>(json) }.getOrNull()
    }
}