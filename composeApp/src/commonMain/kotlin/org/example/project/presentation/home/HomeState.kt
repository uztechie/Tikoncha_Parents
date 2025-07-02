package org.example.project.presentation.home

data class HomeState(
    val counter:Int = 0,
    val list: List<String> = emptyList()
)
