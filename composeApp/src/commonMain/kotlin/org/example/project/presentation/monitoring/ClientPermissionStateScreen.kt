package org.example.project.presentation.monitoring

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.domain.model.PermissionItem
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.DividedButton
import org.example.project.presentation.base.theme.BackgroundColor
import org.example.project.presentation.base.theme.ContainerPadding
import org.example.project.presentation.base.theme.SpaceMedium
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bolaning_ilovasini_sozligi
import tikoncha_parents.composeapp.generated.resources.permission_adminstration
import tikoncha_parents.composeapp.generated.resources.permission_battery_2bars_1
import tikoncha_parents.composeapp.generated.resources.permission_camera
import tikoncha_parents.composeapp.generated.resources.permission_floating
import tikoncha_parents.composeapp.generated.resources.permission_location
import tikoncha_parents.composeapp.generated.resources.permission_message_notif
import tikoncha_parents.composeapp.generated.resources.permission_monitoring
import tikoncha_parents.composeapp.generated.resources.permission_usage_time

class ClientPermissionStateScreen(): Screen {
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
            .background(BackgroundColor)
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
                    title = it.title,
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
        title = "Kamera",
        true,
        icon = Res.drawable.permission_camera
    ),
    PermissionItem(
        title = "Joylashuv",
        true,
        icon = Res.drawable.permission_location
    ),
    PermissionItem(
        title = "Monitoring",
        true,
        icon = Res.drawable.permission_monitoring
    ),
    PermissionItem(
        title = "Admin Ilova",
        false,
        icon = Res.drawable.permission_adminstration
    ),
    PermissionItem(
        title = "Ishlash vaqti",
        false,
        icon = Res.drawable.permission_usage_time
    ),
    PermissionItem(
        title = "Ustida ko'rsatish",
        true,
        icon = Res.drawable.permission_floating
    ),
    PermissionItem(
        title = "Eslatma xizmati",
        true,
        icon = Res.drawable.permission_message_notif
    ),
    PermissionItem(
        title = "Batareya tejash",
        true,
        icon = Res.drawable.permission_battery_2bars_1
    ),
    PermissionItem(
        title = "GPS",
        true,
        icon = Res.drawable.permission_location
    ),

    )

@Composable
@Preview
fun Preview(){
    ClientPermissionStateUi()
}