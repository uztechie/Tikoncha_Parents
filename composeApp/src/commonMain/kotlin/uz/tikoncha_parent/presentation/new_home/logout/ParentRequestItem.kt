package uz.tikoncha_parent.presentation.new_home.logout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bekor_qilish
import tikoncha_parents.composeapp.generated.resources.hisobdan_chiqish
import tikoncha_parents.composeapp.generated.resources.hisobdan_chiqish_uchun_sorov
import tikoncha_parents.composeapp.generated.resources.ilovani_ochirish
import tikoncha_parents.composeapp.generated.resources.ilovani_ochirish_uchun_sorov_kerak
import tikoncha_parents.composeapp.generated.resources.tasdiqlash
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.DividerHorizontal
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.OtpErrorColor
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.SuccessColor
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun ParentRequestItem(
    parentRequestUi: ParentRequestUi,
    onActionClick: () -> Unit = {},
    onCancelRequestClick: () -> Unit = {},
) {

    val text = when(parentRequestUi.type){
        ParentRequestType.LOGOUT -> {
            stringResource(Res.string.hisobdan_chiqish)
        }
        ParentRequestType.DELETE -> {
            stringResource(Res.string.ilovani_ochirish)
        }
    }

    val subtitle = when(parentRequestUi.type){
        ParentRequestType.LOGOUT -> {
            stringResource(Res.string.hisobdan_chiqish_uchun_sorov)
        }
        ParentRequestType.DELETE -> {
            stringResource(Res.string.ilovani_ochirish_uchun_sorov_kerak)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(CardCornerRadius))
            .padding(ContainerPadding),
    )
    {
        Row{

//                Image(
//                    painter = painterResource(parentRequestUi.type.iconId),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .align(Alignment.CenterVertically)
//                        .size(NormalIconSize),
//                    colorFilter = ColorFilter.tint(OtpErrorColor)
//                )
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = text,
                        fontSize = NormalTextSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                    )
                    SpaceSmall()
                    CustomText(
                        text = parentRequestUi.childName,
                        color = MaterialTheme.extendedColor.primaryColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                SpaceSmall()
                CustomText(
                    text = subtitle,
                    fontSize = SmallTextSize,
                    color = MaterialTheme.extendedColor.hintColor
                )
            }
        }

        SpaceSmall()
        Row(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            CustomText(
                text = parentRequestUi.createdAt,
                color = MaterialTheme.extendedColor.hintColor
            )
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .background(
                        parentRequestUi.status.color.copy(0.4f),
                        RoundedCornerShape(CardCornerRadius)
                    )
                    .padding(horizontal = 8.dp, vertical = 0.dp)
            ) {
                CustomText(
                    text = stringResource(parentRequestUi.status.title),
                    fontSize = SmallTextSize,
                    color = parentRequestUi.status.color
                )
            }

        }


        SpaceMedium()
        DividerHorizontal()
        SpaceMedium()


        if (parentRequestUi.status == ParentRequestStatus.PROCESS){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {

                CustomOutlinedButton(
                    text = stringResource(Res.string.bekor_qilish),
                    containerColor = Color.Transparent,
                    borderColor = OtpErrorColor,
                    contentColor = OtpErrorColor,
                    onClick = onCancelRequestClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(DialogButtonHeight),
                )
                SpaceSmall()
                CustomOutlinedButton(
                    borderColor = SuccessColor,
                    contentColor = SuccessColor,
                    containerColor = Color.Transparent,
                    text = stringResource(Res.string.tasdiqlash),
                    onClick = onActionClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DialogButtonHeight)
                        .weight(1f),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        ParentRequestItem(
            parentRequestUi = ParentRequestUi(
                type = ParentRequestType.DELETE,
                requestId = "",
                childName = "Abdurahimjonbek",
                createdAt = "10.12.2025"
            ),
        )
    }
}