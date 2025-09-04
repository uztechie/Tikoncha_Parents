package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.data.remote.model.UserInfoDto
import uz.tikoncha_parent.domain.model.GenderType
import uz.tikoncha_parent.domain.model.UserInfo

fun UserInfoDto.toUserInfo(): UserInfo{
    return UserInfo(
        userId = user_id?:"",
        phoneNumber = phone_number?:"",
        name = first_name?:"",
        lastName = last_name?:"",
        patronymic = patronymic?:"",
        genderType = GenderType.getGenderByKey(gender)?: GenderType.MALE,
        passportId = passport_id?:"",
        age = age,
        schoolId = school_id,
        schoolName = school_name,
        schoolClassName = school_class_name,
        schoolClassId = school_class_id,
        shift = shift,
        fullName = "$last_name $first_name $patronymic"
    )
}