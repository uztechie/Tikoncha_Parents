package uz.tikoncha_parent.domain.use_case

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.mapper.toAppUsageList
import uz.tikoncha_parent.domain.model.AppUsage
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ChildRepository
import uz.tikoncha_parent.platform.Logger
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


class AppUsagesUseCase(
    private val repository: ChildRepository
) {
    @OptIn(ExperimentalTime::class)
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val tenDaysBefore = today.minus(10, DateTimeUnit.DAY)
    val formatter = LocalDate.Format {
        year()      // YYYY
        char('-')
        monthNumber() // MM
        char('-')
        day()  // DD
    }


    suspend operator fun invoke(userId: String): Resource<List<AppUsage>> {
        val usageRes = appUsages(userId)
       return usageRes
    }





    private suspend fun appUsages(userId: String): Resource<List<AppUsage>> {
        return try {
            val params = hashMapOf<String, Any>()
            params["user_id"] = userId
            params["date_from"] = tenDaysBefore.format(formatter)
            params["date_to"] = today.format(formatter)

            val response = repository.appUsages(params)
            if (response.success && response.data != null){
                Resource.Success(response.data.toAppUsageList())
            }
            else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        } catch (e: IOException) {
            Resource.Error(
                resId = Res.string.iltimos_internetga_ulang,
                cause = e
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.kutilmagan_xatolik_qayta_urining,
                cause = e
            )
        }
    }
}