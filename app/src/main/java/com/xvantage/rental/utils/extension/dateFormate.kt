package com.xvantage.rental.utils.extension

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

enum class DateFormats(val label: String) {
    dd("dd"),
    MM("MM"),
    MMM("MMM"),
    MMMM("MMMM"),
    yy("yy"),
    yyyy("yyyy"),
    HH("HH"),
    mm("mm"),
    ss("ss"),
    EEEE("EEEE"),
    HHmmsssss("HH:mm:ss.SSS'Z'"),
    HHmmss("HH:mm:ss"),
    hmm("h:mm"),
    hmma("h:mm a"),
    HHmma("HH:mm a"),
    HHmm("HH:mm"),
    a("a"),
    ddMMyyyy("dd-MM-yyyy"),
    yyyyMMdd("yyyy-MM-dd"),
    ddMMMyyyy("dd MMM yyyy"),
    yyyyMMddHHmmss("${yyyyMMdd.label} ${HHmmss.label}"),
    ddMMyyyyhmma("${ddMMyyyy.label} ${hmma.label}"),
    ddMMyyyyHHmmss("${ddMMyyyy.label} ${HHmmss.label}")
}

// Extension function to format date & time to UTC from String
fun String.formatDateTimeToUTC(
    sourceDateFormat: String,
    targetDateFormat: String,
    timeZone: String
): String {
    if (this.isEmpty() || sourceDateFormat.isEmpty() || targetDateFormat.isEmpty() || timeZone.isEmpty()) return ""

    return try {
        val sourceFormat = SimpleDateFormat(sourceDateFormat, Locale.US).apply {
            this.timeZone = TimeZone.getTimeZone(timeZone)  // Assign TimeZone object directly
        }
        val date = sourceFormat.parse(this)
        val targetFormat = SimpleDateFormat(targetDateFormat, Locale.US).apply {
            this.timeZone = TimeZone.getTimeZone("UTC")  // Ensure UTC is assigned
        }
        targetFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}


// Extension function to format date & time to UTC from String (without timezone)
fun String.formatDateTimeToUTC(sourceDateFormat: String, targetDateFormat: String): String {
    if (this.isEmpty() || sourceDateFormat.isEmpty() || targetDateFormat.isEmpty()) return ""

    return try {
        val sourceFormat = SimpleDateFormat(sourceDateFormat, Locale.US).apply {
            timeZone = TimeZone.getDefault()
        }
        val date = sourceFormat.parse(this)
        val targetFormat = SimpleDateFormat(targetDateFormat, Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        targetFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

// Extension function to format Date to UTC
fun Date.formatDateTimeToUTC(targetDateFormat: String): String {
    if (targetDateFormat.isEmpty()) return ""

    return try {
        val targetFormat = SimpleDateFormat(targetDateFormat, Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        targetFormat.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

// Extension function to convert UTC into date from String
fun String.formatUTCToDate(
    sourceDateFormat: String,
    targetDateFormat: String,
    timeZone: String
): String {
    if (this.isEmpty() || sourceDateFormat.isEmpty() || targetDateFormat.isEmpty() || timeZone.isEmpty()) return ""

    return try {
        val sourceFormat = SimpleDateFormat(sourceDateFormat, Locale.US).apply {
            this.timeZone = TimeZone.getTimeZone("UTC")  // Assign TimeZone object directly
        }
        val date = sourceFormat.parse(this)
        val targetFormat = SimpleDateFormat(targetDateFormat, Locale.US).apply {
            this.timeZone = TimeZone.getTimeZone(timeZone)  // Assign TimeZone object directly
        }
        targetFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}


// Extension function to convert UTC into date from String (without timezone)
fun String.formatUTCToDate(sourceDateFormat: String, targetDateFormat: String): String {
    if (this.isEmpty() || sourceDateFormat.isEmpty() || targetDateFormat.isEmpty()) return ""

    return try {
        val sourceFormat = SimpleDateFormat(sourceDateFormat, Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val date = sourceFormat.parse(this)
        val targetFormat = SimpleDateFormat(targetDateFormat, Locale.US)
        targetFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

// Extension function to format date & time from String
fun String.formatDateTime(sourceDateFormat: String, targetDateFormat: String): String {
    if (this.isEmpty() || sourceDateFormat.isEmpty() || targetDateFormat.isEmpty()) return ""

    return try {
        val sourceFormat = SimpleDateFormat(sourceDateFormat, Locale.US)
        val date = sourceFormat.parse(this)
        val targetFormat = SimpleDateFormat(targetDateFormat, Locale.US)
        targetFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

// Extension function to format Date to a specific format
fun Date.formatDateTime(targetDateFormat: String): String {
    if (targetDateFormat.isEmpty()) return ""

    val targetFormat = SimpleDateFormat(targetDateFormat, Locale.US)
    return targetFormat.format(this)
}

// Extension function to parse a String to Date
fun String.parseToDate(sourceDateFormat: String): Date? {
    if (this.isEmpty() || sourceDateFormat.isEmpty()) return null

    return try {
        val targetFormat = SimpleDateFormat(sourceDateFormat, Locale.US)
        targetFormat.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Extension function to check if String is in valid date format
fun String.isValidFormat(sourceDateFormat: String): Boolean {
    if (this.isEmpty() || sourceDateFormat.isEmpty()) return false

    return try {
        val targetFormat = SimpleDateFormat(sourceDateFormat, Locale.US)
        targetFormat.parse(this)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

// Extension function to get time difference in minutes between two Dates
fun Date.timeDifferenceInMinutes(targetDate: Date): Long {
    val difference = this.time - targetDate.time
    return TimeUnit.MILLISECONDS.toMinutes(difference)
}

// Extension function to get time difference in seconds between two Dates
fun Date.timeDifferenceInSeconds(targetDate: Date): Long {
    val difference = this.time - targetDate.time
    return TimeUnit.MILLISECONDS.toSeconds(difference)
}

// Extension function to get time difference in hours between two Dates
fun Date.timeDifferenceInHours(targetDate: Date): Long {
    val difference = this.time - targetDate.time
    return TimeUnit.MILLISECONDS.toHours(difference)
}

// Extension function to get time difference in days between two Dates
fun Date.timeDifferenceInDays(targetDate: Date): Long {
    val difference = this.time - targetDate.time
    return TimeUnit.MILLISECONDS.toDays(difference)
}

// Extension function to check if the given Date is today
fun Date.isToday(): Boolean {
    val calendar = Calendar.getInstance().apply { time = this@isToday }
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    val todayCalendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return calendar.time == todayCalendar.time
}

// Extension function to check if the given Date is tomorrow
fun Date.isTomorrow(): Boolean {
    val calendar = Calendar.getInstance().apply { time = this@isTomorrow }
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    val tomorrowCalendar = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return calendar.time == tomorrowCalendar.time
}

// Extension function to get the day from a Date
fun Date.getDay(): String {
    val dayFormat = SimpleDateFormat(DateFormats.dd.label, Locale.US)
    return dayFormat.format(this)
}
