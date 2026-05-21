package uz.tikoncha_parent.domain.use_case

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.mapper.toAppUsageList
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ChildRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TodayUsageUseCase(
    private val repository: ChildRepository
) {

    private val dateFormatter = LocalDate.Format {
        year(); char('-'); monthNumber(); char('-'); day()
    }

    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(userId: String): Resource<HourMinute> {
        return try {
            val today = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
            val params = hashMapOf<String, Any>(
                "user_id"   to userId,
                "date_from" to today.format(dateFormatter),
                "date_to"   to today.format(dateFormatter),
            )

            val response = repository.appUsages(params)
            if (response.success && response.data != null) {
                val apps = response.data.toAppUsageList()
                val totalMs = apps.sumOf { app -> app.usage[today]?.values?.sum() ?: 0L }
                Resource.Success(HourMinute.fromMillis(totalMs))
            } else {
                Resource.Error(message = response.error, resId = Res.string.server_connection_error)
            }
        } catch (e: IOException) {
            Resource.Error(resId = Res.string.iltimos_internetga_ulang, cause = e)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(resId = Res.string.kutilmagan_xatolik_qayta_urining, cause = e)
        }
    }
}