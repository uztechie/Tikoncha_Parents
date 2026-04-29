package uz.tikoncha_parent.presentation.tracking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import coil3.compose.AsyncImage
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bekor_qilish
import tikoncha_parents.composeapp.generated.resources.dialog_info
import tikoncha_parents.composeapp.generated.resources.profile_hedgehog_img
import tikoncha_parents.composeapp.generated.resources.s
import tikoncha_parents.composeapp.generated.resources.siz
import tikoncha_parents.composeapp.generated.resources.tracking_gps_off_enable
import tikoncha_parents.composeapp.generated.resources.tracking_gps_off_message
import tikoncha_parents.composeapp.generated.resources.tracking_gps_off_title
import tikoncha_parents.composeapp.generated.resources.tracking_permission_grant
import tikoncha_parents.composeapp.generated.resources.tracking_permission_message
import tikoncha_parents.composeapp.generated.resources.tracking_permission_settings_message
import tikoncha_parents.composeapp.generated.resources.tracking_permission_settings_open
import tikoncha_parents.composeapp.generated.resources.tracking_permission_settings_title
import tikoncha_parents.composeapp.generated.resources.tracking_permission_title
import uz.tikoncha_parent.platform.openAppSettings
import uz.tikoncha_parent.platform.openLocationSettings
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.map.OnScreenActive
import uz.tikoncha_parent.presentation.map2.CameraPosition
import uz.tikoncha_parent.presentation.map2.LatLng
import uz.tikoncha_parent.presentation.map2.MapController
import uz.tikoncha_parent.presentation.map2.MapMarker
import uz.tikoncha_parent.presentation.map2.MarkerStyle
import uz.tikoncha_parent.presentation.map2.YandexMap
import uz.tikoncha_parent.presentation.map2.rememberMapController
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

// Andijon shahri — fallback markaz
private val ANDIJAN_CENTER = LatLng(40.7821, 72.3442)
class TrackingScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<TrackingScreenModel>()
        val state by screenModel.state.collectAsState()
        val mapController = rememberMapController()
        val snackbarHostState = remember { SnackbarHostState() }

        val permissionsController: PermissionsController = koinInject()
        val locationTracker: LocationTracker = koinInject()

        BindEffect(permissionsController)
        BindLocationTrackerEffect(locationTracker)

        var cameFromSettings by remember { mutableStateOf(false) }

        // Settings'dan qaytganini kuzatish
        OnScreenActive(
            launchedToSettings = cameFromSettings,
            onReturned = {
                cameFromSettings = false
                // Permission'ni qayta tekshirish va davom etish
                screenModel.onEvent(TrackingEvent.RecheckPermission)
            }
        )

        // -------- DIALOG STATE'lari (remember bilan) --------
        var showPermissionDialog by remember { mutableStateOf(false) }
        var showPermissionAlwaysDialog by remember { mutableStateOf(false) }
        var showGpsDialog by remember { mutableStateOf(false) }

        // Effects'larni eshitish
        LaunchedEffect(Unit) {
            screenModel.effect.collect { eff ->
                when (eff) {
                    is TrackingEffect.MoveCamera ->
                        mapController.moveTo(CameraPosition(eff.target, eff.zoom))
                    is TrackingEffect.FitBounds ->
                        mapController.fitBounds(eff.points)
                    is TrackingEffect.ShowError ->
                        snackbarHostState.showSnackbar(eff.message)
                    TrackingEffect.OpenAppSettings -> openAppSettings()
                    TrackingEffect.OpenLocationSettings -> openLocationSettings()
                    TrackingEffect.RequestPermission -> Unit

                    // ⬇️ YANGI — retry loop bilan
                    TrackingEffect.MoveToUserLocation -> {
                        // Yandex GPS aniqlashi 1-3 soniya olishi mumkin
                        // 10 marta urinish, har 300ms = 3 soniya
                        repeat(10) {
                            delay(300)
                            if (mapController.tryMoveToUserLocation(animated = true)) {
                                return@repeat  // muvaffaqiyatli — to'xtaydi
                            }
                        }
                    }
                }
            }
        }

        // State -> dialog visibility sinxronizatsiyasi
        // ScreenModel state o'zgarganda dialog'lar ochiladi/yopiladi
        LaunchedEffect(state.permissionDenied, state.permissionDeniedAlways) {
            showPermissionDialog = state.permissionDenied && !state.permissionDeniedAlways
            showPermissionAlwaysDialog = state.permissionDeniedAlways
        }

        LaunchedEffect(state.showGpsDialog) {
            showGpsDialog = state.showGpsDialog
        }

        val placeholderBitmap = imageResource(Res.drawable.profile_hedgehog_img)

        // -------- ASOSIY UI --------
        TrackingContent(
            state = state,
            mapController = mapController,
            snackbarHostState = snackbarHostState,
            placeholderBitmap = placeholderBitmap,
            onEvent = screenModel::onEvent
        )

        // -------- DIALOGLAR (CustomDialog ishlatib) --------

        // 1. Permission denied dialog
        CustomDialog(
            show = showPermissionDialog,
            title = stringResource(Res.string.tracking_permission_title),
            message = stringResource(Res.string.tracking_permission_message),
            painter = painterResource(Res.drawable.dialog_info),
            buttonText = stringResource(Res.string.tracking_permission_grant),
            buttonText2 = stringResource(Res.string.bekor_qilish),
            showCloseButton = true,
            onButtonClick = {
                showPermissionDialog = false
                screenModel.onEvent(TrackingEvent.RequestLocationPermission)
            },
            onDismiss = {
                // ↓ MUHIM: ham lokal flag, ham state'ni tozalash
                showPermissionDialog = false
                screenModel.onEvent(TrackingEvent.DismissPermissionDialog)
            }
        )

