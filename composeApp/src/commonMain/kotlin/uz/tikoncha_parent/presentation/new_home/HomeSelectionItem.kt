package uz.tikoncha_parent.presentation.new_home

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import tikoncha_parents.composeapp.generated.resources.*

enum class HomeSelectionItem (
    val iconId: DrawableResource,
    val title: StringResource,
    val subtitle: StringResource,
){
    XARITA(
        iconId = Res.drawable.map,
        title = Res.string.xarita,
        subtitle = Res.string.farzandlaringiz_qayerdaligi,
    ),
    SIHBAT(
        iconId = Res.drawable.home_chat,
        title = Res.string.suhbat,
        subtitle = Res.string.farzand_va_maktab_bilan_boglaning,
    )
}