package com.xvantage.rental.utils.libs.loggingInterceptor


/**
 * @author mujammil on 01/12/24.
 */
enum class Level {
    /**
     * No logs.
     */
    NONE,
    /**
     *
     * Example:
     * <pre>`- URL
     * - Method
     * - Headers
     * - Body
    `</pre> *
     */
    BASIC,
    /**
     *
     * Example:
     * <pre>`- URL
     * - Method
     * - Headers
    `</pre> *
     */
    HEADERS,
    /**
     *
     * Example:
     * <pre>`- URL
     * - Method
     * - Body
    `</pre> *
     */
    BODY
}