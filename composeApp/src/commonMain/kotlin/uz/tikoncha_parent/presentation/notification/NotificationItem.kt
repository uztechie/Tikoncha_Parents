@file:OptIn(ExperimentalTime::class)

package uz.tikoncha_parent.presentation.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.data.mapper.NewsUi
import uz.tikoncha_parent.ui.DividerHorizontal
import uz.tikoncha_parent.ui.GrayColor
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryLightColor
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.extendedColor
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.*

@Composable
fun NotificationItem(
    item: NewsUi,
    modifier: Modifier = Modifier,
    onClick: ()-> Unit = {}
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                CustomText(
                    text = item.title,
                    fontSize = NormalTextSize,
                    fontWeight = FontWeight.W600,
                    color = if (!item.isRead) MaterialTheme.extendedColor.textColor else GrayColor
                )

                SpaceSmall()

                CustomText(
                    text = item.message,
                    color = MaterialTheme.extendedColor.hintColor,
                    fontSize = SmallTextSize,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp)
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {

                if (!item.isRead){
                    SpaceMedium()
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(PrimaryLightColor)
                    )
                }

                SpaceLarge()

                CustomText(
                    text = formatTimeOrDayNumber(item.createdAt),
                    fontSize = SmallTextSize
                )
            }
        }
        SpaceSmall()
        DividerHorizontal()
    }
}


fun formatTimeOrDayNumber(
    epoch: Long,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    // Istalgan tilga moslab uzgartirishingiz mumkin (quyida uz/ru/en variantlari bor)
    monthNames: List<String> = monthNamesUz
): String {
    // sekund bo'lsa, millisekundga o'tkazamiz
    val epochMillis = if (epoch < 1_000_000_000_000L) epoch * 1000 else epoch

    val nowDate = Clock.System.now().toLocalDateTime(timeZone).date
    val targetLdt = Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(timeZone)
    val targetDate = targetLdt.date

    return if (targetDate == nowDate) {
        // H:mm
        val h = targetLdt.hour
        val m = targetLdt.minute.toString().padStart(2, '0')
        "$h:$m"
    } else {
        // d MMM
        val d = targetDate.dayOfMonth
        val month = monthNames[targetDate.monthNumber - 1]
        "$d $month"
    }
}

val monthNamesUz = listOf("Yan", "Fev", "Mar", "Apr", "May", "Iyun", "Iyul", "Avg", "Sen", "Okt", "Noy", "Dek")
val monthNamesRu = listOf("янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек")



@Composable
@Preview
private fun Preview() {
    NotificationItem(
        item = NewsUi(
            id = 1,
            authorId = "1",
            title = "Title",
            message = "Message",
            published = true,
            createdAt = Clock.System.now().toEpochMilliseconds(),
            modifiedAt = Clock.System.now().toEpochMilliseconds(),
            isRead = false
        ),
        onClick = {}
    )
}