package org.sic4change.nut4health.ui.commons

fun arabicToDecimal(numberStr: String): String {
    val arabicDigits = arrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    val result = StringBuilder()
    for (ch in numberStr) {
        if (ch in arabicDigits) {
            result.append(arabicDigits.indexOf(ch))
        } else {
            result.append(ch)
        }
    }
    return result.toString()
}