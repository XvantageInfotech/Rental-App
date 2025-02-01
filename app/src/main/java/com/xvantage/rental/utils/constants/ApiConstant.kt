package com.xvantage.rental.utils.constants

object ApiConstant {


    // HTTP Methods
    const val METHOD_GET = "GET"
    const val METHOD_POST = "POST"
    const val METHOD_PUT = "PUT"
    const val METHOD_DELETE = "DELETE"

    // Encoding
    const val CHARSET_UTF8 = "UTF-8"

    const val STATUS_SUCCESS: Int = 1

    // response code

    const val STATUS_CODE_SUCCESS: Int = 200

    const val STATUS_CODE_SUCCESS1: Int = 201

    const val STATUS_CODE_CONNECTION_ERROR: Int = -101

    const val STATUS_CODE_ETAG: Int = 304

    const val STATUS_CODE_FORBIDDEN: Int = 403


    const val CODE_ETAG_CATEGORY: Int = 1500

    const val CODE_ETAG_PRODUCT_FEATURE: Int = 1501

    const val CODE_ETAG_PRODUCT_RECOMMENDED: Int = 1502

    // http request method type

    const val UTF8: String = "UTF-8"

    const val GET: String = "GET"

    const val POST: String = "POST"

    const val PUT: String = "PUT"

    const val DELETE: String = "DELETE"


    // server
    //    public static final String SERVER_USER = BuildConfig.SERVER_USER;
    //    public static final String SERVER_CUSTOMER = BuildConfig.SERVER_CUSTOMER;
    //    public static final String SERVER_SUPPLIER = BuildConfig.SERVER_SUPPLIER;
    //    public static final String SERVER_DRIVER = BuildConfig.SERVER_DRIVER;
    //    public static final String SERVER_IMAGE = BuildConfig.SERVER_IMAGE;
    // request header

    const val HEADER_CONTENT_TYPE: String = "Content-Type"

    const val HEADER_AUTH_TOKEN: String = "auth-token"

    // request header - content type

    const val CONTENT_FORM_DATA: String = "form-data"

    const val CONTENT_TYPE_JSON: String = "application/json"

    // extra params

    const val DEFAULT_PARAM_LATLONG: String = "&latitude=%s&longitude=%s"

    const val DEFAULT_PARAM_LATLONG_MAP: String = "&map_lat=%s&map_long=%s"

    const val FROM: String = "?from=%s"
}

