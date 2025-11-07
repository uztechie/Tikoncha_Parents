package uz.tikoncha_parent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource

import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CloseButton
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.base.SoonBox
import uz.tikoncha_parent.presentation.base.bottomShadow
import uz.tikoncha_parent.presentation.base.coverShadow
import uz.tikoncha_parent.presentation.base.verticalShadow

import uz.tikoncha_parent.presentation.notification.NotificationScreen
import uz.tikoncha_parent.presentation.policy.rule_type_selection.RuleType
import uz.tikoncha_parent.presentation.policy.rule_type_selection.RuleTypeUi
import uz.tikoncha_parent.presentation.task.TaskEvent
import uz.tikoncha_parent.presentation.task.TaskState
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.DisableButtonColor
import uz.tikoncha_parent.ui.DisableButtonContentColor
import uz.tikoncha_parent.ui.DisableTextColor
import uz.tikoncha_parent.ui.HintTextColor
import uz.tikoncha_parent.ui.LargeTextSize
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.OnPrimaryColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.ShapeCornerRadius
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun PolicySetupRuleItem(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    onRemoveClick: () -> Unit,
    onItemClick: () -> Unit
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .verticalShadow(
                shape = RoundedCornerShape(CardCornerRadius),
                offset = 0.dp
            )
            .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(CardCornerRadius))
            .padding(horizontal = ContainerPadding, vertical = 12.dp)
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = onItemClick
            ),
    ) {
        Column(
            modifier = Modifier
        ) {
            CustomText(
                text = title,
                fontSize = LargeTextSize,
                fontWeight = FontWeight.SemiBold
            )
            SpaceSmall()
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = subTitle,
                    fontSize = NormalTextSize,
                    color = MaterialTheme.extendedColor.hintColor,
                    lineHeight = NormalTextLineHeight
                )
            }
        }

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier
                .size(SmallIconButtonSize)
        ) {
            Image(
                painter = painterResource(Res.drawable.close_circle),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.textColor),
            )
        }


    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(ThemeMode.DARK) {
        PolicySetupRuleItem(
            title = "Vaqt",
            subTitle = "Du, Se 19:20 - 21:00 \nCh, Pa 19:20 - 21:00 tashqari",
            onItemClick = {},
            onRemoveClick = {}
        )

    }
}