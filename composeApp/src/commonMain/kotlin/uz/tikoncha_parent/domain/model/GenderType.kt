package uz.tikoncha_parent.domain.model

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.ayol
import tikoncha_parents.composeapp.generated.resources.erkak

@Serializable
enum class GenderType(
    val key: String
): JavaSerializable {
    MALE(key = "male"),
    FEMALE(key = "female");

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