package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.TutorialApiService
import uz.tikoncha_parent.data.remote.model.tutorial.VideoTutorialResponse
import uz.tikoncha_parent.domain.repository.TutorialRepository

class TutorialRepositoryImpl(private val api: TutorialApiService): TutorialRepository {
    override suspend fun videoTutorials(): VideoTutorialResponse {
        return api.videoTutorials()
    }
}