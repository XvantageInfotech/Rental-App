package com.xvantage.rental.utils.extension


fun String.isEmailOrPhone(): String {
    val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    val phonePattern = "^[+]?[0-9]{10,13}$"

    return when {
        this.matches(emailPattern.toRegex()) -> "Email"
        this.matches(phonePattern.toRegex()) -> "Phone"
        else -> "Unknown"
    }
}

fun String.isEmail(): Boolean {
    return this.isEmailOrPhone() == "Email"
}

fun String.isPhoneNumber(): Boolean {
    return this.isEmailOrPhone() == "Phone"
}

fun String.maskedEmail(): String {
    return this.split("@").let {
        if (it[0].length > 3) {
            it[0].first() + "***" + it[0].last() + "@" + it[1]
        } else {
            // If the local part is less than or equal to 3 characters, mask them all
            "***@" + it[1]
        }
    }
}

fun String.Companion.empty() = ""

val String?.nullIfEmpty: String?
    get() = this.takeIf { it.isNullOrEmpty().not() }