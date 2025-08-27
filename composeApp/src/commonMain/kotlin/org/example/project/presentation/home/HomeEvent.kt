package org.example.project.presentation.home

import org.example.project.presentation.domain.model.UsagePeriod

sealed interface HomeEvent {
    data class GetUsageList(val usagePeriod: UsagePeriod, val dateSelectionType: DateSelectionType): HomeEvent

    object OnChildSelectClicked: HomeEvent
    data class OnChildSelected(val child: String): HomeEvent
    data class OnLockClicked(val appUsageUi: AppUsageUi): HomeEvent
}