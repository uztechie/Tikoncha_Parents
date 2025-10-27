package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.domain.model.MyCoins

interface MyCoinsRepository {
    suspend fun getMyCoins(): MyCoins
}