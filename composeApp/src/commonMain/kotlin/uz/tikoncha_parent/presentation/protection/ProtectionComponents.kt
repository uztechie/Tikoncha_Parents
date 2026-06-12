package uz.tikoncha_parent.presentation.protection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.berilgan
import tikoncha_parents.composeapp.generated.resources.berilmagan
import tikoncha_parents.composeapp.generated.resources.boshqa_rejimlar_uchun
import tikoncha_parents.composeapp.generated.resources.delete
import tikoncha_parents.composeapp.generated.resources.farzand_ilovadan_chiqish_sorovi
import tikoncha_parents.composeapp.generated.resources.farzand_ilovani_ochirish_sorovi
import tikoncha_parents.composeapp.generated.resources.farzand_qalqon_ochirish_sorovi
import tikoncha_parents.composeapp.generated.resources.farzand_sorovlari
import tikoncha_parents.composeapp.generated.resources.gujanak_uchun
import tikoncha_parents.composeapp.generated.resources.hisobdan_chiqish
import tikoncha_parents.composeapp.generated.resources.ilovani_ochirish
import tikoncha_parents.composeapp.generated.resources.jarayonda
import tikoncha_parents.composeapp.generated.resources.javob_uchun
import tikoncha_parents.composeapp.generated.resources.korsatish
import tikoncha_parents.composeapp.generated.resources.logout
import tikoncha_parents.composeapp.generated.resources.muddati_tugagan
import tikoncha_parents.composeapp.generated.resources.n_ta_yangi
import tikoncha_parents.composeapp.generated.resources.n_ta_zarur_ruxsat_ochiq
import tikoncha_parents.composeapp.generated.resources.ochirish_usuli
import tikoncha_parents.composeapp.generated.resources.oflayn
import tikoncha_parents.composeapp.generated.resources.onlayn
import tikoncha_parents.composeapp.generated.resources.qalqon_uchun
import tikoncha_parents.composeapp.generated.resources.qalqonni_ochirish_sorovi
import tikoncha_parents.composeapp.generated.resources.rad_etildi
import tikoncha_parents.composeapp.generated.resources.rad_etish
import tikoncha_parents.composeapp.generated.resources.ruxsat_berish
import tikoncha_parents.composeapp.generated.resources.ruxsatlar
import tikoncha_parents.composeapp.generated.resources.sinxron
import tikoncha_parents.composeapp.generated.resources.tasdiqlandi
import tikoncha_parents.composeapp.generated.resources.tasdiqlash
import tikoncha_parents.composeapp.generated.resources.tasdiqlasangiz_qalqon_ochadi
import tikoncha_parents.composeapp.generated.resources.x_dan_y_berilgan
import tikoncha_parents.composeapp.generated.resources.yashirish
import tikoncha_parents.composeapp.generated.resources.zarur_ruxsat_banner_desc
import uz.tikoncha_parent.data.remote.model.protection.AccountRequestDto
import uz.tikoncha_parent.data.remote.model.protection.ChildRequestDto
import uz.tikoncha_parent.domain.model.protection.AccountRequestAction
import uz.tikoncha_parent.domain.model.protection.ChildMode
import uz.tikoncha_parent.domain.model.protection.ChildPermission
import uz.tikoncha_parent.domain.model.protection.StrictMethod
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.DividerHorizontal
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography

// ═══════════════════════ Umumiy karta ═══════════════════════

@Composable
private fun ProtectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AppColors.bg.surface)
            .padding(16.dp),
        content = content,
    )
}

/** Farzand ismini accent rang bilan ajratib beradi */
@Composable
private fun highlightedChildText(fullText: String, childName: String): AnnotatedString =
    buildAnnotatedString {
        append(fullText)
        if (childName.isNotEmpty()) {
            val start = fullText.indexOf(childName)
            if (start >= 0) {
                addStyle(
                    style = AppTypography.titleSmSemiBold
                        .copy(color = AppColors.text.accentEmphasis)
                        .toSpanStyle(),
                    start = start,
                    end = start + childName.length,
                )
            }
        }
    }

/** Eski kartadagi kabi to'liq rangli status pill (oq matn) */
@Composable
private fun SolidStatusPill(text: String, bgColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 3.dp),
    ) {
        Text(
            text = text,
            style = AppTypography.bodyMdMedium,
            color = AppColors.text.inverse,
        )
    }
}

// ═══════════════════════ HERO KARTA ═══════════════════════

