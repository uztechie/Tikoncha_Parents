package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.domain.model.MyCoins
import uz.tikoncha_parent.domain.model.app_error.Outcome

interface MyCoinsRepository {
    suspend fun getMyCoins(): Outcome<MyCoins>
}