package org.example.project.domain.model

import org.jetbrains.compose.resources.StringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.ayol
import tikoncha_parents.composeapp.generated.resources.erkak
import tikoncha_parents.composeapp.generated.resources.ona
import tikoncha_parents.composeapp.generated.resources.ota

enum class GenderType(
    val resId: StringResource,
    val key: String
) {
    MALE(resId = Res.string.erkak, key = "male"),
    FEMALE(resId = Res.string.ayol, key = "female");

    companion object Companion {
        fun getGenderByIndex(index:Int): GenderType{
            if (index == 0){
                return MALE
            }else{
                return FEMALE
            }
        }

        fun getGenderByKey(key: String?): GenderType?{
            return entries.find { it.key == key }
        }
    }
}