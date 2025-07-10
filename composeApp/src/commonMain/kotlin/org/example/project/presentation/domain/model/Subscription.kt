package org.example.project.presentation.domain.model

data class Subscription(
    val title: String,
    val price: String,
    var isSelected: Boolean = false
)