package org.example.project.presentation.completedTask

import org.example.project.presentation.register.RegisterEvent

sealed class CompletedTaskEvent {
    data class OnGenderSelected(val genderIndex: Int): CompletedTaskEvent()

}