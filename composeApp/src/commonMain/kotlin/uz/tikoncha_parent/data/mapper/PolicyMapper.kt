package uz.tikoncha_parent.data.mapper

import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.data.remote.model.AppDto
import uz.tikoncha_parent.data.remote.model.LimitRuleDto
import uz.tikoncha_parent.data.remote.model.LocationRuleDto
import uz.tikoncha_parent.data.remote.model.PolicyDto
import uz.tikoncha_parent.data.remote.model.TimeRuleDto
import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.GeoType
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.domain.model.LimitRule
import uz.tikoncha_parent.domain.model.LimitWindow
import uz.tikoncha_parent.domain.model.LocationData
import uz.tikoncha_parent.domain.model.LocationRule
import uz.tikoncha_parent.domain.model.MinuteRange
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.TimeRule
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.domain.model.policy.PolicyAction
import uz.tikoncha_parent.presentation.policy.policy_list.PolicyItemUi
import uz.tikoncha_parent.presentation.policy.app_site_selection.AppSelectionUi
import uz.tikoncha_parent.presentation.policy.app_site_selection.CategoryLocalizer
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.limit_rule.toMinutes
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi


fun AppDto.toAppSelectionUi(): AppSelectionUi {
    return AppSelectionUi(
        name = name?:"",
        packageName = `package`,
        iconUrl = icon,
        category = CategoryLocalizer.toCode(category?:""),
        order = order
    )
}

fun List<TimeRuleUi>.toTimeRuleDtoList(): List<TimeRuleDto> {
    val list = mutableListOf<TimeRuleDto>()
    this.forEach {

        val startTime = if (it.allDay) 0 else it.startTime.toMinutes()
        val endTime = if (it.allDay) 24*60 else it.endTime.toMinutes()

        list.add(
            TimeRuleDto(
                start_time = startTime,
                end_time = endTime,
                days = it.weekDays.map { it.num },
                time_include = !it.outside
            )
        )
    }
    return list
}

fun List<LimitRuleUi>.toLimitRuleDtoList(): List<LimitRuleDto>{
    val list = mutableListOf<LimitRuleDto>()
    this.forEach {
        list.add(
            LimitRuleDto(
               limit_amount = it.time.toMinutes(),
               limit_type = it.limitType.name,
                days = it.weekDays.map { it.num }
            )
        )
    }
    return list
}

fun List<LocationData>?.toPolygonDto(): List<List<Double>>{
    if (this.isNullOrEmpty()) return emptyList()
    return this.map { (lat, lng) ->
        listOf(lat, lng)
    }
}


fun LocationRule?.toLocationRuleDto(): LocationRuleDto?{
    if (this == null) return null
    return LocationRuleDto(
        polygon = polygon.toPolygonDto(),
        circle_radius = radiusMeters?.toDouble(),
        center_latitude = centerLat,
        center_longitude = centerLng,
        location_include = !reverse,
        type = geoType.name
    )
}


fun LimitRuleDto.toLimitRuleUi(): LimitRuleUi {
    return LimitRuleUi(
        time = limit_amount.toHourMinute(),
        weekDays = days.map { WeekDay.fromNum(it) }.toSet(),
        limitType = DayHour.getDayHourByKey(limit_type)
    )
}

fun TimeRuleDto.toTimeRuleUi(): TimeRuleUi {
    val startTime = start_time.toLocalTime()
    val endTime = end_time.toLocalTime()

    return TimeRuleUi(
        startTime = startTime,
        endTime = endTime,
        outside = !time_include,
        allDay = start_time == 0 && end_time == 1440,
        time = "${startTime.hm()} - ${endTime.hm()}",
        weekDays = days.map { WeekDay.fromNum(it) }.toSet(),
        timeRange = buildTimeRanges(
            startTime = startTime,
            endTime = endTime,
            outside = !time_include
        )

    )
}

fun PolicyDto.toPolicyListUi(): PolicyItemUi{

    val location = buildLocation(location_rule)
    val policyType = PolicyType.getPolicyType(scope_type)
    return PolicyItemUi(
        ruleId = rule_id,
        policyName = rule_name ?: policy_name,
        appCount = packages?.size?:0,
        webCount = sites?.size?:0,
        hasTimeRule = !time_rule.isNullOrEmpty(),
        hasLimitRule = !limit_rule.isNullOrEmpty(),
        hasLocationRule = location_rule != null,
        isActive = true,
        packages = packages?:emptyList(),
        categories = categories?:emptyList(),
        action = PolicyAction.valueToPolicyAction(action),
        sites = sites?:emptyList(),
        timeRule = time_rule?.map { it.toTimeRuleUi() }?:emptyList(),
        limitRule = limit_rule?.map { it.toLimitRuleUi() }?:emptyList(),
        policyType = policyType,
        isMine =  policyType == PolicyType.PARENT_CHILD,
        locationRule = location
    )
}

