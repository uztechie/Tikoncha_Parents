package uz.tikoncha_parent.presentation.policy.app_site_selection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.HeaderHeight
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography


@Composable
fun SearchHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester,
    placeholder: String,
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(HeaderHeight)
            .padding(horizontal = ContainerPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier.size(NormalIconButtonSize),
            onClick = onClose,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = AppColors.icon.primary,
            ),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        }

        SpaceMedium()

        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (query.isEmpty()) {
                Text(
                    text = placeholder,
                    style = AppTypography.titleMdRegular,
                    color = AppColors.text.tertiary,
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                singleLine = true,
                textStyle = AppTypography.titleMdRegular.merge(
                    TextStyle(color = AppColors.text.primary),
                ),
                cursorBrush = SolidColor(AppColors.border.primary),
            )
        }
    }
}