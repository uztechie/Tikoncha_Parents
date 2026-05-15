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
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bajarildi
import tikoncha_parents.composeapp.generated.resources.bekor_qilish
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelTimePickerDialog(
    show: Boolean,
    currentTime: LocalTime,
    confirmButtonText: String = stringResource(Res.string.bajarildi),
    dismissButtonText: String = stringResource(Res.string.bekor_qilish),
    confirmButtonColor: Color = AppColors.button.primary,
    dismissButtonColor: Color = AppColors.section.secondary,
    showCancelButton: Boolean = true,
    onConfirm: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
){
    if (show){
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )
        LaunchedEffect(show){
            if (show) sheetState.expand()
        }
        var selectedTime by remember(currentTime) {
            mutableStateOf(currentTime)
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
                        start = 16.dp,
                        end = 16.dp
                    )
            ){
                Card(
                    shape = RoundedCornerShape(24.dp),
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



                        WheelTimePicker(
                            initialTime = selectedTime,
                            onTimeChanged = {
                                selectedTime = it
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = WheelTimePickerDefaults.colors(
                                backgroundColor = Color.Transparent,
                            ),
                            visibleItemCount = 3,
                            selectedTextStyle = AppTypography.displayMdSemiBold,
                            unselectedTextStyle = AppTypography.headlineSmMedium,
                            showLabels = true
                        )


                        CustomButtonNew(
                            text = confirmButtonText,
                            onClick = {
                                onConfirm(selectedTime)
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
                                containerColor = dismissButtonColor,
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
        WheelTimePickerDialog(
            show = true,
            currentTime = LocalTime(5, 12),
            onDismiss = {},
            onConfirm = {}
        )
    }
}