private fun buildLocation(dto: LocationRuleDto?): LocationRule? {
    if (dto == null) return null
    val geo = when (dto.type?.uppercase()) {
        "CIRCLE" -> GeoType.CIRCLE
        "POLYGON" -> GeoType.POLYGON
        else -> return null
    }
    return when (geo) {
        GeoType.CIRCLE -> LocationRule(
            geoType = GeoType.CIRCLE,
            centerLat = dto.center_latitude,
            centerLng = dto.center_longitude,
            radiusMeters = dto.circle_radius?.toInt(),
            polygon = null,
            reverse = !dto.location_include
        )
        GeoType.POLYGON -> LocationRule(
            geoType = GeoType.POLYGON,
            centerLat = null,
            centerLng = null,
            radiusMeters = null,
            polygon = dto.polygon?.mapNotNull { p ->
                if (p.size >= 2) LocationData(p[1], p[0]) else null
            },
            reverse = !dto.location_include
        )
    }
}

fun buildTimeRanges(
    startTime: LocalTime,
    endTime: LocalTime,
    outside: Boolean
): List<MinuteRange> {
    val s = startTime.toMinutes().coerceIn(0, 1440)
    val e = endTime.toMinutes().coerceIn(0, 1440)

    return if (!outside) {
        when {
            s == e -> emptyList()
            s < e -> listOf(MinuteRange(s, e))
            else -> {
                // Agar foydalanuvchi "ichki" oraliqni kesishib kechaga o'tadigan qilsa (mas: 22:00-03:00),
                // uni ikkiga bo'lib qaytarish ham mumkin; lekin odatda ichki oraliqni s<e qilib cheklab qo'yish tavsiya.
                listOf(MinuteRange(s, 1440), MinuteRange(0, e))
            }

        }
    } else {
        when {
            s == e -> listOf(MinuteRange(0, 1440))
            s < e -> {
                val left = if (s > 0) MinuteRange(0, s) else null
                val right = if (e < 1440) MinuteRange(e, 1440) else null
                listOfNotNull(left, right)
            }

            else -> {
                // s > e bo'lsa (mas: 22:00-03:00) ichki oraliq kechani kesib o'tgan bo'ladi,
                // demak tashqarisi faqat (e, s) oralig'i. Uni bitta bo'lak qilib qaytaramiz.
                listOf(MinuteRange(e, s))
            }
        }
    }

}


private fun Int?.toLocalTime(): LocalTime {
    if (this == null) return LocalTime(0, 0)
    return if (this >= 24 * 60) LocalTime(23, 59)
    else LocalTime(this / 60, this % 60)
}

private fun LocalTime.hm(): String {
    val h = hour.toString().padStart(2, '0')
    val m = minute.toString().padStart(2, '0')
    return "$h:$m"
}

private fun Int.toHourMinute(): HourMinute {
    return if (this >= 24 * 60) HourMinute(23, 59)
    else HourMinute(this / 60, this % 60)
}

fun List<TimeRule>.toTimeRuleUiList(): List<TimeRuleUi> {
    if (isEmpty()) return emptyList()

    val grouped = groupBy { Triple(it.startMin, it.endMin, it.inversion) }

    val sortedKeys = grouped.keys.sortedWith(
        compareBy<Triple<Int, Int, Boolean>> { it.first }
            .thenBy { it.second }
            .thenBy { it.third },
    )

    var counter = 1
    return sortedKeys.map { (startMin, endMin, inversion) ->
        val items = grouped[Triple(startMin, endMin, inversion)].orEmpty()
        val weekDays = items.map { WeekDay.fromNum(it.weekDay) }.toSet()

        val startLt = startMin.toLocalTime()
        val endLt = endMin.toLocalTime()
        val allDay = !inversion && startMin == 0 && endMin == (24 * 60) - 1

        val timeLabel = when {
            allDay -> "${startLt.hm()} – ${endLt.hm()}"
            inversion -> "00:00 - ${startLt.hm()}, ${endLt.hm()} - 23:59"
            else -> "${startLt.hm()} – ${endLt.hm()}"
        }

        TimeRuleUi(
            id = counter++,
            startTime = startLt,
            endTime = endLt,
            outside = inversion,
            allDay = allDay,
            time = timeLabel,
            weekDays = weekDays,
            timeRange = buildTimeRanges(
                startTime = startLt,
                endTime = endLt,
                outside = inversion,
            ),
        )
    }
}

fun List<LimitRule>.toLimitRuleUi(): List<LimitRuleUi> {
    if (isEmpty()) return emptyList()

    var indexCounter = 1
    return groupBy { it.window }
        .flatMap { (window, rules) ->
            rules
                .groupBy { it.allowedMinutes }
                .map { (minutes, sameLimitRules) ->
                    LimitRuleUi(
                        id = indexCounter++,
                        time = HourMinute.fromMinutes(minutes),
                        weekDays = sameLimitRules.map { WeekDay.fromNum(it.weekDay) }.toSet(),
                        limitType = when (window) {
                            LimitWindow.DAILY -> DayHour.DAY
                            LimitWindow.HOURLY -> DayHour.HOUR
                        },
                    )
                }
        }
}
