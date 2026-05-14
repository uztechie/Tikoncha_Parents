package uz.tikoncha_parent.presentation.profile.subscription.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bekor_qilish
import tikoncha_parents.composeapp.generated.resources.farzand_uchun_obuna
import tikoncha_parents.composeapp.generated.resources.farzand_uchun_obuna_message
import tikoncha_parents.composeapp.generated.resources.farzandingiz_ismi
import tikoncha_parents.composeapp.generated.resources.person
import tikoncha_parents.composeapp.generated.resources.sotib_olish
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.CustomTextField
import uz.tikoncha_parent.presentation.base.PhoneNumberInputField
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalIconSize
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.TextFieldHeight
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeChildBottomSheet(
    show: Boolean,
    title: String = stringResource(Res.string.farzand_uchun_obuna),
    message: String = stringResource(Res.string.farzand_uchun_obuna_message),
    confirmButtonText: String = stringResource(Res.string.sotib_olish),
    dismissButtonText: String = stringResource(Res.string.bekor_qilish),
    onDismiss: () -> Unit,
    onConfirm: (phoneNumber: String) -> Unit
) {
    if (show) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        var phoneNumber by remember { mutableStateOf("") }

        val isFormValid = phoneNumber.length == 9

        LaunchedEffect(show) {
            if (show) sheetState.expand()
        }

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss,
            containerColor = Color.Transparent,
            dragHandle = {}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.bg.elevated)
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(ContainerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                // Drag handle
                Box(
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                        .background(color = Color(0XFF9A9A9A), shape = CircleShape)
                        .height(3.dp)
                        .width(36.dp)
                )

                // Title
                Text(
                    text = title,
                    color = AppColors.text.primary,
                    style = AppTypography.titleLgSemiBold
                )
                Space(12.dp)

                // Message
                Text(
                    text = message,
                    color = AppColors.text.secondary,
                    style = AppTypography.emphasizedMdMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Space(20.dp)

                // Phone number input
                PhoneNumberInputField(
                    phoneNumber = phoneNumber,
                    onPhoneNumberChange = { phoneNumber = it }
                )

                Space(20.dp)

                // Confirm — Sotib olish
                CustomButtonNew(
                    text = confirmButtonText,
                    enabled = isFormValid,
                    onClick = { onConfirm(phoneNumber) },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = AppColors.button.primary
                )
                Space(8.dp)

                // Cancel — Bekor qilish
                CustomButtonNew(
                    text = dismissButtonText,
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = AppColors.section.section,
                    contentColor = AppColors.text.primary
                )

            }
        }

    }
}


@Preview
@Composable
private fun PreviewDark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        SubscribeChildBottomSheet(
            show = true,
            title = "Farzand ma'lumotlari",
            message = "Farzandingiz hali ro'yxatdan o'tmagan. Obunani faollashtirish uchun uning telefon raqami va ismini kiriting — biz hisobini avtomatik yaratib, obunani ulab beramiz.",
            onDismiss = {},
            onConfirm = { _ -> }
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        SubscribeChildBottomSheet(
            show = true,
            title = "Farzand ma'lumotlari",
            message = "Farzandingiz hali ro'yxatdan o'tmagan. Obunani faollashtirish uchun uning telefon raqami va ismini kiriting — biz hisobini avtomatik yaratib, obunani ulab beramiz.",
            onDismiss = {},
            onConfirm = { _-> }
        )
    }
}