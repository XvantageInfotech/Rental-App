package com.xvantage.rental.utils.libs.loggingInterceptor


import okhttp3.internal.platform.Platform
import okhttp3.internal.platform.Platform.Companion.INFO

/**
 * @author mujammil on 01/12/24.
 */
interface Logger {
    fun log(level: Int = INFO, tag: String?= null, msg: String? = null)

    companion object {
        val DEFAULT: Logger = object : Logger {
            override fun log(level: Int, tag: String?, msg: String?) {
                Platform.get().log("$msg", level, null)
            }
        }
    }
}