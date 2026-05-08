@file:OptIn(ExperimentalMaterial3Api::class)

package uz.tikoncha_parent.presentation.policy.location_rule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.domain.model.GeoType
import uz.tikoncha_parent.domain.model.LocationData
import uz.tikoncha_parent.platform.openAppSettings
import uz.tikoncha_parent.platform.openLocationSettings
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomSwitch
import uz.tikoncha_parent.presentation.base.OnScreenActive
import uz.tikoncha_parent.presentation.map.*
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.ui.theme.*

private val DEFAULT_CENTER = LatLng(40.7821, 72.3442)

private fun Color.toArgbLong(): Long = this.toArgb().toLong() and 0xFFFFFFFFL

class LocationRuleScreen : Screen {

    @Composable
    override fun Content() {


        val navigator = LocalNavigator.current

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()
        val sharedEvent = sharedViewModel::onEvent

        val screenModel = koinScreenModel<LocationRuleScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle()

        val mapController = rememberMapController()

        val permissionsController: PermissionsController = koinInject()
        val locationTracker: LocationTracker = koinInject()
        BindEffect(permissionsController)
        BindLocationTrackerEffect(locationTracker)

        // ⬇️ YANGI — settings'dan qaytganda recheck
        var cameFromSettings by remember { mutableStateOf(false) }
        OnScreenActive(
            launchedToSettings = cameFromSettings,
            onReturned = {
                cameFromSettings = false
                screenModel.onEvent(LocationRuleEvent.RecheckPermission)
            }
        )

        // ⬇️ YANGI — dialog visibility
        var showPermissionDialog by remember { mutableStateOf(false) }
        var showPermissionAlwaysDialog by remember { mutableStateOf(false) }
        var showGpsDialog by remember { mutableStateOf(false) }

        LaunchedEffect(state.permissionDenied, state.permissionDeniedAlways) {
            showPermissionDialog = state.permissionDenied && !state.permissionDeniedAlways
            showPermissionAlwaysDialog = state.permissionDeniedAlways
        }
        LaunchedEffect(state.showGpsDialog) {
            showGpsDialog = state.showGpsDialog
        }

        // Init bir marta
        LaunchedEffect(Unit) {
            screenModel.onEvent(
                LocationRuleEvent.Init(
                    rule = sharedState.locationRule,
                    canUpdate = sharedState.canUpdate,
                )
            )
        }

        // Effects
        LaunchedEffect(Unit) {
            var initialDone = false
            screenModel.effect.collect { eff ->
                when (eff) {
                    is LocationRuleEffect.MoveCamera -> {
                        if (!initialDone) delay(300)
                        mapController.moveTo(CameraPosition(eff.target, eff.zoom))
                    }
                    is LocationRuleEffect.FitBounds -> {
                        if (!initialDone) delay(300)
                        mapController.fitBounds(eff.points)
                    }
                    is LocationRuleEffect.SaveResult -> {
                        sharedEvent(PolicySharedEvent.SetLocationRule(eff.rule))
                        navigator?.pop()
                    }
                    LocationRuleEffect.NavigateBack -> navigator?.pop()
                    LocationRuleEffect.OpenAppSettings -> openAppSettings()    // ⬅️ YANGI
                }
                initialDone = true
            }
        }

        LocationRuleContent(
            state = state,
            policyTitle = sharedState.policyTitle,
            mapController = mapController,
            onEvent = screenModel::onEvent,
            onBack = { navigator?.pop() },
        )

        // ⬇️ YANGI — DIALOGLAR (Tracking'dagidek)

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
                screenModel.onEvent(LocationRuleEvent.RequestLocationPermission)
            },
            onDismiss = {
                showPermissionDialog = false
                screenModel.onEvent(LocationRuleEvent.DismissPermissionDialog)
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
                screenModel.onEvent(LocationRuleEvent.OpenAppSettings)
                screenModel.onEvent(LocationRuleEvent.DismissPermissionDialog)
            },
            onDismiss = {
                showPermissionAlwaysDialog = false
                screenModel.onEvent(LocationRuleEvent.DismissPermissionDialog)
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
                screenModel.onEvent(LocationRuleEvent.GpsEnabledByUser)
            },
            onDismiss = {
                showGpsDialog = false
                screenModel.onEvent(LocationRuleEvent.DismissGpsDialog)
            }
        )


        LocationRuleContent(
            state = state,
            policyTitle = sharedState.policyTitle,
            mapController = mapController,
            onEvent = screenModel::onEvent,
            onBack = { navigator?.pop() },
        )
    }
}