@Composable
fun ProtectionHeroCard(
    state: ProtectionState,
    event: (ProtectionEvent) -> Unit,
) {
    ProtectionCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(state.mode.title()),
                    style = AppTypography.titleLgSemiBold,
                    color = AppColors.text.primary,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(state.mode.desc()),
                    style = AppTypography.emphasizedSmRegular,
                    color = AppColors.text.tertiary,
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(AppColors.bg.primaryContainer)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
            ) {
                Text(
                    text = stringResource(state.mode.levelLabel()),
                    style = AppTypography.emphasizedXsMedium,
                    color = AppColors.text.accentEmphasis,
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = AppColors.border.secondary)
        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(Res.string.sinxron) + " " +
                        relativeTimeText(state.lastSyncAt),
                style = AppTypography.emphasizedSmRegular,
                color = AppColors.text.tertiary,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (state.isOnline) AppColors.text.accentSuccess
                        else AppColors.text.tertiary
                    ),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = stringResource(
                    if (state.isOnline) Res.string.onlayn else Res.string.oflayn
                ),
                style = AppTypography.emphasizedSmMedium,
                color = if (state.isOnline) AppColors.text.accentSuccess
                else AppColors.text.tertiary,
            )
        }

        val method = state.strictMethod
        if (state.mode == ChildMode.STRICT && method != null) {
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = AppColors.border.secondary)
            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(Res.string.ochirish_usuli),
                    style = AppTypography.emphasizedSmRegular,
                    color = AppColors.text.tertiary,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = stringResource(method.title()),
                    style = AppTypography.emphasizedSmSemiBold,
                    color = AppColors.text.primary,
                )
            }

            val code = state.unlockData
            if (method == StrictMethod.SECRET_CODE && !code.isNullOrEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(AppColors.field.secondary)
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = if (state.isCodeVisible) code else "••••",
                            style = AppTypography.titleMdSemiBold,
                            color = AppColors.text.primary,
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AppColors.bg.secondaryContainer)
                            .clickableNoRipple { event(ProtectionEvent.ToggleCodeVisibility) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = stringResource(
                                if (state.isCodeVisible) Res.string.yashirish
                                else Res.string.korsatish
                            ),
                            style = AppTypography.emphasizedSmSemiBold,
                            color = AppColors.text.accentEmphasis,
                        )
                    }
                }
            }
        }
    }
}

// ═══════════════════════ SO'ROVLAR KARTASI ═══════════════════════


@Composable
private fun RequestItemContainer(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AppColors.bg.tertiary)
            .padding(12.dp),
        content = content,
    )
}



@Composable
fun ProtectionRequestsCard(
    state: ProtectionState,
    childName: String,
    onApproveStrict: () -> Unit,
    onRejectStrict: (String) -> Unit,
    onAllowAccount: (AccountRequestAction) -> Unit,
    onDenyAccount: (AccountRequestAction) -> Unit,
) {
    ProtectionCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(Res.string.farzand_sorovlari),
                style = AppTypography.titleMdSemiBold,
                color = AppColors.text.primary,
                modifier = Modifier.weight(1f),
            )
            if (state.pendingRequestCount > 0) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(AppColors.bg.accentWarningContainer)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.n_ta_yangi, state.pendingRequestCount),
                        style = AppTypography.emphasizedXsMedium,
                        color = AppColors.text.accentWarning,
                    )
                }
            }
        }

        var firstItem = true

        // ── 1. Qalqonni o'chirish so'rovi ──
        val strictRequest = state.strictDisableRequest
        if (strictRequest != null) {
            Space(12.dp)
            RequestItemContainer {
                StrictRequestItem(
                    request = strictRequest,
                    childName = strictRequest.childName ?: childName,
                    isPending = state.isStrictRequestPending,
                    remainingSeconds = state.remainingSeconds,
                    isProcessing = state.actionInProgressId == strictRequest.id,
                    onApprove = onApproveStrict,
                    onReject = { strictRequest.id?.let(onRejectStrict) },
                )
            }
            firstItem = false
        }

        // ── 2. Hisobdan chiqish ──
        if (state.isLogoutRequestPending) {
            Space(if (firstItem) 12.dp else 10.dp)
            RequestItemContainer {
                AccountRequestItem(
                    request = state.logoutRequest,
                    childName = childName,
                    title = stringResource(Res.string.hisobdan_chiqish),
                    subtitle = stringResource(
                        Res.string.farzand_ilovadan_chiqish_sorovi, childName
                    ),
                    isProcessing = state.actionInProgressId == state.logoutRequest?.id,
                    onAllow = { onAllowAccount(AccountRequestAction.LOGOUT) },
                    onDeny = { onDenyAccount(AccountRequestAction.LOGOUT) },
                )
            }
            firstItem = false
        }

        // ── 3. Ilovani o'chirish ──
        if (state.isDeleteRequestPending) {
            Space(if (firstItem) 12.dp else 10.dp)
            RequestItemContainer {
                AccountRequestItem(
                    request = state.deleteRequest,
                    childName = childName,
                    title = stringResource(Res.string.ilovani_ochirish),
                    subtitle = stringResource(
                        Res.string.farzand_ilovani_ochirish_sorovi, childName
                    ),
                    isProcessing = state.actionInProgressId == state.deleteRequest?.id,
                    onAllow = { onAllowAccount(AccountRequestAction.DELETE) },
                    onDeny = { onDenyAccount(AccountRequestAction.DELETE) },
                )
            }
        }
    }
}

