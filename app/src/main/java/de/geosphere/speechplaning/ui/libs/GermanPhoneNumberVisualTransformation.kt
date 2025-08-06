package de.geosphere.speechplaning.ui.libs

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

@Suppress("MagicNumber")
class GermanPhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formattedNumber = formatGermanPhoneNumber(text.text)
        val offsetMapping = GermanPhoneNumberOffsetMapping(text.text)
        return TransformedText(AnnotatedString(formattedNumber), offsetMapping)
    }

    private fun formatGermanPhoneNumber(number: String): String {
        val digits = number.filter { it.isDigit() }
        return when {
            digits.isEmpty() -> ""
            digits.startsWith("01") -> formatMobileNumber(digits)
            digits.startsWith("0") -> formatLandlineNumber(digits)
            else -> digits
        }
    }

    private fun formatMobileNumber(digits: String): String {
        return when {
            digits.length <= 2 -> digits
            digits.length <= 5 -> "${digits.substring(0, 4)} ${digits.substring(4)}"
            digits.length <= 9 -> "${digits.substring(0, 4)} ${digits.substring(4, 7)} ${digits.substring(7)}"
            digits.length <= 12 -> "${digits.substring(0, 4)} ${digits.substring(4, 7)} ${digits.substring(7)}"
            else -> "${digits.substring(0, 4)} ${digits.substring(4, 7)} ${digits.substring(7)}"
        }
    }

    private fun formatLandlineNumber(digits: String): String {
        return when {
            digits.length <= 1 -> digits
            digits.length <= 4 -> "${digits.substring(0, digits.length)}"
            digits.length <= 7 -> "${digits.substring(0, digits.length)}"
            digits.length <= 10 -> "${digits.substring(0, digits.length)}"
            digits.length <= 13 -> "${digits.substring(0, digits.length)}"
            else -> "${digits.substring(0, digits.length)}"
        }
    }
}

@Suppress("MagicNumber", "ReturnCount")
class GermanPhoneNumberOffsetMapping(private val originalText: String) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        val digits = originalText.filter { it.isDigit() }
        val formattedNumber = formatGermanPhoneNumber(originalText)

        if (offset <= 0) return 0

        var nonDigits = 0
        for (i in 0 until formattedNumber.length) {
            if (!formattedNumber[i].isDigit()) {
                nonDigits++
            }
            if (i - nonDigits == offset) {
                return i + 1
            }
        }
        return formattedNumber.length
    }

    override fun transformedToOriginal(offset: Int): Int {
        val digits = originalText.filter { it.isDigit() }
        val formattedNumber = formatGermanPhoneNumber(originalText)

        if (offset <= 0) return 0

        var nonDigits = 0
        for (i in 0 until formattedNumber.length) {
            if (!formattedNumber[i].isDigit()) {
                nonDigits++
            }
            if (i == offset) {
                return i - nonDigits
            }
        }
        return originalText.length
    }

    private fun formatGermanPhoneNumber(number: String): String {
        val digits = number.filter { it.isDigit() }
        return when {
            digits.isEmpty() -> ""
            digits.startsWith("01") -> formatMobileNumber(digits)
            digits.startsWith("0") -> formatLandlineNumber(digits)
            else -> digits
        }
    }

    private fun formatMobileNumber(digits: String): String {
        return when {
            digits.length <= 2 -> digits
            digits.length <= 5 -> "${digits.substring(0, 4)} ${digits.substring(4)}"
            digits.length <= 9 -> "${digits.substring(0, 4)} ${digits.substring(4, 7)} ${digits.substring(7)}"
            digits.length <= 12 -> "${digits.substring(0, 4)} ${digits.substring(4, 7)} ${digits.substring(7)}"
            else -> "${digits.substring(0, 4)} ${digits.substring(4, 7)} ${digits.substring(7)}"
        }
    }

    private fun formatLandlineNumber(digits: String): String {
        return when {
            digits.length <= 1 -> digits
            digits.length <= 4 -> digits.substring(0, digits.length)
            digits.length <= 7 -> "${digits.substring(0, digits.length)}"
            digits.length <= 10 -> "${digits.substring(0, digits.length)}"
            digits.length <= 13 -> "${digits.substring(0, digits.length)}"
            else -> "${digits.substring(0, digits.length)}"
        }
    }
}