// 2. DeniedAlways dialog
        CustomDialog(
            show = showPermissionAlwaysDialog,
            title = stringResource(Res.string.tracking_permission_settings_title),
            message = stringResource(Res.string.tracking_permission_settings_message),
            painter = painterResource(Res.drawable.dialog_info),
            buttonText = stringResource(Res.string.tracking_permission_settings_open),
            buttonText2 = stringResource(Res.string.bekor_qilish),
            showCloseButton = true,
            onButtonClick = {
                cameFromSettings = true
                showPermissionAlwaysDialog = false
                screenModel.onEvent(TrackingEvent.OpenAppSettings)
                // ↑ Settings'ga o'tdi — state'ni tozalash shart emas,
                //   chunki user qaytib kelganda biz qayta tekshiramiz
                screenModel.onEvent(TrackingEvent.DismissPermissionDialog)
            },
            onDismiss = {
                showPermissionAlwaysDialog = false
                screenModel.onEvent(TrackingEvent.DismissPermissionDialog)
            }
        )

        // 3. GPS o'chiq
        CustomDialog(
            show = showGpsDialog,
            title = stringResource(Res.string.tracking_gps_off_title),
            message = stringResource(Res.string.tracking_gps_off_message),
            painter = painterResource(Res.drawable.dialog_info),
            buttonText = stringResource(Res.string.tracking_gps_off_enable),
            buttonText2 = stringResource(Res.string.bekor_qilish),
            showCloseButton = true,
            onButtonClick = {
                cameFromSettings = true
                showGpsDialog = false
                openLocationSettings()
                screenModel.onEvent(TrackingEvent.GpsEnabledByUser)
            },
            onDismiss = {
                showGpsDialog = false
                screenModel.onEvent(TrackingEvent.DismissGpsDialog)
            }
        )
    }
}

// ============================================================
// TrackingContent va boshqa UI funksiyalar
// (Avvalgi javobimdagi kod — kichik PersonCard, fallback initialCenter)
// ============================================================

