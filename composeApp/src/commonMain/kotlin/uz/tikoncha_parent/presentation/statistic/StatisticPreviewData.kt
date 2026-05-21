package uz.tikoncha_parent.presentation.statistic

import kotlinx.datetime.LocalDate
import uz.tikoncha_parent.domain.model.GenderType
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.ui_state.ResponseState

// ============ PAGES ============

internal fun previewStateDaily(): StatisticState {
    val pages = listOf(
        PagePeriod(
            title = PageTitle.Day(DayLabel.NONE, 15, 4),
            subtitle = HourMinute(1, 32),
            chartSubtitle = null,
            startDate = LocalDate(2026, 5, 15),
            endDateInclusive = LocalDate(2026, 5, 15),
            totalMillis = 5_520_000L,
        ),
        PagePeriod(
            title = PageTitle.Day(DayLabel.NONE, 17, 4),
            subtitle = HourMinute(3, 5),
            chartSubtitle = null,
            startDate = LocalDate(2026, 5, 17),
            endDateInclusive = LocalDate(2026, 5, 17),
            totalMillis = 11_100_000L,
        ),
        PagePeriod(
            title = PageTitle.Day(DayLabel.YESTERDAY, 18, 4),
            subtitle = HourMinute(2, 18),
            chartSubtitle = null,
            startDate = LocalDate(2026, 5, 18),
            endDateInclusive = LocalDate(2026, 5, 18),
            totalMillis = 8_280_000L,
        ),
        PagePeriod(
            title = PageTitle.Day(DayLabel.TODAY, 19, 4),
            subtitle = HourMinute(2, 9),
            chartSubtitle = null,
            startDate = LocalDate(2026, 5, 19),
            endDateInclusive = LocalDate(2026, 5, 19),
            totalMillis = 7_740_000L,
        ),
    )
    return StatisticState(
        today = LocalDate(2026, 5, 19),
        dateSelectionType = DateSelectionType.DAY,
        pages = pages,
        selectedPageIndex = pages.lastIndex,
        bars = previewDailyBars(),
        topApps = previewTopApps(),
        appUsageResponseState = ResponseState.Success(),
        childrenResponseState = ResponseState.Success(),
        selectedChild = previewChild(),
        childrenList = listOf(previewChild()),
    )
}

internal fun previewStateWeekly(): StatisticState {
    val pages = listOf(
        PagePeriod(
            title = PageTitle.Week(4, 4, 10, 4),
            subtitle = HourMinute(11, 30),
            chartSubtitle = ChartSubtitle.WeeklyAverage(HourMinute(1, 38)),
            startDate = LocalDate(2026, 5, 4),
            endDateInclusive = LocalDate(2026, 5, 10),
            totalMillis = 41_400_000L,
        ),
        PagePeriod(
            title = PageTitle.Week(11, 4, 17, 4),
            subtitle = HourMinute(14, 22),
            chartSubtitle = ChartSubtitle.WeeklyAverage(HourMinute(2, 3)),
            startDate = LocalDate(2026, 5, 11),
            endDateInclusive = LocalDate(2026, 5, 17),
            totalMillis = 51_720_000L,
        ),
    )
    return StatisticState(
        today = LocalDate(2026, 5, 19),
        dateSelectionType = DateSelectionType.WEEK,
        pages = pages,
        selectedPageIndex = pages.lastIndex,
        bars = previewWeeklyBars(),
        topApps = previewTopApps(),
        appUsageResponseState = ResponseState.Success(),
        childrenResponseState = ResponseState.Success(),
        selectedChild = previewChild(),
        childrenList = listOf(previewChild()),
    )
}

