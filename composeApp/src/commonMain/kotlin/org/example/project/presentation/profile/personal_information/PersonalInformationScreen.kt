package org.example.project.presentation.profile.personal_information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.add_child.ChildScreen
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.CustomOutlinedButton
import org.example.project.presentation.profile.ProfileHeader
import org.example.project.ui.AppIconInnerPadding
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ButtonHeight
import org.example.project.ui.ContainerPadding
import org.example.project.ui.NormalLargeTextSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.ShapeCornerRadius
import org.example.project.ui.SpaceLarge
import org.example.project.ui.SpaceMedium
import org.example.project.ui.SpaceSmall
import org.example.project.ui.SpaceUltraSmall
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.add_square
import tikoncha_parents.composeapp.generated.resources.calendar
import tikoncha_parents.composeapp.generated.resources.class_icon
import tikoncha_parents.composeapp.generated.resources.farzand_qo_shish
import tikoncha_parents.composeapp.generated.resources.farzandingiz
import tikoncha_parents.composeapp.generated.resources.id_card
import tikoncha_parents.composeapp.generated.resources.*
import tikoncha_parents.composeapp.generated.resources.maktab
import tikoncha_parents.composeapp.generated.resources.phone
import tikoncha_parents.composeapp.generated.resources.profile
import tikoncha_parents.composeapp.generated.resources.school_icon
import tikoncha_parents.composeapp.generated.resources.shaxsiy_malumotlar
import tikoncha_parents.composeapp.generated.resources.shift_clock
import tikoncha_parents.composeapp.generated.resources.two_users
import uz.saidburxon.newedu.presentation.base.CustomText

class PersonalInformationScreen: Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        val rootNavigator = navigator?.parent

        val viewModel = koinViewModel<PersonalInformationViewModel>()

        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        PersonalInformationUi(
            state = state.value,
            event = event
        )
    }
}

@Composable
fun PersonalInformationUi(
    state: PersonalInformationState,
    event: (PersonalInformationEvent) -> Unit
){
    val rootNavigator = LocalNavigator.current


    val personalInformationItems by remember() {
        derivedStateOf {
            listOf(
                PersonalInformationItemData(
                    icon = Res.drawable.profile,
                    title = Res.string.ism,
                    value = state.fullName
                ),
                PersonalInformationItemData(
                    icon = Res.drawable.phone,
                    title = Res.string.telefon_nomer,
                    value = state.phoneNumber
                ),
                PersonalInformationItemData(
                    icon = Res.drawable.two_users,
                    title = Res.string.qarindoshligi,
                    value = state.relativity
                ),
                PersonalInformationItemData(
                    icon = Res.drawable.id_card,
                    title = Res.string.pasport_id,
                    value = state.passportNumber
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CustomHeader(
            title = stringResource(Res.string.shaxsiy_malumotlar),
            showBackButton = true,
            onBackClick = {
                rootNavigator?.pop()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            ProfileHeader(
                fullName = state.fullName,
                fathersName = "",
                onSelectImageButtonClick = {
                    event(PersonalInformationEvent.OnChangeProfilePhotoClicked(null))
                },
                image = state.profileImage
            )

            SpaceLarge()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(ShapeCornerRadius))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(horizontal = AppIconInnerPadding, vertical = ContainerPadding)
            ) {

                CustomText(
                    text = stringResource(Res.string.shaxsiy_malumotlar),
                    fontSize = NormalLargeTextSize,
                    fontWeight = FontWeight.SemiBold
                )

                SpaceSmall()

                personalInformationItems.forEach { item ->

                    PersonalInformationItem(
                        icon = item.icon,
                        title = stringResource(item.title),
                        value = item.value.toString()
                    )

                    SpaceUltraSmall()
                }
            }

            SpaceSmall()

            CustomText(
                text = stringResource(Res.string.farzandingiz),
                fontSize = NormalLargeTextSize,
                fontWeight = FontWeight.SemiBold
            )

            SpaceSmall()

            state.childrenData.forEach {child ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(ShapeCornerRadius))
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(horizontal = AppIconInnerPadding, vertical = ContainerPadding)
                ){

                    val listOfPersonalInformationData = remember {
                        mutableStateListOf(
                            PersonalInformationItemData(
                                icon = Res.drawable.profile,
                                title = Res.string.ism,
                                value = child.fullName
                            ),
                            PersonalInformationItemData(
                                icon = Res.drawable.calendar,
                                title = Res.string.yosh,
                                value = "${child.age}-yosh"
                            ),
                            PersonalInformationItemData(
                                icon = Res.drawable.two_users,
                                title = Res.string.jins,
                                value = child.gender
                            ),
                            PersonalInformationItemData(
                                icon = Res.drawable.phone,
                                title = Res.string.telefon_nomer,
                                value = child.phoneNumber
                            ),
                        )
                    }

                    val listOfSchoolData = remember {
                        mutableStateListOf(
                            PersonalInformationItemData(
                                icon = Res.drawable.school_icon,
                                title = Res.string.maktab,
                                value = child.school
                            ),
                            PersonalInformationItemData(
                                icon = Res.drawable.class_icon,
                                title = Res.string.sinf,
                                value = child.className
                            ),
                            PersonalInformationItemData(
                                icon = Res.drawable.shift_clock,
                                title = Res.string.smena,
                                value = child.shift
                            )
                        )
                    }

                    CustomText(
                        text = stringResource(Res.string.shaxsiy_malumotlar),
                        fontSize = NormalLargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )

                    SpaceSmall()

                    listOfPersonalInformationData.forEach { item ->

                        PersonalInformationItem(
                            icon = item.icon,
                            title = stringResource(item.title),
                            value = item.value.toString()
                        )

                        SpaceUltraSmall()
                    }

                    SpaceSmall()

                    CustomText(
                        text = stringResource(Res.string.maktab),
                        fontSize = NormalLargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )

                    SpaceSmall()

                    listOfSchoolData.forEach { item ->

                        PersonalInformationItem(
                            icon = item.icon,
                            title = stringResource(item.title),
                            value = item.value.toString()
                        )

                        SpaceUltraSmall()
                    }
                }
                SpaceMedium()
            }

            CustomOutlinedButton(
                text = stringResource(Res.string.farzand_qo_shish),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                endingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.add_square),
                        contentDescription = "",
                        tint = PrimaryColor
                    )
                },
                onClick = { rootNavigator?.push(ChildScreen())},
                textColor = PrimaryColor
            )
            SpaceLarge()
        }
    }
}

@Preview
@Composable
private fun PreviewPersonalInformationScreen(){
    PersonalInformationUi(
        state = PersonalInformationState(),
        event = {}
    )
}