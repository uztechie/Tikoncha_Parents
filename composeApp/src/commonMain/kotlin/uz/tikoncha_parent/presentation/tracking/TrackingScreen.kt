package uz.tikoncha_parent.presentation.tracking

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
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
import tikoncha_parents.composeapp.generated.resources.arrow_back
import tikoncha_parents.composeapp.generated.resources.bekor_qilish
import tikoncha_parents.composeapp.generated.resources.dialog_info
import tikoncha_parents.composeapp.generated.resources.farzandingizni_qayerda_ekanini_kuzatish
import tikoncha_parents.composeapp.generated.resources.plus_obnuna_kerak
import tikoncha_parents.composeapp.generated.resources.profile_hedgehog_img
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
import uz.tikoncha_parent.domain.model.SubscriptionType
import uz.tikoncha_parent.platform.openAppSettings
import uz.tikoncha_parent.platform.openLocationSettings
import uz.tikoncha_parent.platform.openUrl
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.SubscriptionBottomDialog
import uz.tikoncha_parent.presentation.map.OnScreenActive
import uz.tikoncha_parent.presentation.map2.CameraPosition
import uz.tikoncha_parent.presentation.map2.LatLng
import uz.tikoncha_parent.presentation.map2.MapController
import uz.tikoncha_parent.presentation.map2.MapMarker
import uz.tikoncha_parent.presentation.map2.MarkerStyle
import uz.tikoncha_parent.presentation.map2.NativeMarkerIcon
import uz.tikoncha_parent.presentation.map2.YandexMap
import uz.tikoncha_parent.presentation.map2.createMarkerIcon
import uz.tikoncha_parent.presentation.map2.rememberMapController
import uz.tikoncha_parent.presentation.profile.subscription.subscription_payment.SubscriptionPaymentScreen
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.rememberIsDarkTheme
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

private val ANDIJAN_CENTER = LatLng(40.7821, 72.3442)

class TrackingScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<TrackingScreenModel>()
        val state by screenModel.state.collectAsState()
        val event = screenModel::onEvent
        val mapController = rememberMapController()
        val snackbarHostState = remember { SnackbarHostState() }
        val navigator = LocalNavigator.current

        val permissionsController: PermissionsController = koinInject()
        val locationTracker: LocationTracker = koinInject()

        BindEffect(permissionsController)
        BindLocationTrackerEffect(locationTracker)

        var cameFromSettings by remember { mutableStateOf(false) }


        OnScreenActive(
            launchedToSettings = cameFromSettings,
            onReturned = {
                cameFromSettings = false
                screenModel.onEvent(TrackingEvent.RecheckPermission)
            }
        )

        var showPermissionDialog by remember { mutableStateOf(false) }
        var showPermissionAlwaysDialog by remember { mutableStateOf(false) }
        var showGpsDialog by remember { mutableStateOf(false) }

        // ⬇️ YANGI — Self uchun custom icon (Yandex UserLocationLayer'ga uzatiladi)
        val density = LocalDensity.current
        val textMeasurer = rememberTextMeasurer()
        val placeholderBitmap = imageResource(Res.drawable.profile_hedgehog_img)
        val selfText = stringResource(Res.string.siz)

        var userLocationIcon by remember { mutableStateOf<NativeMarkerIcon?>(null) }

        // Self icon'ni bir marta yaratamiz (lokalizatsiya o'zgarsa qayta yaratiladi)
        LaunchedEffect(selfText) {
            userLocationIcon = createMarkerIcon(
                style = MarkerStyle.Self(text = selfText),
                density = density,
                textMeasurer = textMeasurer
            )
        }

        // Effect collect — TUZATILGAN versiya
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

                    // ⬇️ TUZATILGAN — for + break ishlaydi (return@repeat ishlamaydi)
                    TrackingEffect.MoveToUserLocation -> {
                        // Yandex GPS aniqlashi 1-3 soniya olishi mumkin
                        // Muvaffaqiyatli bo'lguncha urinish, keyin TO'XTAYDI
                        for (attempt in 0 until 10) {
                            delay(500)
                            if (mapController.tryMoveToUserLocation(animated = true)) {
                                break  // ⬅️ MUHIM: birinchi muvaffaqiyatda chiqamiz
                            }
                        }
                    }

                    is TrackingEffect.OpenUrl -> {
                        openUrl(eff.url)
                    }
                }
            }
        }

        LaunchedEffect(state.permissionDenied, state.permissionDeniedAlways) {
            showPermissionDialog = state.permissionDenied && !state.permissionDeniedAlways
            showPermissionAlwaysDialog = state.permissionDeniedAlways
        }

        LaunchedEffect(state.showGpsDialog) {
            showGpsDialog = state.showGpsDialog
        }

        TrackingContent(
            state = state,
            mapController = mapController,
            snackbarHostState = snackbarHostState,
            placeholderBitmap = placeholderBitmap,
            userLocationIcon = userLocationIcon,  // ⬅️ UZATAMIZ
            onEvent = event,
            onBack = {
                navigator?.pop()
            }
        )

        // ============== DIALOGLAR (avvalgidek o'zgarmaydi) ==============

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
                showPermissionDialog = false
                screenModel.onEvent(TrackingEvent.DismissPermissionDialog)
            }
        )

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
                screenModel.onEvent(TrackingEvent.DismissPermissionDialog)
            },
            onDismiss = {
                showPermissionAlwaysDialog = false
                screenModel.onEvent(TrackingEvent.DismissPermissionDialog)
            }
        )

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

        PersonInfoSheet(
            show = state.showPersonSheet,
            onDismiss = {event(TrackingEvent.DismissPersonSheet)},
            person = state.sheetPerson,
            issues = state.sheetIssues,
            isCheckingStatus = state.isCheckingPermissionStatus,
            onWatchVideo = {
                event(TrackingEvent.OpenYoutubeUrl(it))
            },
        )

        SubscriptionBottomDialog(
            show = state.showSubscriptionDialog,
            title = stringResource(Res.string.plus_obnuna_kerak),  // yoki boshqa string
            message = stringResource(Res.string.farzandingizni_qayerda_ekanini_kuzatish), // yangi string
            onConfirm = {
                screenModel.onEvent(TrackingEvent.DismissSubscriptionDialog)
                navigator?.push(SubscriptionPaymentScreen())
            },
            onDismiss = {
                screenModel.onEvent(TrackingEvent.DismissSubscriptionDialog)
            }
        )
    }
}

