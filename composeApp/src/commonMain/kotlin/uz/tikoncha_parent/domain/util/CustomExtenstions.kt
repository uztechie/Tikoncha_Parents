package uz.tikoncha_parent.domain.util

fun String.capitalizeFirst(): String {
    return this.replaceFirstChar { char ->
        if (char.isLowerCase()) char.titlecase() else char.toString()
    }
}