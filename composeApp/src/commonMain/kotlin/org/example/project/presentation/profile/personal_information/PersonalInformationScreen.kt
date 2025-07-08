package org.example.project.presentation.profile.personal_information

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.common.CustomOutlinedButton
import org.example.project.presentation.common.CustomText
import org.example.project.presentation.domain.model.Child
import org.example.project.presentation.profile.CustomHeader
import org.example.project.presentation.profile.ProfileHeader
import org.example.project.ui.AppIconInnerPadding
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ButtonHeight
import org.example.project.ui.ContainerPadding
import org.example.project.ui.NormalLargeTextSize
import org.example.project.ui.NormalTextSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.ShapeCornerRadius
import org.example.project.ui.SpaceLarge
import org.example.project.ui.SpaceMedium
import org.example.project.ui.SpaceSmall
import org.example.project.ui.SpaceUltraSmall
import org.example.project.ui.TextColor
import org.example.project.ui.TonalButtonContainerColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.add_square
import tikoncha_parents.composeapp.generated.resources.calendar
import tikoncha_parents.composeapp.generated.resources.class_icon
import tikoncha_parents.composeapp.generated.resources.id_card
import tikoncha_parents.composeapp.generated.resources.phone
import tikoncha_parents.composeapp.generated.resources.profile
import tikoncha_parents.composeapp.generated.resources.school_icon
import tikoncha_parents.composeapp.generated.resources.shift_clock
import tikoncha_parents.composeapp.generated.resources.two_users
import kotlin.toString

class PersonalInformationScreen: Screen {
    @Composable
    override fun Content() {
        PersonalInformationScreen(
            navigator = null
        )
    }
}

@Composable
fun PersonalInformationScreen(
    navigator: Navigator?
){

    val childrenList = remember {
        mutableStateListOf(
            Child(
                fullName = "Ergashev Ergashtir Abdukarim o'g'li",
                age = 16,
                gender = "Erkak",
                phoneNumber = "+998 99 123 34 23",
                school = "Andijon shahar, 13-maktab",
                className = "10-A",
                shift = "2-smena"
            ),
            Child(
                fullName = "Kettiyev Keldi Abdukarim o'g'li",
                age = 16,
                gender = "Erkak",
                phoneNumber = "+998 91 269 48 09",
                school = "Andijon shahar, 9-maktab",
                className = "10-W",
                shift = "5-smena"
            )
        )
    }

    val personalInformationItems by remember() {
        derivedStateOf {
            listOf(
                PersonalInformationItemData(
                    icon = Res.drawable.profile,
                    title = "Ism",
                    value = "Shuxratov Saidburxon"
                ),
                PersonalInformationItemData(
                    icon = Res.drawable.phone,
                    title = "Telefon nomer",
                    value = "+998 99 123 54 67"
                ),
                PersonalInformationItemData(
                    icon = Res.drawable.two_users,
                    title = "Qarindoshligi",
                    value = "Otasi"
                ),
                PersonalInformationItemData(
                    icon = Res.drawable.id_card,
                    title = "Pasport ID",
                    value = "AD 125378029"
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
    ) {
        CustomHeader(
            title = "Shaxsiy ma'lumotlar",
            showBackButton = true,
            onBackClick = {
                navigator!!.pop()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {

            ProfileHeader(
                fullName = "Shuxratov Saidburxon",
                fathersName = "",
                onSelectImageButtonClick = {},
                image = null
            )

            SpaceLarge()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(ShapeCornerRadius))
                    .background(TonalButtonContainerColor)
                    .padding(horizontal = AppIconInnerPadding, vertical = ContainerPadding)
            ) {

                CustomText(
                    text = "Shaxsiy ma'lumotlar",
                    color = TextColor,
                    fontSize = NormalLargeTextSize,
                    fontWeight = FontWeight.SemiBold
                )

                SpaceSmall()

                personalInformationItems.forEach { item ->

                    PersonalInformationItem(
                        icon = item.icon,
                        title = item.title,
                        value = item.value.toString()
                    )

                    SpaceUltraSmall()

                }
            }

            SpaceSmall()

            CustomText(
                text = "Farzandlaringiz",
                fontSize = NormalLargeTextSize,
                color = TextColor,
                fontWeight = FontWeight.SemiBold
            )

            SpaceSmall()

            childrenList.forEach {child ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(ShapeCornerRadius))
                        .background(TonalButtonContainerColor)
                        .padding(horizontal = AppIconInnerPadding, vertical = ContainerPadding)
                ){

                    val listOfPersonalInformationData = remember {
                        mutableStateListOf(
                            PersonalInformationItemData(
                                icon = Res.drawable.profile,
                                title = "Ism",
                                value = child.fullName
                            ),
                            PersonalInformationItemData(
                                icon = Res.drawable.calendar,
                                title = "Yosh",
                                value = "${child.age}-yosh"
                            ),
                            PersonalInformationItemData(
                                icon = Res.drawable.two_users,
                                title = "Jins",
                                value = child.gender
                            ),
                            PersonalInformationItemData(
                                icon = Res.drawable.phone,
                                title = "Telefon nomer",
                                value = child.phoneNumber
                            ),
                        )
                    }

                    val listOfSchoolData = remember {
                        mutableStateListOf(
                            PersonalInformationItemData(
                                icon = Res.drawable.school_icon,
                                title = "Maktab",
                                value = child.school
                            ),
                            PersonalInformationItemData(
                                icon = Res.drawable.class_icon,
                                title = "Sinf",
                                value = child.className
                            ),
                            PersonalInformationItemData(
                                icon = Res.drawable.shift_clock,
                                title = "Smena",
                                value = child.shift
                            )
                        )
                    }

                    CustomText(
                        text = "Shaxsiy ma'lumotlar",
                        color = TextColor,
                        fontSize = NormalLargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )

                    SpaceSmall()

                    listOfPersonalInformationData.forEach { item ->

                        PersonalInformationItem(
                            icon = item.icon,
                            title = item.title,
                            value = item.value.toString()
                        )

                        SpaceUltraSmall()

                    }

                    SpaceSmall()

                    CustomText(
                        text = "Maktab",
                        color = TextColor,
                        fontSize = NormalLargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )

                    SpaceSmall()

                    listOfSchoolData.forEach { item ->

                        PersonalInformationItem(
                            icon = item.icon,
                            title = item.title,
                            value = item.value.toString()
                        )

                        SpaceUltraSmall()

                    }

                }

                SpaceMedium()

            }

            CustomOutlinedButton(
                text = "Farzand qo'shish",
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
                onClick = {

                },
                textColor = PrimaryColor
            )

        }

    }
}

@Preview
@Composable
private fun PreviewPersonalInformationScreen(){
    PersonalInformationScreen(
        navigator = null
    )
}