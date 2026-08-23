package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.mapper.toTutorialUrls
import uz.tikoncha_parent.data.remote.TutorialApiService
import uz.tikoncha_parent.data.remote.app_error.ApiErrorMapper
import uz.tikoncha_parent.domain.model.TutorialUrls
import uz.tikoncha_parent.domain.model.app_error.ErrorCause
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.TutorialRepository

class TutorialRepositoryImpl(private val api: TutorialApiService) : TutorialRepository {
    override suspend fun videoTutorials(): Outcome<TutorialUrls> =
        apiCall(TAG) {
            val r = api.videoTutorials()
            val body = r.data
            when {
                r.success && body != null -> Outcome.Success(body.toTutorialUrls())
                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
        }

    private companion object { const val TAG = "TutorialRepository" }
}