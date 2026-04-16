package uz.tikoncha_parent.domain.model.apps

enum class AppCategory(val id: String) {
    ART_AND_DESIGN("ART_AND_DESIGN"),
    AUTO_AND_VEHICLES("AUTO_AND_VEHICLES"),
    BEAUTY("BEAUTY"),
    BOOKS_AND_REFERENCE("BOOKS_AND_REFERENCE"),
    BUSINESS("BUSINESS"),
    COMICS("COMICS"),
    COMMUNICATION("COMMUNICATION"),
    DATING("DATING"),
    EDUCATION("EDUCATION"),
    ENTERTAINMENT("ENTERTAINMENT"),
    EVENTS("EVENTS"),
    FINANCE("FINANCE"),
    FOOD_AND_DRINK("FOOD_AND_DRINK"),
    HEALTH_AND_FITNESS("HEALTH_AND_FITNESS"),
    HOUSE_AND_HOME("HOUSE_AND_HOME"),
    LIBRARIES_AND_DEMO("LIBRARIES_AND_DEMO"),
    LIFESTYLE("LIFESTYLE"),
    MAPS_AND_NAVIGATION("MAPS_AND_NAVIGATION"),
    MEDICAL("MEDICAL"),
    MUSIC_AND_AUDIO("MUSIC_AND_AUDIO"),
    NEWS_AND_MAGAZINES("NEWS_AND_MAGAZINES"),
    PARENTING("PARENTING"),
    PERSONALIZATION("PERSONALIZATION"),
    PHOTOGRAPHY("PHOTOGRAPHY"),
    PRODUCTIVITY("PRODUCTIVITY"),
    SHOPPING("SHOPPING"),
    SOCIAL("SOCIAL"),
    SPORTS("SPORTS"),
    TOOLS("TOOLS"),
    TRAVEL_AND_LOCAL("TRAVEL_AND_LOCAL"),
    VIDEO_PLAYERS("VIDEO_PLAYERS"),
    WEATHER("WEATHER"),
    GAMES("GAMES"),
    OTHER("OTHER");

    companion object {
        /** Server dan kelgan string → enum. GAME_* → GAMES, noma'lum → OTHER */
        fun from(value: String?): AppCategory {
            if (value == null) return OTHER
            val upper = value.uppercase()
            if (upper.startsWith("GAME_")) return GAMES
            return entries.find { it.id == upper } ?: OTHER
        }
    }
}