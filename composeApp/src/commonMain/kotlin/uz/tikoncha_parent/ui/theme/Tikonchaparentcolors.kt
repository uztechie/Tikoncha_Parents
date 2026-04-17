package uz.tikoncha_parent.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class TikonchaExtendedColors(
    val bg: BgColors,
    val text: TextColors,
    val icon: IconColors,
    val border: BorderColors,
    val button: ButtonColors,
    val action: ActionColors,
    val section: SectionColors,
    val field: FieldColors,
    val modal: ModalColors,
)

@Immutable
data class BgColors(
    val page: Color,
    val section: Color,
    val surface: Color,
    val elevated: Color,
    val secondary: Color,
    val primary: Color,
    val primaryContainer: Color,
    val secondaryBrand: Color,
    val secondaryContainer: Color,
    val accentWarning: Color,
    val accentWarningContainer: Color,
    val tertiary: Color,
    val strong: Color,
    val strongSecondary: Color,
    val secondarySurface: Color,
    val surfaceTertiary: Color,
    val accentDanger: Color,
)

@Immutable
data class TextColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val accentEmphasis: Color,
    val disabled: Color,
    val onPrimary: Color,
    val inverse: Color,
    val strong: Color,
    val label: Color,
    val placeholder: Color,
    val accentWarning: Color,
    val strongSecondary: Color,
    val accentDanger: Color,
    val disabledTertiary: Color,
)

@Immutable
data class IconColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val onSecondary: Color,
    val disabled: Color,
    val accentPrimary: Color,
    val inverse: Color,
    val accentWarning: Color,
    val accentWarningContainer: Color,
    val accentDanger: Color,
    val disabledTertiary: Color,
    val strong: Color,
    val accentPrimarySurface: Color,
)

@Immutable
data class BorderColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val accentEmphasis: Color,
    val disabled: Color,
    val accentWarning: Color,
    val tertiarySubtle: Color,
    val secondarySubtle: Color,
)

@Immutable
data class ButtonColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val accentEmphasis: Color,
    val disabled: Color,
    val primaryHover: Color,
    val primaryPressed: Color,
    val secondaryPressed: Color,
    val accentEmphasisPressed: Color,
    val accentDanger: Color,
    val surface: Color,
)

@Immutable
data class ActionColors(
    val primary: Color,
    val secondary: Color,
    val section: Color,
    val primaryHover: Color,
    val primaryPressed: Color,
    val disabled: Color,
    val disabledTertiary: Color,
    val tertiary: Color,
    val strong: Color,
    val accentDanger: Color,
)

@Immutable
data class SectionColors(
    val primary: Color,
    val secondary: Color,
    val section: Color,
    val tertiary: Color,
    val disabledTertiary: Color,
)

@Immutable
data class FieldColors(
    val page: Color,
    val secondary: Color,
    val section: Color,
    val accentEmphasis: Color,
    val sectionEmphasis2: Color,
    val sectionEmphasisTertiary: Color,
    val accentEmphasisSecondary: Color,
)

@Immutable
data class ModalColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
)


