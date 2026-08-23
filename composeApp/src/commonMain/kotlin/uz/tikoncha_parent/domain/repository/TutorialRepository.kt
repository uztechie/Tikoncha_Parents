package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.domain.model.TutorialUrls
import uz.tikoncha_parent.domain.model.app_error.Outcome

interface TutorialRepository {
    suspend fun videoTutorials(): Outcome<TutorialUrls>
}