@Composable
private fun StrictRequestItem(
    request: ChildRequestDto,
    childName: String,
    isPending: Boolean,
    remainingSeconds: Int,
    isProcessing: Boolean,
    onApprove: () -> Unit,
    onReject: () -> Unit,
) {
    val approved = request.status.equals("approved", ignoreCase = true)
    val rejected = request.status.equals("rejected", ignoreCase = true)

    Column {
        // Sarlavha + status pill
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = if (isPending) AppColors.icon.accentDanger
                else AppColors.icon.secondary,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(Res.string.qalqonni_ochirish_sorovi),
                style = AppTypography.titleMdSemiBold,
                color = AppColors.text.primary,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(8.dp))
            when {
                isPending -> SolidStatusPill(
                    text = stringResource(Res.string.jarayonda),
                    bgColor = AppColors.bg.accentWarning,
                )
                approved -> SolidStatusPill(
                    text = stringResource(Res.string.tasdiqlandi),
                    bgColor = AppColors.text.accentSuccess,
                )
                rejected -> SolidStatusPill(
                    text = stringResource(Res.string.rad_etildi),
                    bgColor = AppColors.bg.accentDanger,
                )
                else -> SolidStatusPill(
                    text = stringResource(Res.string.muddati_tugagan),
                    bgColor = AppColors.text.tertiary,
                )
            }
        }
        Space(12.dp)

        // Tavsif — farzand ismi ajratilgan
        Text(
            text = highlightedChildText(
                fullText = stringResource(Res.string.farzand_qalqon_ochirish_sorovi, childName),
                childName = childName,
            ),
            style = AppTypography.titleSmMedium,
            color = AppColors.text.secondary,
        )

        if (isPending) {
            Space(6.dp)
            Text(
                text = stringResource(Res.string.tasdiqlasangiz_qalqon_ochadi),
                style = AppTypography.bodyMdMedium,
                color = AppColors.text.accentWarning,
            )
        }
        Space(8.dp)

        // Countdown (chap) + sana (o'ng)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isPending && remainingSeconds > 0) {
                Text(
                    text = stringResource(
                        Res.string.javob_uchun, formatCountdown(remainingSeconds)
                    ),
                    style = AppTypography.bodyMdMedium,
                    color = if (remainingSeconds < 60) AppColors.text.accentDanger
                    else AppColors.text.accentWarning,
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = formatDateShort(parseInstantOrNull(request.createdAt)),
                style = AppTypography.bodySmMedium,
                color = AppColors.text.placeholder,
            )
        }

        if (isPending) {
            Space(8.dp)
            DividerHorizontal()
            Space(8.dp)
            RequestActionButtons(
                isProcessing = isProcessing,
                allowText = stringResource(Res.string.tasdiqlash),
                denyText = stringResource(Res.string.rad_etish),
                onAllow = onApprove,
                onDeny = onReject,
            )
        }
    }
}

@Composable
private fun AccountRequestItem(
    request: AccountRequestDto?,
    childName: String,
    title: String,
    subtitle: String,
    isProcessing: Boolean,
    onAllow: () -> Unit,
    onDeny: () -> Unit,
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(
                    if (request?.action == "delete") Res.drawable.delete
                    else Res.drawable.logout
                ),
                contentDescription = null,
                tint = AppColors.icon.accentWarning,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                style = AppTypography.titleMdSemiBold,
                color = AppColors.text.primary,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(8.dp))
            SolidStatusPill(
                text = stringResource(Res.string.jarayonda),
                bgColor = AppColors.bg.accentWarning,
            )
        }
        Space(12.dp)

        Text(
            text = highlightedChildText(fullText = subtitle, childName = childName),
            style = AppTypography.titleSmMedium,
            color = AppColors.text.secondary,
        )
        Space(8.dp)

        Text(
            text = formatDateShort(parseInstantOrNull(request?.createdAt)),
            style = AppTypography.bodySmMedium,
            color = AppColors.text.placeholder,
            modifier = Modifier.align(Alignment.End),
        )

        Space(8.dp)
        DividerHorizontal()
        Space(8.dp)

        RequestActionButtons(
            isProcessing = isProcessing,
            allowText = stringResource(Res.string.ruxsat_berish),
            denyText = stringResource(Res.string.rad_etish),
            onAllow = onAllow,
            onDeny = onDeny,
        )
    }
}

