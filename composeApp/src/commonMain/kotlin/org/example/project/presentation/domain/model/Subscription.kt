package org.example.project.presentation.domain.model

data class Subscription(
    val title: String,
    val price: Int,
    var isSelected: Boolean = false
)