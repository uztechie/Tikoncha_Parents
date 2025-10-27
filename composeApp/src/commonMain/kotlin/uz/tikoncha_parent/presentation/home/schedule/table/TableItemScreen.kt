package uz.tikoncha_parent.presentation.home.schedule.table

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun TableItemScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            title = stringResource(Res.string.jadvallar),
            showBackButton = true,
            onBackClick = { },
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
                fontSize = UltraLargeTextSize,
                fontWeight = FontWeight.SemiBold
            )

            SpaceLarge()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp, MaterialTheme.extendedColor.borderColor, RoundedCornerShape(
                            TextFieldCornerRadius
                        )
                    )
                    .padding(horizontal = ContainerPadding, vertical = 12.dp),
            ) {
                Column() {
                    CustomText(
                        text = stringResource(Res.string.vaqt),
                        fontSize = UltraLargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )
                    SpaceSmall()
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomText(
                            text = stringResource(Res.string.budilnik),
                            fontSize = LargeTextSize,
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

            SpaceMedium()

            TextButton(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PrimaryColor, RoundedCornerShape(TextFieldCornerRadius))
                    .height(ButtonHeight),
            ) {

                Text(
                    text = stringResource(Res.string.vazifa_qo_shish),
                    fontSize = 16.sp,
                    color = PrimaryColor
                )

                SpaceMedium()

                Icon(
                    painter = painterResource(Res.drawable.add_square),
                    contentDescription = "",
                    tint = PrimaryColor
                )
            }

            SpaceMedium()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = stringResource(Res.string.qora_ro_yxat),
                    fontSize = UltraLargeTextSize,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(
                    onClick = { }
                ) {
                    Image(
                        painter = painterResource(Res.drawable.arrow_down),
                        contentDescription = null
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
                    .border(
                        1.dp,
                        MaterialTheme.extendedColor.borderColor,
                        RoundedCornerShape(TextFieldCornerRadius)
                    )
                    .padding(ContainerPadding)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = stringResource(Res.string.ilovalar),
                        fontSize = UltraLargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        painter = painterResource(Res.drawable.apps_icon),
                        contentDescription = null,
                        tint = PrimaryColor,
                        modifier = Modifier.size(SmallIconSize)
                    )
                    Spacer(Modifier.size(4.dp))
                    CustomText(
                        text = "6",
                        fontSize = LargeTextSize,
                        color = PrimaryColor
                    )

                    SpaceUltraSmall()
                    Icon(
                        painter = painterResource(Res.drawable.arrow_right),
                        contentDescription = null,
                        modifier = Modifier.size(NormalIconSize)
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

                    val icons = listOf(
                        Res.drawable.instagram_icon,
                        Res.drawable.whatsapp_icon,
                        Res.drawable.discord_icon,
                        Res.drawable.linkedin_icon,
                        Res.drawable.social_x_icon,
                        Res.drawable.google_icon
                    )

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
                    .border(
                        1.dp,
                        MaterialTheme.extendedColor.borderColor,
                        RoundedCornerShape(TextFieldCornerRadius)
                    )
            ) {
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
                        fontSize = UltraLargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        painter = painterResource(Res.drawable.globuse),
                        contentDescription = null,
                        tint = PrimaryColor,
                        modifier = Modifier.size(SmallIconSize)
                    )
                    Spacer(Modifier.size(4.dp))
                    CustomText(
                        text = "6",
                        fontSize = LargeTextSize,
                        color = PrimaryColor
                    )

                    SpaceUltraSmall()
                    Icon(
                        painter = painterResource(Res.drawable.arrow_right),
                        contentDescription = null,
                        modifier = Modifier.size(NormalIconSize)
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

                    val sayt = listOf(
                        "Instagram_com",
                        "Whatsapp_com",
                        "Discord_com",
                        "Linkedin_com",
                        "Social_x_com",
                        "Google_com"
                    )

                    sayt.forEach { sayt ->

                        Box(
                            modifier = Modifier
                                .background(
                                    LightGrayColor,
                                    RoundedCornerShape(ContainerCornerRadius)
                                )
                                .padding(horizontal = 8.dp)
                        ) {
                            CustomText(
                                text = sayt,
                                fontSize = NormalTextSize,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            CustomButton(
                text = stringResource(Res.string.saqlash),
                fontSize = NormalLargeTextSize,
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
private fun PreviewTableScreen() {
    TableItemScreen()
}