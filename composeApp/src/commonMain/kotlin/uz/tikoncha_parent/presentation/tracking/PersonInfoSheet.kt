@file:OptIn(ExperimentalMaterial3Api::class)

package uz.tikoncha_parent.presentation.tracking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.profile_hedgehog_img
import tikoncha_parents.composeapp.generated.resources.tracking_checking_settings
import tikoncha_parents.composeapp.generated.resources.tracking_last_update
import tikoncha_parents.composeapp.generated.resources.tracking_location_unavailable
import tikoncha_parents.composeapp.generated.resources.tracking_watch_video
import tikoncha_parents.composeapp.generated.resources.ulashish
import tikoncha_parents.composeapp.generated.resources.xaritadan_ochish
import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusIssus
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.presentation.map.LatLng
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun PersonInfoSheet(
    show: Boolean,
    person: Person?,
    issues: List<PermissionStatusIssus> = emptyList(),
    isCheckingStatus: Boolean = false,
    onDismiss: () -> Unit,
    onWatchVideo: (String) -> Unit = {},
    onOpenInMaps: (Person) -> Unit = {},
    onShare: (Person) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (!show) return

    val hasLocation = person?.location != null
    val hasIssues = issues.isNotEmpty()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    LaunchedEffect(show) {
        if (show) sheetState.expand()
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        containerColor = Color.Transparent,
        dragHandle = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = ContainerPadding)
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.bg.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ContainerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Drag handle
                    Box(
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .background(color = Color(0xFF9A9A9A), shape = CircleShape)
                            .height(3.dp)
                            .width(36.dp)
                    )



                    // ⬇️ 2. PERSON INFO (avatar + ism + lastSeen)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(AppColors.bg.primaryContainer)
                        ) {
                            AsyncImage(
                                model = person?.avatarUrl,
                                placeholder = painterResource(Res.drawable.profile_hedgehog_img),
                                error = painterResource(Res.drawable.profile_hedgehog_img),
                                fallback = painterResource(Res.drawable.profile_hedgehog_img),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = person?.name ?: "",
                                style = AppTypography.titleMdSemiBold,
                                color = AppColors.text.primary
                            )

                            if (hasLocation) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = stringResource(Res.string.tracking_last_update),
                                        style = AppTypography.bodyMdMedium,
                                        color = AppColors.text.tertiary
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = formatLastSeen(person?.lastSeenEpochMs),
                                        style = AppTypography.bodyMdSemiBold,
                                        color = AppColors.text.accentEmphasis
                                    )
                                }
                            }
                        }
                    }

                    // ⬇️ 3. Location yo'q VA issue ham yo'q VA loading ham yo'q
                    if (!hasLocation && !hasIssues && !isCheckingStatus) {
                        Space(12.dp)
                        Text(
                            text = stringResource(Res.string.tracking_location_unavailable),
                            style = AppTypography.titleSmSemiBold,
                            color = AppColors.text.primary
                        )
                    }

                    // ⬇️ 4. LOADING — pastida kichik text + spinner
                    if (isCheckingStatus) {
                        Space(16.dp)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                strokeWidth = 2.dp,
                                color = AppColors.text.tertiary
                            )
                            Text(
                                // "Farzand sozlamalari tekshirilmoqda..."
                                text = stringResource(Res.string.tracking_checking_settings),
                                style = AppTypography.bodySmMedium,
                                color = AppColors.text.tertiary
                            )
                        }
                    }

                    if (hasIssues) {
                        Space(16.dp)
                        issues.forEach { issue ->
                            IssueWarningBlock(
                                issue = issue,
                                onWatchVideo = { onWatchVideo(issue.video_url?:"") }
                            )
                            Space(12.dp)
                        }
                    }

                    if (hasLocation) {
                        Space(16.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {

                            ActionButton(
                                text = stringResource(Res.string.xaritadan_ochish),
                                icon = Icons.Default.Map,
                                onClick = {onOpenInMaps(person)},
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                            ActionButton(
                                text = stringResource(Res.string.ulashish),
                                icon = Icons.Default.Share,
                                onClick = {onShare(person)},
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                    }



                    Space(10.dp)
                }
            }
        }
    }

}

/**
 * Warning blok — title + body + youtube video tugma.
 */
@Composable
private fun IssueWarningBlock(
    issue: PermissionStatusIssus,
    onWatchVideo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.bg.accentWarningContainer  // sariq och fon
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title
            Text(
                text = issue.title,
                style = AppTypography.titleSmSemiBold,
                color = AppColors.text.primary
            )

            // Body
            Text(
                text = issue.body,
                style = AppTypography.bodyMdMedium,
                color = AppColors.text.secondary
            )

            // Video tugma (URL bo'lsa)
            if (!issue.video_url.isNullOrBlank()) {
                Space(4.dp)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppColors.button.primary)
                        .clickable { onWatchVideo() }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        // "Video ko'rish"
                        text = stringResource(Res.string.tracking_watch_video),
                        style = AppTypography.bodyMdSemiBold,
                        color = Color.White
                    )
                }
            }

        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .border(1.dp, AppColors.border.secondarySubtle, RoundedCornerShape(20.dp))
            .singleClick{
                onClick()
            }
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(
            imageVector = icon,
            contentDescription = "",
            modifier = Modifier
                .size(32.dp),
            tint = AppColors.icon.accentPrimary
        )
        Text(
            text = text,
            style = AppTypography.titleSmMedium,
            color = AppColors.text.primary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun Pre_Loading() {
    TikonchaParentTheme {
        PersonInfoSheet(
            person = Person(
                id = "",
                name = "Ibroxim",
                location = LatLng(40.78, 72.34),
                isSelf = false,
                lastSeenEpochMs = 32323L,
                lastSeen = "12,021",
                avatarUrl = ""
            ),
            isCheckingStatus = true,
            onDismiss = {},
            show = true
        )
    }
}

@Preview
@Composable
fun Pre_WithIssue() {
    TikonchaParentTheme {
        PersonInfoSheet(
            person = Person(
                id = "",
                name = "Ibroxim",
                location = null,
                isSelf = false,
                lastSeenEpochMs = 32323L,
                lastSeen = "12,021",
                avatarUrl = ""
            ),
            issues = listOf(
                PermissionStatusIssus(
                    state = "monitor",
                    missing_permissions = listOf("location"),
                    title = "Joylashuvni sozlash kerak",
                    body = "Farzandingizning joylashuvini ko'rish uchun uning telefonidagi G'ujanak rejimini yoqish kerak.",
                    video_url = "https://youtu.be/NI1e38qD-Sg"
                )
            ),
            onDismiss = {},
            show = true
        )
    }
}