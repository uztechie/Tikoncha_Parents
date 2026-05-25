package uz.tikoncha_parent.presentation.policy.app_site_selection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.close_remove
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.sayt_manzilini_kiriting
import tikoncha_parents.composeapp.generated.resources.sayt_yoki_kalit_soz_kiriting
import tikoncha_parents.composeapp.generated.resources.sayt_yoki_kalit_soz_label
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomTextField
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography

@Composable
fun AddSiteDialog(
    show: Boolean,
    inputUrl: String,
    inputError: String?,
    onInputChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!show) return

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.bg.elevated,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ContainerPadding),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(Res.string.sayt_yoki_kalit_soz_kiriting),
                        color = AppColors.text.primary,
                        style = AppTypography.titleMdSemiBold,
                        modifier = Modifier.weight(1f),
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(30.dp),
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.close_remove),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                CustomTextField(
                    label = stringResource(Res.string.sayt_yoki_kalit_soz_label),
                    value = inputUrl,
                    singleLine = true,
                    onValueChange = onInputChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    containerColor = AppColors.bg.secondarySurface,
                    contentColor = AppColors.text.primary,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.None,
                        imeAction = ImeAction.Done,
                    ),
                )

                if (inputError != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = inputError,
                        color = AppColors.action.accentDanger,
                        style = AppTypography.bodySmMedium,
                    )
                }

                Spacer(Modifier.height(16.dp))

                CustomButton(
                    text = stringResource(Res.string.saqlash),
                    onClick = onConfirm,
                    enabled = inputUrl.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                )
            }
        }
    }
}