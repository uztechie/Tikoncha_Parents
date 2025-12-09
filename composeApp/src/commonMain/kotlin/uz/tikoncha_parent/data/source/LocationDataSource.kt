package uz.tikoncha_parent.data.source

import uz.tikoncha_parent.domain.model.LocationData

interface LocationDataSource {
    suspend fun getLastLocation(): LocationData?
    fun observe(): kotlinx.coroutines.flow.Flow<LocationData?>
}