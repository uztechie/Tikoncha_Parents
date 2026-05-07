package uz.tikoncha_parent.presentation.profile.children

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.profile_hedgehog_img
import tikoncha_parents.composeapp.generated.resources.vertical_menu
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun ChildrenItem(
    modifier: Modifier = Modifier,
    endingIcon: (@Composable () -> Unit)? = null,
    onMenuClick: () -> Unit = {},
    onClick: () -> Unit = {},
    imageUrl: String = "",
    lastSeen: String = "",
    gadget: String = "",
    name: String = ""
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(73.dp)
            .singleClick { onClick() }
            .background(AppColors.bg.surface, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .border(1.dp, AppColors.bg.surface, CircleShape)
                .background(AppColors.bg.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            when {
                imageUrl.isNotEmpty() -> {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "",
                        error = painterResource(Res.drawable.profile_hedgehog_img),
                        placeholder = painterResource(Res.drawable.profile_hedgehog_img),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
                else -> {
                    Image(
                        painter = painterResource(Res.drawable.profile_hedgehog_img),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            }
        }
        Space(18.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            Text(
                text = name,
                style = AppTypography.titleMdSemiBold,
                color = AppColors.text.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = gadget,
                    style = AppTypography.titleSmMedium,
                    color = AppColors.text.secondary
                )
                Space(18.dp)

                Text(
                    text = lastSeen,
                    style = AppTypography.titleSmMedium,
                    color = AppColors.text.secondary
                )
            }
        }
        Spacer(Modifier.width(12.dp))

        if (endingIcon != null) {
            endingIcon()
        } else {
//            Icon(
//                painter = painterResource(Res.drawable.vertical_menu),
//                contentDescription = "",
//                tint = AppColors.icon.secondary,
//                modifier = Modifier
//                    .size(24.dp)
//                    .singleClick { onMenuClick() }
//            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        ChildrenItem(
            name = "Jaloliddin",
            gadget = "Samsung A12",
            lastSeen = "Onlayn"
        )
    }
}