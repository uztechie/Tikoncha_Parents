package org.example.project.presentation.home

import org.example.project.presentation.domain.model.UsagePeriod

sealed interface HomeEvent {

    data object Count: HomeEvent

    data class GetUsageList(val usagePeriod: UsagePeriod, val dateSelectionType: DateSelectionType): HomeEvent

    object OnChildSelectClicked: HomeEvent
    data class OnChildSelected(val child: String): HomeEvent
}