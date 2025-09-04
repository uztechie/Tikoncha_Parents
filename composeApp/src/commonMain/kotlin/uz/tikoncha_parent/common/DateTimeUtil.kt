package uz.tikoncha_parent.common

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object DateTimeUtil {

    fun formatToIsoString(
        localDate: LocalDate?,
        localTime: LocalTime?,
        timeZone: TimeZone = TimeZone.UTC
    ): String {
        if (localTime == null || localDate == null){
            return ""
        }
        // Combine LocalDate + LocalTime into LocalDateTime
        val localDateTime = LocalDateTime(localDate, localTime)

        val year = localDateTime.year.toString().padStart(4, '0')
        val month = localDateTime.month.ordinal.toString().padStart(2, '0')
        val day = localDateTime.day.toString().padStart(2, '0')
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        val second = localDateTime.second.toString().padStart(2, '0')
        val millis = (localDateTime.nanosecond / 1_000_000).toString().padStart(3, '0')

        return "$year-$month-${day}T$hour:$minute:$second.${millis}"
    }

    fun formatToIsoString(
        millis: Long,
        timeZone: TimeZone = TimeZone.UTC
    ): String {
        println("formatToIsoString millis=$millis")
        val instant = Instant.fromEpochMilliseconds(millis)
        val localDateTime = instant.toLocalDateTime(timeZone)


        val year = localDateTime.year.toString().padStart(4, '0')
        val month = localDateTime.month.ordinal.toString().padStart(2, '0')
        val day = localDateTime.day.toString().padStart(2, '0')
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        val second = localDateTime.second.toString().padStart(2, '0')
        val millis = (localDateTime.nanosecond / 1_000_000).toString().padStart(3, '0')

        return "$year-$month-${day}T$hour:$minute:$second.${millis}"
    }


    fun getCurrentIsoDateTime(): String {
        val nowInstant = Clock.System.now()
        val localDateTime = nowInstant.toLocalDateTime(TimeZone.currentSystemDefault())

        val year = localDateTime.year.toString().padStart(4, '0')
        val month = localDateTime.month.ordinal.toString().padStart(2, '0')
        val day = localDateTime.day.toString().padStart(2, '0')
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        val second = localDateTime.second.toString().padStart(2, '0')
        val millis = (localDateTime.nanosecond / 1_000_000).toString().padStart(3, '0')

        return "$year-$month-${day}T$hour:$minute:$second.${millis}"
    }

    fun String.fromServerToLocalDateTime(): LocalDateTime =
        runCatching { Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault()) }
            .getOrElse { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }



    fun String?.serverDateTimeToMillis(defaultTimeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
        if (this == null){
            return 0
        }
        val raw = this.trim()
        if (raw.isEmpty()) return 0L

        return try {
            // Offset bormi? (Z yoki +HH:MM / +HHMM)
            val hasZone = raw.endsWith("Z") ||
                    Regex("[+-]\\d{2}:?\\d{2}$").containsMatchIn(raw)

            // Fraksiyani 9 digit (nanosecond) ga normallashtiramiz, offsetni HHMM -> HH:MM ga
            val normalized = raw
                // ".123" yoki ".123000" -> ".123000000"
                .replace(Regex("""\.(\d{1,9})""")) { m ->
                    val frac = m.groupValues[1]
                    "." + (frac + "000000000").take(9)
                }
                // "+0500" -> "+05:00"
                .replace(Regex("""([+-]\d{2})(\d{2})$"""), "$1:$2")

            if (hasZone) {
                // Z yoki offset bor: bevosita Instant
                Instant.parse(normalized).toEpochMilliseconds()
            } else {
                // Zona yo‘q: LocalDateTime sifatida parse qilib, defaultTimeZone bilan Instantga o‘tkazamiz
                val re = Regex(
                    """^(\d{4})-(\d{2})-(\d{2})T""" +         // yyyy-MM-ddT
                            """(\d{2}):(\d{2})""" +                   // HH:mm
                            """(?::(\d{2})(?:\.(\d{1,9}))?)?$"""      // [:ss[.fffffffff]]
                )
                val m = re.matchEntire(normalized) ?: return 0L

                val year   = m.groupValues[1].toInt()
                val month  = m.groupValues[2].toInt()
                val day    = m.groupValues[3].toInt()
                val hour   = m.groupValues[4].toInt()
                val minute = m.groupValues[5].toInt()
                val second = m.groupValues.getOrNull(6)?.takeIf { it.isNotEmpty() }?.toInt() ?: 0
                val nano   = m.groupValues.getOrNull(7)?.takeIf { it.isNotEmpty() }?.let {
                    (it + "000000000").take(9).toInt()
                } ?: 0

                val ldt = LocalDateTime(year, month, day, hour, minute, second, nano)
                ldt.toInstant(defaultTimeZone).toEpochMilliseconds()
            }
        } catch (_: Throwable) {
            0L
        }
    }
}