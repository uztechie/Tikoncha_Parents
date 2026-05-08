package uz.tikoncha_parent.presentation.tracking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.tikoncha_parent.presentation.map.LatLng
import kotlin.time.Clock

// ============================================================
// MOCK DATA
// ============================================================

private val mockSelf = Person(
    id = "self",
    name = "Olim",
    location = LatLng(41.3111, 69.2797),
    isSelf = true,
    lastSeenEpochMs = Clock.System.now().toEpochMilliseconds()
)

private val mockChild1 = Person(
    id = "child_1",
    name = "Saidburkhon",
    location = LatLng(41.3275, 69.2817),
    isSelf = false,
    lastSeenEpochMs = Clock.System.now().toEpochMilliseconds() - 60_000
)

private val mockChild2 = Person(
    id = "child_2",
    name = "Madina",
    location = LatLng(41.3050, 69.2700),
    isSelf = false,
    lastSeenEpochMs = Clock.System.now().toEpochMilliseconds() - 5 * 60_000
)

private val mockChild3 = Person(
    id = "child_3",
    name = "Aziz",
    location = null, // offline
    isSelf = false,
    lastSeenEpochMs = Clock.System.now().toEpochMilliseconds() - 3 * 60 * 60_000
)

private val mockPeople = listOf(mockSelf, mockChild1, mockChild2, mockChild3)

// ============================================================
// FULL SCREEN PREVIEW (Map placeholder bilan)
// ============================================================

@Preview(showBackground = true, widthDp = 412, heightDp = 915, name = "Default")
@Composable
private fun TrackingScreenPreview_Default() {
    MaterialTheme {
        TrackingContentPreview(
            state = TrackingState(
                people = mockPeople,
                selectedPersonId = "child_1",
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915, name = "Loading")
@Composable
private fun TrackingScreenPreview_Loading() {
    MaterialTheme {
        TrackingContentPreview(
            state = TrackingState(
                people = mockPeople.take(2),
                isLoading = true,
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915, name = "Empty")
@Composable
private fun TrackingScreenPreview_Empty() {
    MaterialTheme {
        TrackingContentPreview(
            state = TrackingState(
                people = emptyList(),
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915, name = "All people, no selection")
@Composable
private fun TrackingScreenPreview_NoSelection() {
    MaterialTheme {
        TrackingContentPreview(
            state = TrackingState(
                people = mockPeople,
            )
        )
    }
}

// ============================================================
// PREVIEW VERSION OF TrackingContent — Map placeholder bilan
// Asl TrackingContent'ga juda mos ko'rinish
// ============================================================

@Composable
private fun TrackingContentPreview(state: TrackingState) {
    Box(Modifier.fillMaxSize()) {

        // Map o'rniga placeholder (Yandex Maps Android Studio preview'da render qilolmaydi)
        MapPlaceholder(state)

        // Top-right: Fit-all tugmasi
        FloatingActionButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Icon(Icons.Default.ZoomOutMap, contentDescription = "Hammasini ko'rish")
        }

        // Bottom: Bolalar ro'yxati
        PeopleStripPreview(
            people = state.people,
            selectedId = state.selectedPersonId,
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

// ============================================================
// MAP PLACEHOLDER — preview uchun
// ============================================================

@Composable
private fun MapPlaceholder(state: TrackingState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5F3FF)), // ochiq ko'k — xarita ko'rinishi
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "🗺",
                fontSize = 64.sp
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = "Yandex Map (preview rejimida ko'rinmaydi)",
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.size(4.dp))
            Text(
                text = "${state.people.count { it.location != null }} ta marker mavjud",
                fontSize = 11.sp,
                color = Color(0xFF9CA3AF)
            )
        }

        // Mock markerlar — taxminiy joylashuvda
        if (state.people.isNotEmpty()) {
            MockMarkers(state)
        }
    }
}

@Composable
private fun BoxScope.MockMarkers(state: TrackingState) {
    // Mock markerlarni ekran bo'ylab tasodifiy joylashtirish
    val markers = state.people.filter { it.location != null }

    markers.forEachIndexed { index, person ->
        // Hardcoded pozitsiyalar (mockup)
        val (xOffset, yOffset) = when (index) {
            0 -> Pair(0.3f, 0.4f)
            1 -> Pair(0.6f, 0.3f)
            2 -> Pair(0.4f, 0.55f)
            else -> Pair(0.5f, 0.45f)
        }

        MockMarker(
            person = person,
            isSelected = person.id == state.selectedPersonId,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = (412 * xOffset).dp,
                    top = (700 * yOffset).dp
                )
        )
    }
}

@Composable
private fun MockMarker(
    person: Person,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val bg = when {
        person.isSelf -> Color(0xFF111827)
        isSelected -> Color(0xFF2563EB)
        else -> Color(0xFF22C55E)
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = bg,
        shadowElevation = 4.dp,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = person.name.firstOrNull()?.uppercase() ?: "?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(
                text = person.name,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ============================================================
// PeopleStrip preview kopiyasi (asl 'private', shuning uchun nusxa)
// ============================================================

@Composable
private fun PeopleStripPreview(
    people: List<Person>,
    selectedId: String?,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(people, key = { it.id }) { person ->
            PersonCardPreview(
                person = person,
                selected = person.id == selectedId
            )
        }
    }
}

@Composable
private fun PersonCardPreview(person: Person, selected: Boolean) {
    val containerColor = when {
        selected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        modifier = Modifier.widthIn(min = 140.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            AvatarPreview(person)
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = if (person.isSelf) "${person.name} (Siz)" else person.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (person.location != null) "Onlayn" else "Joylashuv yo'q",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AvatarPreview(person: Person) {
    val bg = if (person.isSelf) Color(0xFF111827) else Color(0xFF22C55E)
    Box(
        modifier = Modifier.size(36.dp).clip(CircleShape).background(bg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = person.name.firstOrNull()?.uppercase() ?: "?",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

// ============================================================
// COMPONENT PREVIEW'lari — alohida elementlar
// ============================================================

@Preview(showBackground = true, name = "PersonCard - Self selected")
@Composable
private fun PersonCardPreview_SelfSelected() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            PersonCardPreview(person = mockSelf, selected = true)
        }
    }
}

@Preview(showBackground = true, name = "PersonCard - Child selected")
@Composable
private fun PersonCardPreview_ChildSelected() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            PersonCardPreview(person = mockChild1, selected = true)
        }
    }
}

@Preview(showBackground = true, name = "PersonCard - Child unselected")
@Composable
private fun PersonCardPreview_ChildUnselected() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            PersonCardPreview(person = mockChild2, selected = false)
        }
    }
}

@Preview(showBackground = true, name = "PersonCard - Offline")
@Composable
private fun PersonCardPreview_Offline() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            PersonCardPreview(person = mockChild3, selected = false)
        }
    }
}

@Preview(showBackground = true, widthDp = 412, name = "PeopleStrip — full")
@Composable
private fun PeopleStripPreviewFull() {
    MaterialTheme {
        Box(Modifier.background(Color(0xFFF3F4F6)).padding(vertical = 16.dp)) {
            PeopleStripPreview(
                people = mockPeople,
                selectedId = "child_1"
            )
        }
    }
}