package uz.tikoncha_parent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.base.coverShadow
import uz.tikoncha_parent.presentation.base.verticalShadow
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeColumn
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeEvent
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeState
import uz.tikoncha_parent.presentation.home.schedule.timelist.WeekdayChips
import uz.tikoncha_parent.presentation.home.schedule.type.ScheduleItem
import uz.tikoncha_parent.presentation.home.schedule.type.ScheduleTypeItem
import uz.tikoncha_parent.presentation.task.TaskEvent
import uz.tikoncha_parent.presentation.task.TaskState
import uz.tikoncha_parent.ui.ButtonCornerRadius
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
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun ScheduleTypeUi(
    navigator: Navigator?,
    state: TaskState,
    event: (TaskEvent) -> Unit,

    modifier: Modifier = Modifier,
    painter: Painter,
    title: String,
    subTitle: String,
    enable: Boolean = false

) {
    val bgColor = MaterialTheme.extendedColor.cardColor


    Row(
        modifier = modifier
            .fillMaxWidth()
            .verticalShadow(
                shape = RoundedCornerShape(TextFieldCornerRadius),
                offset = 0.dp
            )
            .background(
                if (enable) DisableButtonContentColor else
                bgColor,
                RoundedCornerShape(TextFieldCornerRadius)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(NormalIconButtonSize)
                .clip(RoundedCornerShape(ShapeCornerRadius))
                .background(MaterialTheme.extendedColor.tonalButtonColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize(0.6f)
            )
        }

        SpaceMedium()

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomText(
                text = title,
                fontSize = LargeTextSize,
                fontWeight = FontWeight.SemiBold
            )

            CustomText(
                text = subTitle,
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold,
                color = HintTextColor
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        ScheduleTypeUi(
            navigator = null,
            state = TaskState(),
            event = {},


            painter = painterResource(Res.drawable.clock),
            title = stringResource(Res.string.vaqt),
            subTitle = "Tanlash vaqti va kunlar"
        )
    }
}