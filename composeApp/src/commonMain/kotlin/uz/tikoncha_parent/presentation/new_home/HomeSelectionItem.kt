package uz.tikoncha_parent.presentation.new_home

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import tikoncha_parents.composeapp.generated.resources.*

enum class HomeSelectionItem (
    val iconId: DrawableResource,
    val title: StringResource,
    val subtitle: StringResource,
    val iconEndPadding: Dp,
){
    XARITA(
        iconId = Res.drawable.home_map,
        title = Res.string.xarita,
        subtitle = Res.string.farzandlaringiz_qayerdaligi,
        iconEndPadding = 7.dp,
    ),
    SIHBAT(
        iconId = Res.drawable.home_chat,
        title = Res.string.suhbat,
        subtitle = Res.string.farzand_va_maktab_bilan_boglaning,
        iconEndPadding = 0.dp,
    )
}