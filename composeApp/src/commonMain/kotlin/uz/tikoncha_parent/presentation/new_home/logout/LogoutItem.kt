package uz.tikoncha_parent.presentation.new_home.logout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bekor_qilish
import tikoncha_parents.composeapp.generated.resources.hisobdan_chiqish
import tikoncha_parents.composeapp.generated.resources.hisobdan_chiqish_uchun_sorov
import tikoncha_parents.composeapp.generated.resources.ilovani_ochirish
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.new_home.logout.LogoutType.*
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.NormalIconSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.OtpErrorColor
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun LogoutItem(
    logoutUi: LogoutUi,
    onActionClick: () -> Unit = {},
    onSendRequestClick: () -> Unit = {},
    onCheckStatusClick: () -> Unit = {},
    onCancelRequestClick: () -> Unit = {},
) {

    val text = when(logoutUi.type){
        LogoutType.LOGOUT -> {
            stringResource(Res.string.hisobdan_chiqish)
        }
        LogoutType.DELETE -> {
            stringResource(Res.string.ilovani_ochirish)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColor.backgroundColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.extendedColor.cardColor,
                    RoundedCornerShape(CardCornerRadius)
                )
                .padding(ContainerPadding),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(logoutUi.type.iconId),
                    contentDescription = "",
                    modifier = Modifier.size(NormalIconSize),
                    colorFilter = ColorFilter.tint(OtpErrorColor)
                )
                SpaceMedium()
                Column {
                    CustomText(
                        text = text,
                        fontSize = NormalTextSize,
                        fontWeight = FontWeight.Bold
                    )

                    SpaceSmall()
                    CustomText(
                        text = stringResource(Res.string.hisobdan_chiqish_uchun_sorov),
                        fontSize = SmallTextSize,
                        color = MaterialTheme.extendedColor.hintColor
                    )
                }
            }

            SpaceLarge()

            when(logoutUi.type){

                LOGOUT -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        CustomOutlinedButton(
                            text = stringResource(Res.string.bekor_qilish),
                            backgroundColor = Color.Transparent,
                            borderColor = OtpErrorColor,
                            textColor = OtpErrorColor,
                            onClick = onCancelRequestClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(DialogButtonHeight),
                        )
                        SpaceSmall()
                        CustomOutlinedButton(
                            backgroundColor = Color.Transparent,
                            text = "Tasdiqlash",
                            onClick = onCheckStatusClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(DialogButtonHeight)
                                .weight(1f),
                        )
                    }
                }
                DELETE -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        CustomOutlinedButton(
                            text = stringResource(Res.string.bekor_qilish),
                            backgroundColor = Color.Transparent,
                            borderColor = OtpErrorColor,
                            textColor = OtpErrorColor,
                            onClick = onCancelRequestClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(DialogButtonHeight),
                        )
                        SpaceSmall()
                        CustomOutlinedButton(
                            backgroundColor = Color.Transparent,
                            text = "Tasdiqlash",
                            onClick = onCheckStatusClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(DialogButtonHeight)
                                .weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        LogoutItem(
            logoutUi = LogoutUi(
                type = LogoutType.DELETE,
                requestId = ""
            ),
        )
    }
}