@Composable
private fun LocationRuleContent(
    state: LocationRuleState,
    policyTitle: String,
    mapController: MapController,
    onEvent: (LocationRuleEvent) -> Unit,
    onBack: () -> Unit,
) {
    val isDark = rememberIsDarkTheme()

    val initialCenter = remember {
        state.center
            ?: state.polygon.firstOrNull()?.let { LatLng(it.lat, it.lng) }
            ?: state.userLocation
            ?: DEFAULT_CENTER
    }

    val dangerColor = AppColors.icon.accentDanger
    val circleFillColor = dangerColor.copy(alpha = 0.2f).toArgbLong()
    val circleStrokeColor = dangerColor.toArgbLong()
    val accuracyFillColor = dangerColor.copy(alpha = 0.20f).toArgbLong()
    val accuracyStrokeColor = dangerColor.copy(alpha = 0.40f).toArgbLong()

    val userAccuracyCircle = remember(
        state.userLocation,
        accuracyFillColor,
        accuracyStrokeColor,
    ) {
        if (state.userLocation != null) {
            listOf(
                MapCircle(
                    id = "user_accuracy",
                    center = state.userLocation,
                    radiusMeters = 30.0, // fixed accuracy
                    reverse = false,
                    fillColor = accuracyFillColor,
                    strokeColor = accuracyStrokeColor,
                    strokeWidthDp = 1f,
                )
            )
        } else emptyList()
    }


    val circles = remember(
        state.center, state.radiusMeters, state.reverse, state.geoType,
        circleFillColor, circleStrokeColor
    ) {
        if (state.geoType == GeoType.CIRCLE && state.center != null) {
            listOf(
                MapCircle(
                    id = "rule_circle",
                    center = state.center,
                    radiusMeters = state.radiusMeters.toDouble(),
                    reverse = state.reverse,
                    fillColor = circleFillColor,
                    strokeColor = circleStrokeColor,
                    strokeWidthDp = 3f,
                )
            )
        } else emptyList()
    }

    val allCircles = circles + userAccuracyCircle


    val polygons = remember(
        state.polygon, state.reverse, state.geoType,
        circleFillColor, circleStrokeColor
    ) {
        if (state.geoType == GeoType.POLYGON && state.polygon.size >= 3) {
            listOf(
                MapPolygon(
                    id = "rule_polygon",
                    points = state.polygon.map { LatLng(it.lat, it.lng) },
                    reverse = state.reverse,
                    fillColor = circleFillColor,
                    strokeColor = circleStrokeColor,
                    strokeWidthDp = 3f,
                )
            )
        } else emptyList()
    }


    // ── USER LOCATION MARKER (custom Yandex Maps style) ──
    val markers = remember(state.userLocation) {
        state.userLocation?.let { loc ->
            listOf(
                MapMarker(
                    id = "user_location",
                    position = loc,
                    style = MarkerStyle.Self(),
                    zIndex = 100f,
                )
            )
        } ?: emptyList()
    }



    // Radius o'zgarganda zoom moslash

    LaunchedEffect(state.autoFocusTarget) {
        val target = state.autoFocusTarget ?: return@LaunchedEffect
        mapController.awaitReady()
        mapController.moveTo(CameraPosition(target, 16f))
        onEvent(LocationRuleEvent.AutoFocusConsumed)
    }

    LaunchedEffect(state.autoFitBoundsTarget) {
        val pts = state.autoFitBoundsTarget ?: return@LaunchedEffect
        mapController.awaitReady()
        mapController.fitBounds(pts)
        onEvent(LocationRuleEvent.AutoFocusConsumed)
    }

    LaunchedEffect(state.radiusMeters) {
        if (!state.canUpdate || state.geoType != GeoType.CIRCLE) return@LaunchedEffect
        delay(200)
        val targetZoom = zoomForCircleRadius(state.radiusMeters)
        val currentPos = mapController.getCameraPosition() ?: return@LaunchedEffect
        if (currentPos.zoom > targetZoom + 0.5f) {
            mapController.moveTo(
                CameraPosition(currentPos.target, targetZoom),
                animated = true
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
            initialCamera = CameraPosition(initialCenter, zoom = 16f),
            markers = markers,
            circles = allCircles,
            polygons = polygons,
            onCameraIdle = { onEvent(LocationRuleEvent.CameraIdle(it)) },
            isDark = isDark,
            modifier = Modifier.fillMaxSize(),
        )

        // ── Markaziy PIN (rule center) ──
        if (state.showCenterPin) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = AppColors.icon.accentDanger,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
                    .offset(y = (-24).dp)
            )
        }

        // ── Top: Back + policyTitle ──
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 8.dp, start = 16.dp, end = 80.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FloatingActionButton(
                onClick = onBack,
                shape = CircleShape,
                modifier = Modifier.size(52.dp),
                containerColor = AppColors.modal.primary,
                contentColor = AppColors.icon.primary,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back),
                    contentDescription = null,
                )
            }

            if (policyTitle.isNotBlank()) {
                Spacer(Modifier.width(12.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AppColors.modal.primary,
                    tonalElevation = 4.dp,
                ) {
                    Text(
                        text = policyTitle,
                        style = AppTypography.titleMdSemiBold,
                        color = AppColors.text.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // ── Locate-me FAB ──
        if (state.canUpdate) {
            FloatingActionButton(
                onClick = {
                    if (!state.isLocating) onEvent(LocationRuleEvent.LocateMe)
                },
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 16.dp)
                    .size(52.dp),
                containerColor = AppColors.modal.primary,
                contentColor = AppColors.icon.primary,
            ) {
                if (state.isLocating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = AppColors.icon.primary,
                    )
                } else {
                    Icon(Icons.Default.MyLocation, contentDescription = null)
                }
            }
        }

        // ── Bottom panel ──
        if (state.showBottomPanel) {
            BottomPanel(
                state = state,
                onEvent = onEvent,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun BottomPanel(
    state: LocationRuleState,
    onEvent: (LocationRuleEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = AppColors.bg.elevated,
        tonalElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            if (state.showRadiusSlider) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(Res.string.radius),
                        style = AppTypography.titleMdMedium,
                        color = AppColors.text.secondary,
                    )
                    Text(
                        text = "${state.radiusMeters} m",
                        style = AppTypography.titleMdSemiBold,
                        color = AppColors.text.primary,
                    )
                }

                Spacer(Modifier.height(4.dp))

                val sliderColors = SliderDefaults.colors(
                    activeTrackColor = AppColors.bg.primary,
                    inactiveTrackColor = AppColors.border.disabled,
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent,
                )

                Slider(
                    value = state.radiusMeters.toFloat(),
                    onValueChange = { onEvent(LocationRuleEvent.RadiusChanged(it.toInt())) },
                    valueRange = state.radiusMin.toFloat()..state.radiusMax.toFloat(),
                    colors = sliderColors,
                    thumb = {
                        Box(
                            modifier = Modifier.size(32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(AppColors.bg.primary.copy(alpha = 0.2f), CircleShape)
                            )
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .shadow(2.dp, CircleShape)
                                    .background(AppColors.bg.primary, CircleShape)
                            )
                        }
                    },
                    track = { sliderState ->
                        SliderDefaults.Track(
                            sliderState = sliderState,
                            colors = sliderColors,
                            thumbTrackGapSize = 0.dp,
                            trackInsideCornerSize = 0.dp,
                            drawStopIndicator = null,
                        )
                    },
                )
                Spacer(Modifier.height(4.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.teskari_radius),
                    style = AppTypography.titleMdMedium,
                    color = AppColors.text.primary,
                )
                CustomSwitch(
                    checked = state.reverse,
                    onCheckedChange = { onEvent(LocationRuleEvent.ReverseChanged(it)) },
                )
            }

            Spacer(Modifier.height(20.dp))

            CustomButtonNew(
                text = stringResource(Res.string.saqlash),
                onClick = { onEvent(LocationRuleEvent.Save) },
                enabled = state.canSave,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

// ─────────────────────────────────────────────
// PREVIEWS
// ─────────────────────────────────────────────

@Preview
@Composable
fun LocationRuleContentPreview_Empty() {
    TikonchaParentTheme {
        LocationRuleContent(
            state = LocationRuleState(
                canUpdate = true,
                geoType = GeoType.CIRCLE,
                center = LatLng(40.7821, 72.3442),
                radiusMeters = 200,
                reverse = false,
            ),
            policyTitle = "Maktab vaqti",
            mapController = rememberMapController(),
            onEvent = {},
            onBack = {},
        )
    }
}

@Preview
@Composable
fun LocationRuleContentPreview_Reverse() {
    TikonchaParentTheme {
        LocationRuleContent(
            state = LocationRuleState(
                canUpdate = true,
                geoType = GeoType.CIRCLE,
                center = LatLng(40.7821, 72.3442),
                radiusMeters = 500,
                reverse = true,
            ),
            policyTitle = "Tashqi zona",
            mapController = rememberMapController(),
            onEvent = {},
            onBack = {},
        )
    }
}

@Preview
@Composable
fun LocationRuleContentPreview_ReadOnly() {
    TikonchaParentTheme {
        LocationRuleContent(
            state = LocationRuleState(
                canUpdate = false,
                geoType = GeoType.CIRCLE,
                center = LatLng(40.7821, 72.3442),
                radiusMeters = 300,
                reverse = false,
            ),
            policyTitle = "Ota-ona qoidasi",
            mapController = rememberMapController(),
            onEvent = {},
            onBack = {},
        )
    }
}

@Preview
@Composable
fun LocationRuleContentPreview_Polygon() {
    TikonchaParentTheme {
        LocationRuleContent(
            state = LocationRuleState(
                canUpdate = true,
                geoType = GeoType.POLYGON,
                center = null,
                polygon = listOf(
                    LocationData(40.7821, 72.3442),
                    LocationData(40.7900, 72.3500),
                    LocationData(40.7850, 72.3600),
                ),
                reverse = false,
            ),
            policyTitle = "Maktab hududi",
            mapController = rememberMapController(),
            onEvent = {},
            onBack = {},
        )
    }
}