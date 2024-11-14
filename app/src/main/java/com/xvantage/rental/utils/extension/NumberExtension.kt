package com.xvantage.rental.utils.extension


import java.text.NumberFormat
import java.util.Locale
import kotlin.math.ln
import kotlin.math.pow

/**
 * Convert a [Long] number into its abbreviated string representation.
 *
 * This function will convert numbers into a format like 1K, 1M, 1B, etc.
 * - Numbers below 1,000 will be returned as-is.
 * - Numbers 1,000 and above will be abbreviated as K (thousands), M (millions),
 *   G (billions), T (trillions), P (quadrillions), E (quintillions), and so on.
 *
 * Example:
 * 900 -> "900"
 * 5,500 -> "5.5K"
 * 5,500,000 -> "5.5M"
 *
 * @return The abbreviated string representation of this [Long] number.
 */
fun Long.toAbbreviatedString(startAt: Long = 1_000): String {
    if (this < startAt) return this.toString()

    val exp = (ln(this.toDouble()) / ln(1_000.0)).toInt()
    val value = this / 1_000.0.pow(exp.toDouble())

    // Determine if the value is an integer
    val isIntegerValue = value % 1.0 == 0.0

    // Format the number using integer formatting if it's an integer value after division
    val format = if (isIntegerValue) "%.0f%c" else "%.1f%c"

    val chars = "KMGTPE"
    return String.format(format, value, chars[exp - 1])
}

fun Long.toFormattedString(locale: Locale = Locale.getDefault()): String {
    val formatter = NumberFormat.getNumberInstance(locale)
    return formatter.format(this)
}

fun Double.toFormattedString(locale: Locale = Locale.getDefault()): String {
    val formatter = NumberFormat.getNumberInstance(locale)
    return formatter.format(this)
}

fun Double.roundToDecimalPlaces(decimalPlace : Int = 2): String {
    return String.format("%.${decimalPlace}f", this)
}