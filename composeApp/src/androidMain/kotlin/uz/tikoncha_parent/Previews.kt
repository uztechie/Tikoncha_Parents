package uz.tikoncha_parent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource

import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CloseButton
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.base.bottomShadow
import uz.tikoncha_parent.presentation.base.coverShadow
import uz.tikoncha_parent.presentation.base.verticalShadow
import uz.tikoncha_parent.presentation.home.schedule.ScheduleEvent
import uz.tikoncha_parent.presentation.home.schedule.ScheduleState
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleSetupTimeDialog
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeColumn
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeEvent
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeItem
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeState
import uz.tikoncha_parent.presentation.home.schedule.timelist.WeekdayChips
import uz.tikoncha_parent.presentation.home.schedule.type.ScheduleItem
import uz.tikoncha_parent.presentation.home.schedule.type.ScheduleType
import uz.tikoncha_parent.presentation.home.schedule.type.ScheduleTypeItem
import uz.tikoncha_parent.presentation.notification.NotificationScreen
import uz.tikoncha_parent.presentation.task.TaskEvent
import uz.tikoncha_parent.presentation.task.TaskState
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.DisableButtonColor
import uz.tikoncha_parent.ui.DisableButtonContentColor
import uz.tikoncha_parent.ui.DisableTextColor
import uz.tikoncha_parent.ui.HintTextColor
import uz.tikoncha_parent.ui.LargeTextSize
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.OnPrimaryColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.ShapeCornerRadius
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun ScheduleTypeUi(
    navigator: Navigator?,
    state: ScheduleTimeState,
    event: (ScheduleTimeEvent) -> Unit,

    ) {
    val bgColor = MaterialTheme.extendedColor.cardColor

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            title = stringResource(Res.string.jadval),
            showBackButton = true,
            onBackClick = {
//                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {

                Box(
                    modifier = Modifier
                        .size(NormalIconButtonSize)
                        .clip(RoundedCornerShape(ShapeCornerRadius))
                        .background(MaterialTheme.extendedColor.cardColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.edite_pen_ilne),
                        contentDescription = "Search",
                        tint = MaterialTheme.extendedColor.onBackgroundColor
                    )
                }
            }
        )

        SpaceMedium()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {
            CustomText(
                text = stringResource(Res.string.shartlar),
                fontSize = LargeTextSize,
                fontWeight = FontWeight.SemiBold
            )

            SpaceLarge()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalShadow(
                        shape = RoundedCornerShape(CardCornerRadius),
                        offset = 0.dp
                    )
                    .background(bgColor, RoundedCornerShape(CardCornerRadius))
                    .padding(horizontal = ContainerPadding, vertical = 12.dp),
            ) {
                Column(
                    modifier = Modifier
                        .clickable { }
                ) {
                    CustomText(
                        text = stringResource(Res.string.vaqt),
                        fontSize = LargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )
                    SpaceSmall()
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomText(
                            text = stringResource(Res.string.ishlash_vaqti),
                            fontSize = NormalTextSize,
                            color = HintTextColor
                        )
                        SpaceUltraSmall()
                        CustomText(
                            text = "9:00",
                            fontSize = LargeTextSize,
                            color = HintTextColor
                        )
                        SpaceUltraSmall()
                        CustomText(
                            text = "-",
                            color = HintTextColor,
                        )
                        SpaceUltraSmall()
                        CustomText(
                            text = "17:00",
                            fontSize = LargeTextSize,
                            color = HintTextColor
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                Image(
                    painter = painterResource(Res.drawable.close_circle),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.textColor),
                )
            }

            SpaceLarge()

            TextButton(
                onClick = {
//                    navController.navigate(Screen.ScheduleType)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PrimaryColor, RoundedCornerShape(ButtonCornerRadius))
                    .height(ButtonHeight),
            ) {
                Row {

                    Text(
                        text = stringResource(Res.string.shartlar_kiritish),
                        color = PrimaryColor
                    )

                    SpaceMedium()

                    Icon(
                        painter = painterResource(Res.drawable.add_square),
                        contentDescription = "",
                        tint = PrimaryColor
                    )
                }
            }

            SpaceMedium()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = stringResource(Res.string.qora_ro_yxat),
                    fontSize = LargeTextSize,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(
                    onClick = { }
                ) {
                    Image(
                        painter = painterResource(Res.drawable.arrow_down),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.textColor)
                    )
                }
            }

            CustomText(
                text = stringResource(Res.string.bloklamoqchi_bo_lgan_ilova_yoki_saytlarni_tanlang),
                fontSize = NormalTextSize,
                color = HintTextColor
            )

            SpaceLarge()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