internal fun previewStateEmpty(): StatisticState = StatisticState(
    today = LocalDate(2026, 5, 19),
    dateSelectionType = DateSelectionType.DAY,
    pages = listOf(
        PagePeriod(
            title = PageTitle.Day(DayLabel.TODAY, 19, 4),
            subtitle = HourMinute(0, 0),
            chartSubtitle = null,
            startDate = LocalDate(2026, 5, 19),
            endDateInclusive = LocalDate(2026, 5, 19),
            totalMillis = 0L,
        ),
    ),
    selectedPageIndex = 0,
    bars = emptyBars(DateSelectionType.DAY),
    topApps = emptyList(),
    appUsageResponseState = ResponseState.Success(),
    childrenResponseState = ResponseState.Success(),
    selectedChild = previewChild(),
    childrenList = listOf(previewChild()),
)

// ============ BARS ============

internal fun previewDailyBars(): List<ChartBarUi> = listOf(
    ChartBarUi(0,  0.0,  0L),
    ChartBarUi(1,  0.0,  0L),
    ChartBarUi(2,  0.0,  0L),
    ChartBarUi(3,  15.0, 15 * 60_000L),
    ChartBarUi(4,  62.0, 62 * 60_000L),
    ChartBarUi(5,  90.0, 90 * 60_000L),
    ChartBarUi(6,  45.0, 45 * 60_000L),
    ChartBarUi(7,  20.0, 20 * 60_000L),
    ChartBarUi(8,  75.0, 75 * 60_000L),
    ChartBarUi(9, 120.0, 120 * 60_000L),
    ChartBarUi(10, 80.0,  80 * 60_000L),
    ChartBarUi(11,  0.0,   0L),
)

internal fun previewWeeklyBars(): List<ChartBarUi> = listOf(
    ChartBarUi(0, 120.0, 120 * 60_000L),
    ChartBarUi(1,  85.0,  85 * 60_000L),
    ChartBarUi(2,  60.0,  60 * 60_000L),
    ChartBarUi(3,   0.0,   0L),
    ChartBarUi(4, 145.0, 145 * 60_000L),
    ChartBarUi(5, 200.0, 200 * 60_000L),
    ChartBarUi(6, 110.0, 110 * 60_000L),
)

// ============ APPS ============

internal fun previewTopApps(): List<TopAppUi> = listOf(
    TopAppUi("com.android.chrome",      "Chrome",      null, 9_800_000L, HourMinute(2, 43)),
    TopAppUi("com.instagram.android",   "Instagram",   null, 6_300_000L, HourMinute(1, 45)),
    TopAppUi("com.whatsapp",            "WhatsApp",    null, 3_600_000L, HourMinute(1, 0)),
    TopAppUi("com.google.youtube",      "YouTube",     null, 2_100_000L, HourMinute(0, 35)),
    TopAppUi("com.telegram.android",    "Telegram",    null, 1_200_000L, HourMinute(0, 20)),
)

internal fun previewDetailItems(): List<UsageDetailItem> = listOf(
    UsageDetailItem("com.android.chrome", "Chrome", null, 5_400_000L, HourMinute(1, 30)),
    UsageDetailItem("com.instagram.android", "Instagram", null, 2_400_000L, HourMinute(0, 40)),
    UsageDetailItem("com.whatsapp",          "WhatsApp",  null, 1_800_000L, HourMinute(0, 30)),
    UsageDetailItem("com.telegram.android",  "Telegram",  null, 1_200_000L, HourMinute(0, 20)),
    UsageDetailItem("com.google.youtube",    "YouTube",   null,   840_000L, HourMinute(0, 14)),
)

// ============ CHILD ============

internal fun previewChild(): UserInfo = UserInfo(
    userId = "preview-child-id",
    phoneNumber = "+998 90 123 45 67",
    fullName = "Doniyor Aliyev Akmalovich",
    name = "Doniyor",
    lastName = "Aliyev",
    patronymic = "Akmalovich",
    genderType = GenderType.MALE,
    passportId = null,
    age = 12,
    schoolId = null,
    schoolName = "32-maktab",
    schoolClassName = "6-A",
    schoolClassId = null,
    shift = "1",
    avatarUrl = null,
    last_seen = null,
    subscription = null,
    subscription_end_date = null,
)