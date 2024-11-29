package com.xvantage.rental.utils.libs.loggingInterceptor

import java.io.IOException
import okhttp3.Request
/**
 * @author mujammil on 01/12/24.
 */
interface BufferListener {
    @Throws(IOException::class)
    fun getJsonResponse(request: Request?): String?
}