package uz.tikoncha_parent.presentation.policy.policy_setup

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.jadval_nomi
import tikoncha_parents.composeapp.generated.resources.saqlash
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.CustomTextField
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun PolicyNameDialog(
    modifier: Modifier = Modifier,
    show:Boolean = false,
    title:String = stringResource(Res.string.jadval_nomi),
    buttonText:String = stringResource(Res.string.saqlash),
    policyName: String = "",
    onDismiss: () -> Unit,
    onButtonClick: (String)->Unit,
    enableButton: Boolean = true,
) {

    if (!show){
        return
    }

    var value by remember { mutableStateOf(policyName) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(AppColors.bg.elevated),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp, start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = AppTypography.titleMdSemiBold,
                        color = AppColors.text.primary,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Space(8.dp)
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .size(24.dp),
                            tint = MaterialTheme.extendedColor.onBackgroundColor
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    CustomTextField(
                        label = stringResource(Res.string.jadval_nomi),
                        value = value,
                        onValueChange = {
                            value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        containerColor = AppColors.bg.secondarySurface,
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words
                        )
                    )
                    Space(16.dp)

                    CustomButtonNew(
                        enabled = enableButton,
                        text = buttonText,
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            onDismiss.invoke()
                            onButtonClick.invoke(
                                value
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CustomConfirmDialogPre() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        PolicyNameDialog(
            show = true,
            title = "Title",
            buttonText = "Ok",
            onDismiss = {},
            onButtonClick = {}
        )
    }
}