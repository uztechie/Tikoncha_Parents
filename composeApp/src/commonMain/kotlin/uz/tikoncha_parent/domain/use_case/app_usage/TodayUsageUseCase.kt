package uz.tikoncha_parent.domain.use_case.app_usage

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.ChildRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TodayUsageUseCase(
    private val repository: ChildRepository
) {

    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(userId: String): Outcome<HourMinute> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        return when (val res = repository.appUsages(userId, from = today, to = today)) {
            is Outcome.Failure -> res
            is Outcome.Success -> {
                val totalMs = res.data.sumOf { app -> app.usage[today]?.values?.sum() ?: 0L }
                Outcome.Success(HourMinute.fromMillis(totalMs))
            }
        }
    }
}