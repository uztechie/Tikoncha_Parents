package org.example.project.domain.use_case

import androidx.navigation.NavUri
import kotlinx.coroutines.async
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.example.project.data.mapper.toAppUsageList
import org.example.project.data.remote.model.GetRulesResponse
import org.example.project.data.remote.model.UserInfoDto
import org.example.project.domain.model.AppUsage
import org.example.project.domain.model.Resource
import org.example.project.domain.repository.ChildRepository
import org.example.project.domain.repository.LoginRepository
import org.example.project.domain.repository.RulesRepository
import org.example.project.platform.Logger
import uz.saidburxon.newedu.data.model.SendOtpRequest
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