package uz.tikoncha_parent.domain.use_case.push

import kotlinx.serialization.json.Json
import uz.tikoncha_parent.domain.model.push.FcmPayload


class ParseFcmPayloadUseCase {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }

    operator fun invoke(raw: String?): FcmPayload? {
        if (raw.isNullOrBlank()) return null
        return runCatching { json.decodeFromString(FcmPayload.serializer(), raw) }.getOrNull()
    }
}