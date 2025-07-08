package org.example.project.presentation.profile.personal_information

import org.jetbrains.compose.resources.DrawableResource

data class PersonalInformationItemData(
    val icon: DrawableResource,
    val title: String,
    val value: String
)