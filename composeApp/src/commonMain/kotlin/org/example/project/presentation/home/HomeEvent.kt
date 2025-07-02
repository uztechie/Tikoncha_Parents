package org.example.project.presentation.home

sealed interface HomeEvent {

    data object Count: HomeEvent
}