val TikonchaParentLightExtendedColors = TikonchaExtendedColors(
    bg = BgColors(
        page = Color(0xFFF3EADE),                               // orange.100
        section = Color(0xFFEFEFF0),                            // neutral.200
        surface = Color(0xFFFFFFFF),                            // base.white
        elevated = Color(0xFFFFFFFF),                           // base.white
        secondary = Color(0xFFF9F5EF),                          // orange.50
        primary = Color(0xFFC3955B),                            // orange.500
        primaryContainer = Color(0xFFF3EADE),                   // orange.100
        secondaryBrand = Color(0xFF9C7749),                     // orange.600
        secondaryContainer = Color(0xFFF9F5EF),                 // orange.50
        accentWarning = Color(0xFFC3955B),                      // orange.500
        accentWarningContainer = Color(0xFFF9F5EF),             // orange.50
        tertiary = Color(0xFFEFEFF0),                           // gray.100
        strong = Color(0xFF010D01),                             // base.black
        strongSecondary = Color(0xFF252528),                    // gray.700
        secondarySurface = Color(0xFFEFEFF0),                   // gray.100
        surfaceTertiary = Color(0xFFFFFFFF),                    // base.white
        accentDanger = Color(0xFFF04438),                       // red.500
    ),
    text = TextColors(
        primary = Color(0xFF22262F),                            // neutral.800
        secondary = Color(0xFF2F3137),                          // neutral.700
        tertiary = Color(0xFF85888E),                           // neutral.500
        accentEmphasis = Color(0xFFC3955B),                     // orange.500
        disabled = Color(0xFFFFFFFF),                           // base.white
        onPrimary = Color(0xFFFFFFFF),                          // base.white
        inverse = Color(0xFFFFFFFF),                            // base.white
        strong = Color(0xFF010D01),                             // base.black
        label = Color(0xFF94979C),                              // gray.400
        placeholder = Color(0xFF94979C),                        // gray.400
        accentWarning = Color(0xFFF79009),                      // orange-variant.500
        strongSecondary = Color(0xFF85888E),                    // neutral.500
        accentDanger = Color(0xFFF04438),                       // red.500
        disabledTertiary = Color(0xFF94979C),                   // gray.400
    ),
    icon = IconColors(
        primary = Color(0xFF121212),                            // gray.900
        secondary = Color(0xFF85888E),                          // neutral.500
        tertiary = Color(0xFFFFFFFF),                           // base.white
        onSecondary = Color(0xFFF9FAFA),                        // gray.50
        disabled = Color(0xFFFFFFFF),                           // base.white
        accentPrimary = Color(0xFFC3955B),                      // orange.500
        inverse = Color(0xFFFFFFFF),                            // base.white
        accentWarning = Color(0xFFF79009),                      // orange-variant.500
        accentWarningContainer = Color(0xFFF3EADE),             // orange.100
        accentDanger = Color(0xFFF04438),                       // red.500
        disabledTertiary = Color(0xFF94979C),                   // gray.400
        strong = Color(0xFF010D01),                             // base.black
        accentPrimarySurface = Color(0xFFC3955B),               // orange.500
    ),
    border = BorderColors(
        primary = Color(0xFFCECFD2),                            // gray.300
        secondary = Color(0xFFE5E5E6),                          // gray.200
        tertiary = Color(0xFFFFFFFF),                           // base.white
        accentEmphasis = Color(0xFFC3955B),                     // orange.500
        disabled = Color(0xFFEFEFF0),                           // gray.100
        accentWarning = Color(0xFFF79009),                      // orange-variant.500
        tertiarySubtle = Color(0xFFFFFFFF),                     // base.white
        secondarySubtle = Color(0xFFE5E5E6),                    // gray.200
    ),
    button = ButtonColors(
        primary = Color(0xFFC3955B),                            // orange.500
        secondary = Color(0xFFEFEFF0),                          // neutral.200
        tertiary = Color(0xFF85888E),                           // neutral.500
        accentEmphasis = Color(0xFFF9F5EF),                     // orange.50
        disabled = Color(0xFFEFEFF0),                           // neutral.200
        primaryHover = Color(0xFF409452),                       // green.600
        primaryPressed = Color(0xFF755937),                     // orange.700
        secondaryPressed = Color(0xFFDCDDDF),                   // neutral.300
        accentEmphasisPressed = Color(0xFFF3EADE),              // orange.100
        accentDanger = Color(0xFFF04438),                       // red.500
        surface = Color(0xFFFFFFFF)
    ),
    action = ActionColors(
        primary = Color(0xFFC3955B),                            // orange.500
        secondary = Color(0xFFE5E5E6),                          // gray.200
        section = Color(0xFFEFEFF0),                            // gray.100
        primaryHover = Color(0xFF9C7749),                       // orange.600
        primaryPressed = Color(0xFF755937),                     // orange.700
        disabled = Color(0xFFE5E5E6),                           // gray.200
        disabledTertiary = Color(0xFFE5E5E6),                   // gray.200
        tertiary = Color(0xFFFFFFFF),                           // base.white
        strong = Color(0xFF252528),                             // gray.700
        accentDanger = Color(0xFFF04438),                       // red.500
    ),
    section = SectionColors(
        primary = Color(0xFFC3955B),                            // orange.500
        secondary = Color(0xFFE5E5E6),                          // gray.200
        section = Color(0xFFEFEFF0),                            // gray.100
        tertiary = Color(0xFFFFFFFF),                           // base.white
        disabledTertiary = Color(0xFFE5E5E6),                   // gray.200
    ),
    field = FieldColors(
        page = Color(0xFFEFEFF0),                               // gray.100
        secondary = Color(0xFFF2F2F3),                          // neutral.100
        section = Color(0xFFF9FAFA),                            // gray.50
        accentEmphasis = Color(0xFFC3955B),                     // orange.500
        sectionEmphasis2 = Color(0xFFF9FAFA),                   // gray.50
        sectionEmphasisTertiary = Color(0xFFF9FAFA),            // gray.50
        accentEmphasisSecondary = Color(0xFFF3EADE),            // orange.100
    ),
    modal = ModalColors(
        primary = Color(0xFFFFFFFF),                            // base.white
        secondary = Color(0xFFE5E5E6),                          // gray.200
        tertiary = Color(0xFFFFFFFF),                           // base.white
    ),
)


