package com.nailorsh.repeton.features.auth.presentation.config

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PinVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Make the string XXX-XXX-XXX
        val trimmed = if (text.text.length >= 6) text.text.substring(0..5) else text.text
        var output = ""
        for (i in trimmed.indices) {
            output += trimmed[i]
            if (i % 2 == 1 && i != 5) output += "-"
        }

        val sixDigitPinTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // [offset [0 - 1] remain the same]
                if (offset <= 1) {
                    return offset
                } else if (offset <= 3) {
                    return offset + 1
                }
                return offset + 2
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 1) {
                    return offset
                } else if (offset <= 4) {
                    return offset - 1
                }
                return offset - 2
            }
        }

        return TransformedText(
            AnnotatedString(output),
            sixDigitPinTranslator
        )
    }
}