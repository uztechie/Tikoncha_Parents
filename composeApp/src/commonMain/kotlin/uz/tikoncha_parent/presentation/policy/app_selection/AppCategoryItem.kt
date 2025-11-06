package uz.tikoncha_parent.presentation.policy.app_selection

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.policy.RoundedCheckbox
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun AppCategoryItem(
    category: AppCategoryUi,
    onHeaderClick: () -> Unit,
    onToggleAll: (checked: Boolean) -> Unit,
    onToggleApp: (appUi: AppsUi, checked: Boolean) -> Unit
){

    val allChecked = category.apps.isNotEmpty() && category.apps.all { it.checked }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ){ onHeaderClick() }
                .padding(start = 0.dp, end = 6.dp, top = 8.dp, bottom = 8.dp),
        ) {
            Icon(
                painter = if (category.expanded)
                    painterResource(Res.drawable.arrow_up)
                else
                    painterResource(Res.drawable.arrow_down),
                contentDescription = null,
            )
            SpaceSmall()

            val iconUrl = category.apps.firstOrNull()?.iconUrl

            Box(
                modifier = Modifier
                    .size(LargeIconSize)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Background),
                contentAlignment = Alignment.Center
            ){
                if (!iconUrl.isNullOrBlank()){
                    AsyncImage(
                        modifier = Modifier
                            .size(LargeIconSize)
                            .clip(RoundedCornerShape(6.dp)),
                        model = iconUrl,
                        contentDescription = null,
                        placeholder = painterResource(Res.drawable.ic_launcher_foreground),
                        error = painterResource(Res.drawable.ic_launcher_foreground),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            SpaceSmall()

            CustomText(
                text = category.title,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            RoundedCheckbox(
                checked = allChecked,
                onCheckedChange = { onToggleAll(it) }
            )
        }
        AnimatedVisibility(
            visible = category.expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                category.apps.forEach { apps ->
                    AppRow( app = apps, onCheckedChange = {onToggleApp(apps,it)})
                }
            }
        }
    }
}