package com.xvantage.rental.utils.constants

object Constant {

    // -------------- API Request Parameters --------------
    /**
     * Constants used for passing request parameters in API calls or method parameters.
     */
    const val PARAM_USER_ID = "userId"
    const val PARAM_ACCESS_TOKEN = "accessToken"
    const val PARAM_PAGE_NUMBER = "pageNumber"
    const val PARAM_LIMIT = "limit"
    /**
     * static appended text
     */
    const val const_aestrisk: String = "*"
    const val const_dot: String = "•"
    const val const_multiply: String = "X"
    const val const_percentage: String = "%"
    const val const_doller: String = "$"
    const val const_hash: String = "#"
    const val const_slash_forward: String = "/"
    const val const_colon: String = ":"
    const val const_rs: String = "Rs."
    const val const_after_point_2: String = "%.2f"

    // -------------- General Constants --------------
    /**
     * General constants used for formatting, delays, and other non-API related parameters.
     */
    const val BIRTHDAY_EMPTY = "0000-00-00"
    const val PHONE_FORMATTER = "### ### ####"

    // Delay constants (in milliseconds)
    const val DELAY_SPLASH = 3000
    const val DELAY_API_CALL = 20000
    const val DELAY_FRAGMENT_PAUSE = 100
    const val DELAY_SCROLL_PAUSE = 300

    // -------------- Receiver ACTIONS --------------
    /**
     * Constants for broadcasting receiver actions.
     */
    const val ACTION_PUSH_NOTIFICATION = "PUSH_NOTIFICATION"
    const val ACTION_PUSH_NOTIFICATION_ORDER_STATUS = "PUSH_NOTIFICATION_ORDER_STATUS"

}
