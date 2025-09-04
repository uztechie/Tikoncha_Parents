package uz.tikoncha_parent.domain.use_case

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
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
            else{
                Resource.Error(response.error?: "")
            }
        }

        catch (e: Exception){
            e.printStackTrace()
            Logger.e("XATOOOO", "message: ",e)
            Resource.Error("Xatolik")
        }
    }
}