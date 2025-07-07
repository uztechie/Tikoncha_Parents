package org.example.project.presentation.home

import androidx.compose.runtime.mutableStateListOf
import org.example.project.presentation.domain.model.UsagePeriod

data class HomeState(
    val socialAppUsageList: List<AppUsage> = listOf(
        AppUsage("", "Instagram", "", "1 soat"),
        AppUsage("", "You tube", "", "2 soat"),
        AppUsage("", "Tik Tok", "", "3 soat"),
        AppUsage("", "Pubg Mobile", "", "4 soat"),
        AppUsage("", "Mobile Legends Bing Bang", "", "5 soat"),
        AppUsage("", "Facebook", "", "6 soat"),
        AppUsage("", "Twitter", "", "7 soat")
    ),
    val gameUsageList: List<AppUsage> = listOf(
        AppUsage("", "Telegram", "", "10 soat"),
        AppUsage("", "Chrome", "", "11 soat"),
        AppUsage("", "Settings", "", "12 soat"),
    ),
    val otherAppUsageList: List<AppUsage> = listOf(
        AppUsage("", "Linkedin", "", "8 soat"),
        AppUsage("", "Duolingo", "", "9 soat")
    ),
    val dailyPeriods: List<UsagePeriod> = emptyList(),
    val weeklyPeriods: List<UsagePeriod> = emptyList(),

    val dailyChartData: Map<Int, Double> = mapOf(
        1 to 2.0,
        2 to 1.0,
        3 to 0.0,
        4 to 45.0,
        5 to 12.0,
        6 to 9.0,
        7 to 45.0,
        8 to 31.0,
        9 to 2.0,
        10 to 1.0,
        11 to 0.0,
        12 to 45.0,
        13 to 12.0,
        14 to 9.0,
        15 to 45.0,
        16 to 31.0,
        17 to 2.0,
        18 to 1.0,
        19 to 0.0,
        20 to 45.0,
        21 to 12.0,
        22 to 9.0,
        23 to 45.0,
        24 to 31.0,
    ),
    val weeklyChartData: Map<Int, Double> = mapOf(
        1 to 2.0,
        2 to 1.0,
        3 to 0.0,
        4 to 45.0,
        5 to 12.0,
        6 to 9.0,
        7 to 45.0
    ),

    val childrenList: List<String> = listOf("Saidburxon", "Muhammadsaid", "Muhammadyusuf", "Beka"),
    val selectedChildren: String = childrenList[0]
)
