package uz.tikoncha_parent.presentation.monitoring

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import uz.tikoncha_parent.domain.model.PermissionItem
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.DividedButton
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.ui.theme.extendedColor

class ClientPermissionStateScreen() : Screen {
    @Composable
    override fun Content() {
        ClientPermissionStateUi()
    }


}

@Composable
fun ClientPermissionStateUi(
) {
    val rootNavigator = LocalNavigator.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {

        CustomHeader(
            title = stringResource(Res.string.bolaning_ilovasini_sozligi),
            showBackButton = true,
            onBackClick = {
                rootNavigator?.pop()
            }
        )

        SpaceMedium()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(ContainerPadding)
        ) {
            items(permissionList) {
                DividedButton(
                    success = it.isEnabled,
                    title = stringResource(it.title),
                    icon = painterResource(it.icon),
                    onItemClick = {},
                    isPermission = true
                )
            }
        }
    }
}

val permissionList = listOf<PermissionItem>(
    PermissionItem(
        title = Res.string.kamera,
        true,
        icon = Res.drawable.permission_camera
    ),
    PermissionItem(
        title = Res.string.joylashuv,
        true,
        icon = Res.drawable.permission_location
    ),
    PermissionItem(
        title = Res.string.monitoring,
        true,
        icon = Res.drawable.permission_monitoring
    ),
    PermissionItem(
        title = Res.string.admin_ilova,
        false,
        icon = Res.drawable.permission_adminstration
    ),
    PermissionItem(
        title = Res.string.ishlash_vaqti,
        false,
        icon = Res.drawable.permission_usage_time
    ),
    PermissionItem(
        title = Res.string.ustida_ko_rsatish,
        true,
        icon = Res.drawable.permission_floating
    ),
    PermissionItem(
        title = Res.string.eslatma_xizmati,
        true,
        icon = Res.drawable.permission_message_notif
    ),
    PermissionItem(
        title = Res.string.batareya_tejash,
        true,
        icon = Res.drawable.permission_battery_2bars_1
    ),
    PermissionItem(
        title = Res.string.gps,
        true,
        icon = Res.drawable.permission_location
    ),

    )

@Composable
@Preview
fun Preview() {
    ClientPermissionStateUi()
}