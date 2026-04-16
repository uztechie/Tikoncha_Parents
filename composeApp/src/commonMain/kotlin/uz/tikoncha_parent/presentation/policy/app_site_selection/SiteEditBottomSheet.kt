package uz.tikoncha_parent.presentation.policy.app_site_selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.message_delete
import tikoncha_parents.composeapp.generated.resources.message_edit
import tikoncha_parents.composeapp.generated.resources.ochirish
import tikoncha_parents.composeapp.generated.resources.tahrirlash
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteEditBottomSheet(
    show: Boolean,
    site: SiteUi?,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!show || site == null) return

    val sheetState = rememberModalBottomSheetState()


    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        containerColor = Color.Transparent,
        dragHandle = {}
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = ContainerPadding,
                    end = ContainerPadding
                )
        ) {
            Card(
                shape = RoundedCornerShape(CardCornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.bg.surface
                ),
                modifier = Modifier.fillMaxWidth()
            )
            {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ContainerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .padding(bottom = 15.dp)
                            .background(color = Color(0XFF9A9A9A), shape = CircleShape)
                            .height(3.dp)
                            .width(36.dp),
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .singleClick { onEdit() },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.message_edit),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = AppColors.icon.primary,
                        )
                        Space(24.dp)
                        Text(
                            text = stringResource(Res.string.tahrirlash),
                            style = AppTypography.titleMdMedium,
                            color = AppColors.text.primary,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .singleClick { onDelete() },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.message_delete),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = AppColors.icon.primary,
                        )
                        Space(24.dp)
                        Text(
                            text = stringResource(Res.string.ochirish),
                            style = AppTypography.titleMdMedium,
                            color = AppColors.text.primary,
                        )
                    }
                }
            }
        }
    }


}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        SiteEditBottomSheet(
            show = true,
            site = SiteUi(
                url = "",
                isDefault = true
            ),
            onEdit = {},
            onDelete = {},
            onDismiss = {}
        )
    }
}