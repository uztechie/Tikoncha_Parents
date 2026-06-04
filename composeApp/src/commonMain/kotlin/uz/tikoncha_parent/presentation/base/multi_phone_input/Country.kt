package uz.tikoncha_parent.presentation.base.multi_phone_input

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.country_uz
import tikoncha_parents.composeapp.generated.resources.country_ru
import tikoncha_parents.composeapp.generated.resources.country_kz
import tikoncha_parents.composeapp.generated.resources.country_kg
import tikoncha_parents.composeapp.generated.resources.country_tj
import tikoncha_parents.composeapp.generated.resources.country_tm
import tikoncha_parents.composeapp.generated.resources.country_tr
import tikoncha_parents.composeapp.generated.resources.country_af
import tikoncha_parents.composeapp.generated.resources.country_ae
import tikoncha_parents.composeapp.generated.resources.country_us
import tikoncha_parents.composeapp.generated.resources.country_gb
import tikoncha_parents.composeapp.generated.resources.country_de
import tikoncha_parents.composeapp.generated.resources.country_kr

/**
 * Bitta davlat uchun ma'lumot.
 *
 * @param iso     ISO 3166-1 alpha-2 kodi ("UZ", "RU"...). Unique identifier sifatida ishlatiladi.
 * @param nameRes Ko'rinadigan nomi — string resource (uz/ru/en avtomatik tanlanadi).
 * @param dial    Xalqaro kod ("+998").
 * @param flag    Emoji bayroq.
 * @param mask    Raqam formati. '#' — bitta raqam o'rni, qolgan belgilar (probel) — ajratuvchi.
 *                Masalan "## ### ## ##" -> 90 123 45 67
 */
@Immutable
data class Country(
    val iso: String,
    val nameRes: StringResource,
    val dial: String,
    val flag: String,
    val mask: String,
) {
    /** Mask bo'yicha kiritish mumkin bo'lgan maksimal raqamlar soni. */
    val maxDigits: Int get() = mask.count { it == '#' }
}

/**
 * Qo'llab-quvvatlanadigan davlatlar.
 * Nomlar string resource orqali keladi -> til (uz/ru/en) avtomatik moslashadi.
 */
val Countries: List<Country> = listOf(
    Country("UZ", Res.string.country_uz, "+998", "🇺🇿", "## ### ## ##"),
    Country("RU", Res.string.country_ru, "+7",   "🇷🇺", "### ### ## ##"),
    Country("KZ", Res.string.country_kz, "+7",   "🇰🇿", "### ### ## ##"),
    Country("KG", Res.string.country_kg, "+996", "🇰🇬", "### ### ###"),
    Country("TJ", Res.string.country_tj, "+992", "🇹🇯", "## ### ####"),
    Country("TM", Res.string.country_tm, "+993", "🇹🇲", "## ## ## ##"),
    Country("TR", Res.string.country_tr, "+90",  "🇹🇷", "### ### ## ##"),
    Country("AF", Res.string.country_af, "+93",  "🇦🇫", "## ### ####"),
    Country("AE", Res.string.country_ae, "+971", "🇦🇪", "## ### ####"),
    Country("US", Res.string.country_us, "+1",   "🇺🇸", "### ### ####"),
    Country("GB", Res.string.country_gb, "+44",  "🇬🇧", "#### ######"),
    Country("DE", Res.string.country_de, "+49",  "🇩🇪", "### #######"),
    Country("KR", Res.string.country_kr, "+82",  "🇰🇷", "## #### ####"),
)

/** Standart (boshlang'ich) davlat. */
val DefaultCountry: Country = Countries.first()

/** Berilgan ISO kodi bo'yicha davlatni topadi, topilmasa default qaytaradi. */
fun countryOf(iso: String): Country =
    Countries.firstOrNull { it.iso.equals(iso, ignoreCase = true) } ?: DefaultCountry