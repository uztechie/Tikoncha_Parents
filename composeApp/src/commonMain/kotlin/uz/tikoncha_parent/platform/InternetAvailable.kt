package uz.tikoncha_parent.platform

import kotlinx.coroutines.flow.Flow

expect fun isInternetAvailable(): Boolean
//expect fun observeInternetConnection(): Flow<Boolean>