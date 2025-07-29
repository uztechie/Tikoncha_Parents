package org.example.project.presentation.profile.personal_information

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class PersonalInformationItemData(
    val icon: DrawableResource,
    val title: StringResource,
    val value: String
)