@Composable
fun TrackingContent(
    state: TrackingState,
    mapController: MapController,
    snackbarHostState: SnackbarHostState,
    onEvent: (TrackingEvent) -> Unit,
    placeholderBitmap: ImageBitmap
) {
    val selfText = stringResource(Res.string.siz)
    val initialCenter = remember(state.self, state.people) {
        state.self?.location
            ?: state.people.firstNotNullOfOrNull { it.location }
            ?: ANDIJAN_CENTER
    }

    val initialZoom = remember(state.self, state.people) {
        when {
            state.self?.location != null -> 14f
            state.people.any { it.location != null } -> 13f
            else -> 11f
        }
    }

    val markers = remember(state.people, state.selectedPersonId, placeholderBitmap) {
        state.people.mapNotNull { person ->
            val loc = person.location ?: return@mapNotNull null
            val style = when {
                person.isSelf -> MarkerStyle.Self(
                    text = selfText
                )
                person.id == state.selectedPersonId -> MarkerStyle.ChildSelected(
                    label = person.name,
                    avatarUrl = person.avatarUrl,
                    placeholderBitmap = placeholderBitmap
                )
                else -> MarkerStyle.Child(
                    label = person.name,
                    avatarUrl = person.avatarUrl,
                    placeholderBitmap = placeholderBitmap
                )
            }
            MapMarker(
                id = person.id,
                position = loc,
                style = style,
                zIndex = if (person.id == state.selectedPersonId) 1f else 0f
            )
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {

            YandexMap(
                controller = mapController,
                initialCamera = CameraPosition(initialCenter, zoom = initialZoom),
                markers = markers,
                onMarkerClick = { id -> onEvent(TrackingEvent.MarkerClicked(id)) },
                showUserLocation = state.userLocationEnabled,   // ← MUHIM!
                modifier = Modifier.fillMaxSize()
            )

            FloatingActionButton(
                onClick = { onEvent(TrackingEvent.SelfClicked) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 70.dp),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Self")
            }

            PeopleStrip(
                people = state.people,
                selectedId = state.selectedPersonId,
                onClick = { onEvent(TrackingEvent.PersonClicked(it)) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            if (state.isLoading) {
                LinearProgressIndicator(
                    Modifier.fillMaxWidth().align(Alignment.TopStart)
                )
            }
        }
    }
}


@Composable
private fun PeopleStrip(
    people: List<Person>,
    selectedId: String?,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(people, key = { it.id }) { person ->
            PersonCard(
                person = person,
                selected = person.id == selectedId,
                onClick = { onClick(person.id) }
            )
        }
    }
}

@Composable
private fun PersonCard(
    person: Person,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = AppColors.bg.tertiary,
        tonalElevation = 2.dp,
        shadowElevation = 3.dp,
        border = if (selected) BorderStroke(2.dp, AppColors.border.accentEmphasis) else null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Avatar(
                person = person,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = person.name,
                style = AppTypography.titleSmMedium,
                color = AppColors.text.primary
            )
            Space(16.dp)
        }
    }
}

@Composable
private fun Avatar(
    modifier: Modifier = Modifier,
    person: Person
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(AppColors.bg.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = person.avatarUrl,
            placeholder = painterResource(Res.drawable.profile_hedgehog_img),
            error = painterResource(Res.drawable.profile_hedgehog_img),         // ← rasm yuklashda xato bo'lsa ham
            fallback = painterResource(Res.drawable.profile_hedgehog_img),      // ← URL null bo'lsa
            modifier = Modifier.fillMaxSize(),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
fun Pre(){
    TikonchaParentTheme {
//        PersonCard(
//            person = Person(
//                id = "ds",
//                name = "Ibroxim",
//                location = null,
//                isSelf = false,
//                lastSeenEpochMs = null,
//                lastSeen = "12/02/2025",
//                avatarUrl = null
//            ),
//            selected = true,
//            onClick = {}
//        )
//    }
    }
}