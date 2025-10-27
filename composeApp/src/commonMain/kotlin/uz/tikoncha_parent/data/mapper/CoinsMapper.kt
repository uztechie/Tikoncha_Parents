package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.data.remote.model.CoinsData
import uz.tikoncha_parent.domain.model.MyCoins

fun CoinsData.toDomain() = MyCoins(coins)