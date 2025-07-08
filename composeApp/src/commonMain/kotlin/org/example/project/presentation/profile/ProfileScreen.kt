package org.example.project.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.common.CustomOutlinedButton
import org.example.project.presentation.profile.personal_information.PersonalInformationScreen
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ContainerPadding
import org.example.project.ui.PrimaryColor
import org.example.project.ui.ProfileStatsContainerHeight
import org.example.project.ui.SpaceLarge
import org.example.project.ui.SpaceMedium
import org.example.project.ui.SpaceSmall
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.coin
import tikoncha_parents.composeapp.generated.resources.coins
import tikoncha_parents.composeapp.generated.resources.file
import tikoncha_parents.composeapp.generated.resources.global
import tikoncha_parents.composeapp.generated.resources.profile
import tikoncha_parents.composeapp.generated.resources.scan
import tikoncha_parents.composeapp.generated.resources.settings
import tikoncha_parents.composeapp.generated.resources.telegrams_star

class ProfileScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        ProfileScreen(
            navigator = navigator
        )
    }
}

@Composable
fun ProfileScreen(
    navigator: Navigator?
){

    val sections = remember {
        mutableStateListOf(
            ProfileSectionItemData(
                painter = Res.drawable.profile,
                section = ProfileSection.PERSONAL_INFORMATION
            ),
            ProfileSectionItemData(
                painter = Res.drawable.global,
                section = ProfileSection.LANGUAGE
            ),
            ProfileSectionItemData(
                painter = Res.drawable.settings,
                section = ProfileSection.SETTINGS
            ),
            ProfileSectionItemData(
                painter = Res.drawable.telegrams_star,
                section = ProfileSection.SUBSCRIPTIONS
            ),
            ProfileSectionItemData(
                painter = Res.drawable.coins,
                section = ProfileSection.COINS
            ),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        CustomHeader(
            title = "Profil",
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {


            ProfileHeader(
                fullName = "Shuxratov Saidburxon",
                fathersName = "",
                image = null,
                onSelectImageButtonClick = {

                }
            )

            SpaceLarge()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {

                UserStatsItem(
                    title = "Farzandingiz tangalari",
                    value = "0 ta",
                    icon = painterResource(Res.drawable.coin),
                    modifier = Modifier
                        .height(ProfileStatsContainerHeight)
                        .weight(1f)
                )

                SpaceSmall()

                UserStatsItem(
                    title = "Farzandingiz bajarilmagan vazifalari",
                    value = "0 ta",
                    icon = painterResource(Res.drawable.file),
                    modifier = Modifier
                        .height(ProfileStatsContainerHeight)
                        .weight(1f)
                )
            }

            SpaceLarge()

            sections.forEach { data ->

                SpaceSmall()

                ProfileSectionItem(
                    icon = painterResource(data.painter),
                    section = data.section,
                    onItemClick = { section ->
                        when (section) {

                            ProfileSection.PERSONAL_INFORMATION -> {
                                navigator!!.push(PersonalInformationScreen())
                            }

                            ProfileSection.LANGUAGE -> {

                            }

                            ProfileSection.SETTINGS -> {

                            }

                            ProfileSection.SUBSCRIPTIONS -> {

                            }

                            ProfileSection.COINS -> {

                            }
                        }
                    }
                )
            }

            SpaceLarge()
            SpaceLarge()

            CustomOutlinedButton(
                text = "Qr kod",
                onClick = {

                },
                textColor = PrimaryColor,
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.scan),
                        contentDescription = ""
                    )
                },
                modifier = Modifier.width(130.dp)
            )
        }
    }

}

@Preview
@Composable
private fun PreviewProfileScreen(){1
    ProfileScreen(
        navigator = null
    )
}
