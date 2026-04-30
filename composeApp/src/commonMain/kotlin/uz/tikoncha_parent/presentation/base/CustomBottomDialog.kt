package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bekor_qilish
import tikoncha_parents.composeapp.generated.resources.davom_etish
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.OtpErrorColor
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomDialog(
    show: Boolean,
    title: String,
    message: String,
    confirmButtonText: String = stringResource(Res.string.davom_etish),
    dismissButtonText: String = stringResource(Res.string.bekor_qilish),
    confirmButtonColor: Color = AppColors.button.primary,
    showCancelButton: Boolean = true,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
){
    if (show){
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )
        LaunchedEffect(show){
            if (show) sheetState.expand()
        }

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss,
            containerColor = Color.Transparent,
            dragHandle = {}
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        start = ContainerPadding,
                        end = ContainerPadding
                    )
            ){
                Card(
                    shape = RoundedCornerShape(CardCornerRadius),
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.bg.surface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(ContainerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Box(
                            modifier = Modifier
                                .padding(bottom = 15.dp)
                                .background(color = Color(0XFF9A9A9A), shape = CircleShape)
                                .height(3.dp)
                                .width(36.dp)
                        )

                        Text(
                            text = title,
                            color = AppColors.text.primary,
                            style = AppTypography.titleLgSemiBold

                        )
                        Space(12.dp)

                        Text(
                            text = message,
                            color = AppColors.text.secondary,
                            style = AppTypography.emphasizedMdMedium,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Space(16.dp)

                        CustomButtonNew(
                            text = confirmButtonText,
                            onClick = {
                                onConfirm()
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            containerColor = confirmButtonColor
                        )
                        if (showCancelButton){
                            Space(8.dp)
                            CustomButtonNew(
                                text = dismissButtonText,
                                onClick = onDismiss,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                containerColor = AppColors.section.section,
                                contentColor = AppColors.text.primary
                            )
                        }
                    }
                }
                SpaceLarge()
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
        CustomBottomDialog(
            show = true,
            title = "Title",
            message = "Message",
            onDismiss = {},
            onConfirm = {}
        )
    }
}