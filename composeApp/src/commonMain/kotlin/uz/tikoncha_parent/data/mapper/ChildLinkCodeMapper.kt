package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.data.remote.model.AddChildData
import uz.tikoncha_parent.domain.model.ChildLinkCode

fun AddChildData.toChildLinkCode(): ChildLinkCode = ChildLinkCode(
    code = code,
    expiresAtMillis = DateTimeUtil.toMillisUtc(expires_at),
)