//                        navController.navigate(Screen.ScheduleAppsSelect)
                    }
                    .verticalShadow(
                        shape = RoundedCornerShape(CardCornerRadius),
                        offset = 0.dp
                    )
                    .background(
                        bgColor, RoundedCornerShape(CardCornerRadius)
                    )
                    .padding(ContainerPadding)
            ) {
                val icons = listOf(
                    Res.drawable.instagram_icon,
                    Res.drawable.whatsapp_icon,
                    Res.drawable.discord_icon,
                    Res.drawable.linkedin_icon,
                    Res.drawable.social_x_icon,
                    Res.drawable.google_icon
                )

                val statusColor = if (icons.isEmpty()) HintTextColor else PrimaryColor
                val countText = icons.size.toString()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = stringResource(Res.string.ilovalar),
                        fontSize = LargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        painter = painterResource(Res.drawable.apps_icon),
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(SmallIconSize)
                    )
                    Spacer(Modifier.size(4.dp))
                    CustomText(
                        text = countText,
                        fontSize = NormalTextSize,
                        color = statusColor
                    )

                    SpaceUltraSmall()
                    Icon(
                        painter = painterResource(Res.drawable.arrow_right),
                        contentDescription = null,
                        modifier = Modifier.size(NormalIconSize),
                        tint = MaterialTheme.extendedColor.textColor
                    )
                }

                SpaceSmall()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    icons.forEach { icon ->
                        Image(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier.size(NormalIconSize)
                        )
                    }
                }
            }
            SpaceLarge()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalShadow(
                        shape = RoundedCornerShape(CardCornerRadius),
                        offset = 0.dp
                    )
                    .background(
                        bgColor, RoundedCornerShape(CardCornerRadius)
                    )
            ) {
                val sayt = listOf(
                    "Instagram_com",
                    "Whatsapp_com",
                    "Discord_com",
                    "Linkedin_com",
                    "Social_x_com",
                    "Google_com"
                )

                val statusColor = if (sayt.isEmpty()) HintTextColor else PrimaryColor
                val countText = sayt.size.toString()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = ContainerPadding,
                            end = ContainerPadding,
                            top = ContainerPadding
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    CustomText(
                        text = stringResource(Res.string.veb_sayt),
                        fontSize = LargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        painter = painterResource(Res.drawable.globuse),
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(SmallIconSize)
                    )
                    Spacer(Modifier.size(4.dp))
                    CustomText(
                        text = countText,
                        fontSize = NormalTextSize,
                        color = statusColor
                    )

                    SpaceUltraSmall()
                    Icon(
                        painter = painterResource(Res.drawable.arrow_right),
                        contentDescription = null,
                        modifier = Modifier.size(NormalIconSize),
                        tint = MaterialTheme.extendedColor.textColor
                    )
                }

                SpaceSmall()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = ContainerPadding, bottom = ContainerPadding)
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    sayt.forEach { sayt ->
                        Column(
                            modifier = Modifier
                                .verticalShadow(
                                    shape = RoundedCornerShape(CardCornerRadius),
                                    offset = 0.dp
                                )
                                .background(
                                    MaterialTheme.extendedColor.backgroundColor,
                                    RoundedCornerShape(CardCornerRadius)
                                )
                                .padding(horizontal = 8.dp)
                        ) {
                            CustomText(
                                text = sayt,
                                fontSize = SmallTextSize,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            CustomButton(
                text = stringResource(Res.string.saqlash),
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = true
            )
            SpaceSmall()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        ScheduleTypeUi(
            navigator = null,
            state = ScheduleTimeState(),
            event = {},
        )
    }
}