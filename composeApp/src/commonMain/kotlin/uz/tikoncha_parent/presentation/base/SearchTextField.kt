package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    query: String,
    label: String,
    containerColor: Color = MaterialTheme.extendedColor.tonalButtonColor,
    contentColor: Color = MaterialTheme.extendedColor.textColor,
    onQueryChange: (String)-> Unit
) {
    CustomTextField(
        modifier = modifier
            .clickable {  },
        value = query,
        label = label,
        onValueChange = onQueryChange,
        containerColor = containerColor,
        contentColor = contentColor,
        leadingIcon = {
            Image(
                painter = painterResource(Res.drawable.search_normal),
                contentDescription = "Search",
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onQueryChange("")
                    },
                    modifier = Modifier
                        .size(SmallIconButtonSize)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.close_circle),
                        contentDescription = "Close",
                        tint = contentColor
                    )
                }
            }
        }
    )
}