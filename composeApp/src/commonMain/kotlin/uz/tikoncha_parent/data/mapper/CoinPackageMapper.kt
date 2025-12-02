package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.data.remote.model.CoinPackageDto
import uz.tikoncha_parent.domain.model.CoinPackage

fun CoinPackageDto.toDomain(): CoinPackage =
    CoinPackage(
        coins = coins,
        price = price,
        discountPercent = discount_percent
    )