@Composable
private fun RequestActionButtons(
    isProcessing: Boolean,
    allowText: String,
    denyText: String,
    onAllow: () -> Unit,
    onDeny: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CustomButtonNew(
            text = denyText,
            enabled = !isProcessing,
            containerColor = AppColors.section.section,
            contentColor = AppColors.text.primary,
            onClick = onDeny,
            modifier = Modifier
                .weight(1f)
                .height(DialogButtonHeight),
        )
        Box(modifier = Modifier.weight(1f)) {
            CustomButtonNew(
                text = allowText,
                enabled = !isProcessing,
                onClick = onAllow,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DialogButtonHeight),
            )
            if (isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(16.dp),
                    color = AppColors.icon.inverse,
                    strokeWidth = 2.dp,
                )
            }
        }
    }
}

// ═══════════════════ RUXSAT YO'Q — WARNING BANNER ═══════════════════

@Composable
fun MissingPermissionsBanner(missing: List<ChildPermission>) {
    val names = ArrayList<String>(missing.size)
    for (permission in missing) {
        names.add(stringResource(permission.label()))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.bg.accentWarningContainer)
            .border(1.dp, AppColors.border.accentWarning, RoundedCornerShape(16.dp))
            .padding(14.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = AppColors.icon.accentWarning,
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                text = stringResource(Res.string.n_ta_zarur_ruxsat_ochiq, missing.size),
                style = AppTypography.emphasizedSmSemiBold,
                color = AppColors.text.primary,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = names.joinToString(", ") + ". " +
                        stringResource(Res.string.zarur_ruxsat_banner_desc),
                style = AppTypography.emphasizedXsRegular,
                color = AppColors.text.secondary,
            )
        }
    }
}

// ═══════════════════════ RUXSATLAR KARTASI ═══════════════════════

@Composable
fun ProtectionPermissionsCard(state: ProtectionState) {
    ProtectionCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(Res.string.ruxsatlar),
                style = AppTypography.titleMdSemiBold,
                color = AppColors.text.primary,
                modifier = Modifier.weight(1f),
            )
            if (state.requiredPermissions.isNotEmpty()) {
                Text(
                    text = stringResource(
                        Res.string.x_dan_y_berilgan,
                        state.grantedRequiredCount,
                        state.requiredPermissions.size,
                    ),
                    style = AppTypography.emphasizedSmMedium,
                    color = if (state.missingRequiredPermissions.isEmpty())
                        AppColors.text.accentSuccess
                    else AppColors.text.accentWarning,
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        for (permission in state.requiredPermissions) {
            PermissionRow(
                permission = permission,
                isGranted = permission in state.enabledPermissions,
            )
        }

        if (state.higherTierPermissions.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = AppColors.border.secondary)
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.boshqa_rejimlar_uchun),
                style = AppTypography.emphasizedXsRegular,
                color = AppColors.text.tertiary,
            )
            Spacer(Modifier.height(4.dp))

            for (permission in state.higherTierPermissions) {
                HigherTierPermissionRow(permission = permission)
            }
        }
    }
}

@Composable
private fun PermissionRow(permission: ChildPermission, isGranted: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(permission.label()),
            style = AppTypography.emphasizedSmMedium,
            color = AppColors.text.primary,
            modifier = Modifier.weight(1f),
        )
        StatusPill(
            text = stringResource(
                if (isGranted) Res.string.berilgan else Res.string.berilmagan
            ),
            color = if (isGranted) AppColors.text.accentSuccess
            else AppColors.text.accentWarning,
        )
    }
}

@Composable
private fun HigherTierPermissionRow(permission: ChildPermission) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(permission.label()),
            style = AppTypography.emphasizedSmMedium,
            color = AppColors.text.tertiary,
            modifier = Modifier.weight(1f),
        )
        StatusPill(
            text = stringResource(
                if (permission.minMode == ChildMode.STRICT) Res.string.qalqon_uchun
                else Res.string.gujanak_uchun
            ),
            color = AppColors.text.tertiary,
        )
    }
}

@Composable
private fun StatusPill(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = text,
            style = AppTypography.emphasizedXsMedium,
            color = color,
        )
    }
}