val TikonchaParentDarkExtendedColors = TikonchaExtendedColors(
    bg = BgColors(
        page = Color(0xFF010D01),                               // base.black
        section = Color(0xFF2B2B31),                            // gray.600
        surface = Color(0xFF252528),                            // gray.700
        elevated = Color(0xFF252528),                           // gray.700
        secondary = Color(0xFF010D01),                          // base.black
        primary = Color(0xFFC3955B),                            // green.500
        primaryContainer = Color(0xFF2C2F36),                   // gray.500
        secondaryBrand = Color(0xFF409452),                     // green.600
        secondaryContainer = Color(0xFF252528),                 // gray.700
        accentWarning = Color(0xFF9C7749),                      // orange.600
        accentWarningContainer = Color(0xFF2F3137),             // neutral.700
        tertiary = Color(0xFF2F3137),                           // neutral.700
        strong = Color(0xFF010D01),                             // base.black
        strongSecondary = Color(0xFF252528),                    // gray.700
        secondarySurface = Color(0xFF010D01),                   // base.black
        surfaceTertiary = Color(0xFF252528),                    // gray.700
        accentDanger = Color(0xFFC23931),                       // red.600
    ),
    text = TextColors(
        primary = Color(0xFFFFFFFF),                            // base.white
        secondary = Color(0xFFB2B4B8),                          // neutral.400
        tertiary = Color(0xFF85888E),                           // neutral.500
        accentEmphasis = Color(0xFFC3955B),                     // orange.500
        disabled = Color(0xFF94979C),                           // gray.400
        onPrimary = Color(0xFFFFFFFF),                          // base.white
        inverse = Color(0xFFFFFFFF),                            // base.white
        strong = Color(0xFF010D01),                             // base.black
        label = Color(0xFF94979C),                              // gray.400
        placeholder = Color(0xFF94979C),                        // gray.400
        accentWarning = Color(0xFFF79009),                      // orange-variant.500
        strongSecondary = Color(0xFF85888E),                    // neutral.500
        accentDanger = Color(0xFFF04438),                       // red.500
        disabledTertiary = Color(0xFFCECFD2),                   // gray.300
    ),
    icon = IconColors(
        primary = Color(0xFFF9FAFA),                            // gray.50
        secondary = Color(0xFF94979C),                          // gray.400
        tertiary = Color(0xFFFFFFFF),                           // base.white
        onSecondary = Color(0xFF121212),                        // gray.900
        disabled = Color(0xFF94979C),                           // gray.400
        accentPrimary = Color(0xFFC3955B),                      // orange.500
        inverse = Color(0xFFFFFFFF),                            // base.white
        accentWarning = Color(0xFFF79009),                      // orange-variant.500
        accentWarningContainer = Color(0xFF2B2B31),             // gray.600
        accentDanger = Color(0xFFF04438),                       // red.500
        disabledTertiary = Color(0xFFCECFD2),                   // gray.300
        strong = Color(0xFF010D01),                             // base.black
        accentPrimarySurface = Color(0xFFFFFFFF),               // base.white
    ),
    border = BorderColors(
        primary = Color(0xFF94979C),                            // gray.400
        secondary = Color(0xFF61656C),                          // neutral.600
        tertiary = Color(0xFF61656C),                           // neutral.600
        accentEmphasis = Color(0xFFC3955B),                     // orange.500
        disabled = Color(0xFF2B2B31),                           // gray.600
        accentWarning = Color(0xFFF79009),                      // orange-variant.500
        tertiarySubtle = Color(0xFF2F3137),                     // neutral.700
        secondarySubtle = Color(0xFF2F3137),                    // neutral.700
    ),
    button = ButtonColors(
        primary = Color(0xFFC3955B),                            // green.500
        secondary = Color(0xFF2F3137),                          // neutral.700
        tertiary = Color(0xFF22262F),                           // neutral.800
        accentEmphasis = Color(0xFF2F3137),                     // neutral.700
        disabled = Color(0xFF2B2B31),                           // gray.600
        primaryHover = Color(0xFF409452),                       // green.600
        primaryPressed = Color(0xFF347342),                     // green.700
        secondaryPressed = Color(0xFF2B2B31),                   // gray.600
        accentEmphasisPressed = Color(0xFF2B2B31),              // gray.600
        accentDanger = Color(0xFFF04438),                       // red.500
        surface = Color(0xFF2F3137)
    ),
    action = ActionColors(
        primary = Color(0xFFC3955B),                            // orange.500
        secondary = Color(0xFF1A1A1A),                          // gray.800
        section = Color(0xFF2F3137),                            // neutral.700
        primaryHover = Color(0xFF9C7749),                       // orange.600
        primaryPressed = Color(0xFF755937),                     // orange.700
        disabled = Color(0xFF22262F),                           // neutral.800
        disabledTertiary = Color(0xFF2F3137),                   // neutral.700
        tertiary = Color(0xFF252528),                           // gray.700
        strong = Color(0xFF252528),                             // gray.700
        accentDanger = Color(0xFFF04438),                       // red.500
    ),
    section = SectionColors(
        primary = Color(0xFFC3955B),                            // green.500
        secondary = Color(0xFF2F3137),                          // neutral.700
        section = Color(0xFF2F3137),                            // neutral.700
        tertiary = Color(0xFF252528),                           // gray.700
        disabledTertiary = Color(0xFF2F3137),                   // neutral.700
    ),
    field = FieldColors(
        page = Color(0xFF010D01),                               // base.black
        secondary = Color(0xFF252528),                          // gray.700
        section = Color(0xFF2F3137),                            // neutral.700
        accentEmphasis = Color(0xFFC3955B),                     // orange.500
        sectionEmphasis2 = Color(0xFF2F3137),                   // neutral.700
        sectionEmphasisTertiary = Color(0xFF2F3137),            // neutral.700
        accentEmphasisSecondary = Color(0xFFF3EADE),            // orange.100
    ),
    modal = ModalColors(
        primary = Color(0xFF2B2B31),                            // gray.600
        secondary = Color(0xFF2C2F36),                          // gray.500
        tertiary = Color(0xFF252528),                           // gray.700
    ),
)