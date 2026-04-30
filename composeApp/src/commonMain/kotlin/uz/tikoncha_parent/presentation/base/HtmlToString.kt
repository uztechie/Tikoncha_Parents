package uz.tikoncha_parent.presentation.base

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

/**
 * Sodda HTML → AnnotatedString konverteri.
 * Qo'llab-quvvatlanadigan teglar: <b>, <strong>, <i>, <em>, <u>, <s>, <strike>, <br>, <p>
 * Va asosiy HTML entity'lar: &amp; &lt; &gt; &quot; &#39; &nbsp;
 */
fun htmlToAnnotatedString(html: String): AnnotatedString = buildAnnotatedString {
    // Har bir tag uchun ochilgan joyni stack'da saqlaymiz
    val openTags = ArrayDeque<Pair<String, Int>>() // tagName -> textStart

    var i = 0
    val n = html.length

    while (i < n) {
        val ch = html[i]

        if (ch == '<') {
            // Tag oxirini topamiz
            val close = html.indexOf('>', i + 1)
            if (close == -1) {
                // Yopilmagan '<' — oddiy belgi sifatida qo'shamiz
                append(ch)
                i++
                continue
            }

            val raw = html.substring(i + 1, close).trim()
            val isClosing = raw.startsWith("/")
            // Tag nomini ajratib olamiz (atributlarni e'tiborsiz qoldiramiz)
            val tagName = raw
                .removePrefix("/")
                .substringBefore(' ')
                .substringBefore('/')
                .lowercase()

            when {
                // <br> yoki <br/>
                tagName == "br" -> append('\n')

                // <p> ochilishi — agar mavjud matn bo'lsa, yangi qatorga o'tamiz
                tagName == "p" && !isClosing -> {
                    if (length > 0 && this.toAnnotatedString().text.lastOrNull() != '\n') {
                        append('\n')
                    }
                }
                // </p> — paragraf oxirida yangi qator
                tagName == "p" && isClosing -> append('\n')

                isClosing -> {
                    // Yopilgan tag — stack'dan mos ochilgan tagni topib, style qo'llaymiz
                    val idx = openTags.indexOfLast { it.first == tagName }
                    if (idx != -1) {
                        val (_, start) = openTags.removeAt(idx)
                        val end = length
                        if (end > start) {
                            styleFor(tagName)?.let { addStyle(it, start, end) }
                        }
                    }
                }

                else -> {
                    // Ochilgan tag — joriy pozitsiyani saqlab qo'yamiz
                    openTags.addLast(tagName to length)
                }
            }

            i = close + 1
        } else if (ch == '&') {
            // HTML entity
            val semi = html.indexOf(';', i + 1)
            if (semi != -1 && semi - i <= 10) {
                val entity = html.substring(i + 1, semi)
                val decoded = decodeEntity(entity)
                if (decoded != null) {
                    append(decoded)
                    i = semi + 1
                    continue
                }
            }
            append(ch)
            i++
        } else {
            append(ch)
            i++
        }
    }
}

private fun styleFor(tag: String): SpanStyle? = when (tag) {
    "b", "strong" -> SpanStyle(fontWeight = FontWeight.SemiBold)
    "i", "em" -> SpanStyle(fontStyle = FontStyle.Italic)
    "u" -> SpanStyle(textDecoration = TextDecoration.Underline)
    "s", "strike", "del" -> SpanStyle(textDecoration = TextDecoration.LineThrough)
    else -> null
}

private fun decodeEntity(entity: String): String? = when {
    entity == "amp" -> "&"
    entity == "lt" -> "<"
    entity == "gt" -> ">"
    entity == "quot" -> "\""
    entity == "apos" || entity == "#39" -> "'"
    entity == "nbsp" -> "\u00A0"
    entity.startsWith("#x") || entity.startsWith("#X") ->
        entity.drop(2).toIntOrNull(16)?.let { Char(it).toString() }
    entity.startsWith("#") ->
        entity.drop(1).toIntOrNull()?.let { Char(it).toString() }
    else -> null
}