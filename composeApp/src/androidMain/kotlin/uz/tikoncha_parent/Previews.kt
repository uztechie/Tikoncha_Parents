package uz.tikoncha_parent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.painterResource

import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bekor_qilish
import tikoncha_parents.composeapp.generated.resources.boshlanishi
import tikoncha_parents.composeapp.generated.resources.ch
import tikoncha_parents.composeapp.generated.resources.clock
import tikoncha_parents.composeapp.generated.resources.close
import tikoncha_parents.composeapp.generated.resources.close_circle
import tikoncha_parents.composeapp.generated.resources.du
import tikoncha_parents.composeapp.generated.resources.faol_vaqt_yaratish
import tikoncha_parents.composeapp.generated.resources.ju
import tikoncha_parents.composeapp.generated.resources.pa
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.se
import tikoncha_parents.composeapp.generated.resources.sh
import tikoncha_parents.composeapp.generated.resources.tugashi
import tikoncha_parents.composeapp.generated.resources.vaqt
import tikoncha_parents.composeapp.generated.resources.ya
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.base.verticalShadow
import uz.tikoncha_parent.presentation.home.schedule.timelist.AllDayRow
import uz.tikoncha_parent.presentation.home.schedule.timelist.Timeline
import uz.tikoncha_parent.presentation.home.schedule.timelist.WeekdayChips
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeEvent
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeItem
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeState
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeUi
import uz.tikoncha_parent.ui.BackgroundColor
import uz.tikoncha_parent.ui.BorderColor
import uz.tikoncha_parent.ui.ButtonCornerRadius
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.LargeTextSize
import uz.tikoncha_parent.ui.NormalIconButtonPadding
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.NormalIconSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.OnPrimaryColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.ShapeCornerRadius
import uz.tikoncha_parent.ui.SmallIconButtonSize
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor





@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(ThemeMode.LIGHT) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(ContainerPadding),
            contentAlignment = Alignment.Center
        ) {

        }


    }

}

