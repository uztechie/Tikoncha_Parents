package uz.tikoncha_parent.domain.use_case.chat

import uz.tikoncha_parent.data.remote.model.SendMessageRequest
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.ChatRepository

class SendMessageApiUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(request: SendMessageRequest): Outcome<Unit> =
        repository.sendMessageApi(request)
}