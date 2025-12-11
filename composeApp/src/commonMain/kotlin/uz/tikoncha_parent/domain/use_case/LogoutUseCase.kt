package uz.tikoncha_parent.domain.use_case

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.mapper.toLogoutUi
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.LogoutRepository
import uz.tikoncha_parent.presentation.new_home.logout.LogoutUi

class LogoutUseCase(
    private val repository: LogoutRepository
) {
    suspend operator fun invoke(): Resource<List<LogoutUi>?> {
        return try {
            val response = repository.getLogout()

            if (!response.success){
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            } else {
                val data = response.data?.items?.map { it.toLogoutUi() }
                Resource.Success(data)
            }
        } catch (e: Exception) {
            Resource.Error(
                message = e.message ?: "Unknown error",
                resId = Res.string.server_connection_error
            )
        }
    }
}