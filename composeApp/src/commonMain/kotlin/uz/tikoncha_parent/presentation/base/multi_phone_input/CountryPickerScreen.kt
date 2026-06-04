package uz.tikoncha_parent.presentation.base.multi_phone_input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.mp_cd_back
import tikoncha_parents.composeapp.generated.resources.mp_cd_clear
import tikoncha_parents.composeapp.generated.resources.mp_cd_selected
import tikoncha_parents.composeapp.generated.resources.mp_search_hint
import tikoncha_parents.composeapp.generated.resources.mp_select_country_title
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography

/**
 * Full-screen davlat tanlash ekrani (matnlar uz/ru/en).
 */
@Composable
fun CountryPickerScreen(
    onSelect: (Country) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    countries: List<Country> = Countries,
    selected: Country? = null,
) {
    var query by remember { mutableStateOf("") }

    val named = ArrayList<Pair<Country, String>>(countries.size)
    for (c in countries) {
        named.add(c to stringResource(c.nameRes))
    }
    val filtered = if (query.isBlank()) named
    else named.filter { (c, name) ->
        name.contains(query, ignoreCase = true) ||
                c.dial.contains(query) ||
                c.iso.contains(query, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.bg.surface)
            .statusBarsPadding(),
    ) {

        // ---------- Header ----------
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.mp_cd_back),
                    tint = AppColors.icon.primary,
                    modifier = Modifier.size(24.dp),
                )
            }
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(Res.string.mp_select_country_title),
                style = AppTypography.titleLgSemiBold,
                color = AppColors.text.primary,
            )
        }

        // ---------- Search ----------
        SearchField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        // ---------- List ----------
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        ) {
            items(filtered, key = { it.first.iso + it.first.dial }) { (country, name) ->
                CountryRow(
                    country = country,
                    name = name,
                    isSelected = country.iso == selected?.iso,
                    onClick = { onSelect(country) },
                )
            }
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.field.section)
            .border(1.dp, AppColors.border.secondary, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = AppColors.icon.secondary,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(10.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = AppTypography.titleMdRegular.copy(color = AppColors.text.primary),
            cursorBrush = SolidColor(AppColors.border.accentEmphasis),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
            ),
            modifier = Modifier.weight(1f),
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.mp_search_hint),
                        style = AppTypography.titleMdRegular,
                        color = AppColors.text.placeholder,
                    )
                }
                inner()
            },
        )
        if (value.isNotEmpty()) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(Res.string.mp_cd_clear),
                tint = AppColors.icon.secondary,
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .clickable { onValueChange("") },
            )
        }
    }
}

@Composable
private fun CountryRow(
    country: Country,
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val rowModifier = if (isSelected) {
        Modifier
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.bg.secondaryContainer)
            .border(1.dp, AppColors.border.accentEmphasis, RoundedCornerShape(12.dp))
    } else {
        Modifier
            .padding(horizontal = 2.dp)
            .clip(RoundedCornerShape(12.dp))
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .then(rowModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
    ) {
        Text(text = country.flag, style = AppTypography.headlineSmRegular)
        Spacer(Modifier.width(14.dp))
        Text(
            text = name,
            style = AppTypography.titleMdMedium,
            color = AppColors.text.primary,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = country.dial,
            style = AppTypography.titleMdRegular,
            color = AppColors.text.tertiary,
        )
        if (isSelected) {
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = stringResource(Res.string.mp_cd_selected),
                tint = AppColors.icon.accentPrimary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}