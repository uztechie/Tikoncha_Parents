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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bekor_qilish
import tikoncha_parents.composeapp.generated.resources.farzand_ilovadan_chiqish_sorovi
import tikoncha_parents.composeapp.generated.resources.farzand_ilovani_ochirish_sorovi
import tikoncha_parents.composeapp.generated.resources.hisobdan_chiqish
import tikoncha_parents.composeapp.generated.resources.hisobdan_chiqish_uchun_sorov
import tikoncha_parents.composeapp.generated.resources.ilovani_ochirish
import tikoncha_parents.composeapp.generated.resources.ilovani_ochirish_uchun_sorov_kerak
import tikoncha_parents.composeapp.generated.resources.tasdiqlash
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.DividerHorizontal
import uz.tikoncha_parent.ui.OtpErrorColor
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.SuccessColor
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun ParentRequestItem(
    parentRequestUi: ParentRequestUi,
    onActionClick: () -> Unit = {},
    onCancelRequestClick: () -> Unit = {},
) {

    val requestTitle = when(parentRequestUi.type){
        ParentRequestType.LOGOUT -> {
            stringResource(Res.string.hisobdan_chiqish)
        }
        ParentRequestType.DELETE -> {
            stringResource(Res.string.ilovani_ochirish)
        }
    }

    val childName = parentRequestUi.childName

    val fullText = when (parentRequestUi.type) {
        ParentRequestType.LOGOUT -> stringResource(
            Res.string.farzand_ilovadan_chiqish_sorovi,
            childName
        )
        ParentRequestType.DELETE -> stringResource(
            Res.string.farzand_ilovani_ochirish_sorovi,
            childName
        )
    }

    val requestSubtitle = buildAnnotatedString {
        append(fullText)
        val startIndex = fullText.indexOf(childName)
        if (startIndex >= 0) {
            addStyle(
                style = AppTypography.titleMdMedium
                    .copy(color = AppColors.text.accentEmphasis)
                    .toSpanStyle(),
                start = startIndex,
                end = startIndex + childName.length
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(CardCornerRadius))
            .padding(ContainerPadding),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = requestTitle,
                style = AppTypography.titleMdSemiBold,
                color = AppColors.text.primary,
                modifier = Modifier.weight(1f)
            )
            SpaceSmall()

            Box(
                modifier = Modifier
                    .background(
                        parentRequestUi.status.color,
                        RoundedCornerShape(CardCornerRadius)
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(parentRequestUi.status.title),
                    style = AppTypography.bodyMdMedium,
                    color = AppColors.text.inverse
                )
            }
        }
        Space(12.dp)

        Text(
            text = requestSubtitle,
            style = AppTypography.titleSmMedium,
            color = AppColors.text.secondary
        )
        Space(8.dp)

        Text(
            text = parentRequestUi.createdAt,
            style = AppTypography.bodySmMedium,
            color = AppColors.text.placeholder,
            modifier = Modifier.align(Alignment.End)
        )

        Space(8.dp)
        DividerHorizontal()
        Space(8.dp)

        if (parentRequestUi.status == ParentRequestStatus.PROCESS){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomButtonNew(
                    text = stringResource(Res.string.bekor_qilish),
                    containerColor = AppColors.section.section,
                    contentColor = AppColors.text.primary,
                    onClick = onCancelRequestClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(DialogButtonHeight),
                )
                Space(8.dp)
                CustomButtonNew(
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
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ){
        ParentRequestItem(
            parentRequestUi = ParentRequestUi(
                type = ParentRequestType.DELETE,
                requestId = "",
                childName = "Abdurahimjon",
                createdAt = "10.12.2025",
                status = ParentRequestStatus.DENIED
            ),
        )
    }
}