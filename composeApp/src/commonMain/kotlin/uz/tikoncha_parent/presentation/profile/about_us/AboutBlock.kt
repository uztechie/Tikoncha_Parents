// AboutContent.kt
package uz.tikoncha_parent.presentation.profile.about_us

import org.jetbrains.compose.resources.StringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*

sealed interface AboutBlock {
    data class Paragraph(val text: StringResource) : AboutBlock
    data class Heading(val text: StringResource) : AboutBlock
    data class BulletList(val items: List<StringResource>) : AboutBlock
    data class Quote(val text: StringResource) : AboutBlock
    data object Spacing : AboutBlock
}

val AboutUsContent: List<AboutBlock> = listOf(
    AboutBlock.Paragraph(Res.string.about_intro),
    AboutBlock.Spacing,
    AboutBlock.Paragraph(Res.string.about_body),
    AboutBlock.Spacing,
    AboutBlock.Heading(Res.string.about_features_title),
    AboutBlock.BulletList(
        listOf(
            Res.string.about_feature_1,
            Res.string.about_feature_2,
            Res.string.about_feature_3
        )
    ),
    AboutBlock.Spacing,
    AboutBlock.Paragraph(Res.string.about_mission),
    AboutBlock.Spacing,
    AboutBlock.Quote(Res.string.about_belief)
)