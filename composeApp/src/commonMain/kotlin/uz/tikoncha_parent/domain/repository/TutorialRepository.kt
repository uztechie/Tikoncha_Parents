package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.tutorial.VideoTutorialResponse

interface TutorialRepository {
    suspend fun videoTutorials(): VideoTutorialResponse
}