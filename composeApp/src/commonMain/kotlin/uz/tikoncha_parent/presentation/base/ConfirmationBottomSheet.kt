package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationBottomSheet(
    title: String,
    subtitle: String,
    confirmText: String,
    cancelText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppColors.bg.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = AppTypography.titleLgSemiBold,
                color = AppColors.text.primary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))

            Text(
                text = subtitle,
                style = AppTypography.emphasizedMdMedium,
                color = AppColors.text.secondary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            // Tasdiqlash tugmasi (qizil)
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.bg.accentDanger,
                    contentColor = AppColors.text.inverse
                )
            ) {
                Text(
                    text = confirmText,
                    style = AppTypography.titleSmMedium
                )
            }
            Spacer(Modifier.height(8.dp))

            // Bekor qilish tugmasi (kulrang)
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.section.secondary,
                    contentColor = AppColors.text.primary
                )
            ) {
                Text(
                    text = cancelText,
                    style = AppTypography.titleSmMedium
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
    ) {
        ConfirmationBottomSheet(
            title = "Title",
            subtitle = "Subtitle",
            confirmText = "Confirm",
            cancelText = "Cancel",
            onConfirm = {},
            onDismiss = {}
        )
    }
}