@Composable
fun TrackingContent(
    state: TrackingState,
    mapController: MapController,
    snackbarHostState: SnackbarHostState,
    onEvent: (TrackingEvent) -> Unit,
    placeholderBitmap: ImageBitmap,
    userLocationIcon: NativeMarkerIcon?,
    onBack: () -> Unit
) {
    val isDark = rememberIsDarkTheme()
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

    // ⬇️ TUZATILGAN — Self markerini OLIB TASHLAYMIZ
    // Self'ni Yandex UserLocationLayer chizadi (real-time GPS bilan)

    val markers = remember(
        state.people,
        state.selectedPersonId,
        state.subscriptionLimits,         // ⬅️ YANGI
        placeholderBitmap
    ) {
        state.people
            .filter { !it.isSelf }
            .filter { person ->            // ⬅️ YANGI: faqat PLUS bolalar
                val limit = state.subscriptionLimits.firstOrNull { it.childId == person.id }
                limit != null && limit.subscriptionType != SubscriptionType.FREE
            }
            .mapNotNull { person ->
                val loc = person.location ?: return@mapNotNull null
                val style = if (person.id == state.selectedPersonId) {
                    MarkerStyle.ChildSelected(
                        label = person.name,
                        avatarUrl = person.avatarUrl,
                        placeholderBitmap = placeholderBitmap
                    )
                } else {
                    MarkerStyle.Child(
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

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.page.copy(alpha = 0.3f),
        navigationBarColor = AppColors.bg.page.copy(alpha = 0.3f)
    )

    Box(Modifier.then(systemBars.modifier).fillMaxSize()) {

        YandexMap(
            controller = mapController,
            initialCamera = CameraPosition(initialCenter, zoom = initialZoom),
            markers = markers,
            onMarkerClick = { id -> onEvent(TrackingEvent.MarkerClicked(id)) },
            showUserLocation = state.userLocationEnabled,
            userLocationIcon = userLocationIcon,    // ⬅️ MUHIM — custom indicator
            modifier = Modifier.fillMaxSize(),
            isDark = isDark
        )

        FloatingActionButton(
            onClick = {
                onBack()
            },
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 8.dp, start = 16.dp)
                .size(52.dp),
            containerColor = AppColors.modal.primary,
            contentColor = AppColors.icon.primary
        ) {
            Icon(
                painter = painterResource(Res.drawable.arrow_back),
                contentDescription = ""
            )
        }

        FloatingActionButton(
            onClick = { onEvent(TrackingEvent.SelfClicked) },
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 70.dp)
                .size(52.dp),
            containerColor = AppColors.modal.primary,
            contentColor = AppColors.icon.primary
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
                modifier = Modifier.padding(8.dp)
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
            error = painterResource(Res.drawable.profile_hedgehog_img),
            fallback = painterResource(Res.drawable.profile_hedgehog_img),
            modifier = Modifier.fillMaxSize(),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}