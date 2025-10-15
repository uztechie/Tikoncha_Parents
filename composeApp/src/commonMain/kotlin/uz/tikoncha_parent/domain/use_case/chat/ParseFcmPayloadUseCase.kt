package uz.tikoncha_parent.domain.use_case.chat

import kotlinx.serialization.json.Json
import uz.tikoncha_parent.domain.model.FcmPayload

class ParseFcmPayloadUseCase(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }
) {
    operator fun invoke(raw: String?): FcmPayload? {
        if (raw.isNullOrBlank()) return null
        return runCatching { json.decodeFromString(FcmPayload.serializer(), raw) }.getOrNull()
    }
}