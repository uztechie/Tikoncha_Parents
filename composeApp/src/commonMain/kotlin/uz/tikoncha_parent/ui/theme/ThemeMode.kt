package uz.tikoncha_parent.ui.theme

import org.jetbrains.compose.resources.StringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*

enum class ThemeMode(val resId: StringResource){
    SYSTEM(resId = Res.string.ism),
    LIGHT(resId = Res.string.yorug),
    DARK(resId = Res.string.qorong_u),
}