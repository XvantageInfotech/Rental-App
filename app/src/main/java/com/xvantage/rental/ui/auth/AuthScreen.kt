package com.xvantage.rental.ui.auth

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  03/02/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */

sealed class AuthScreen {
    object SignIn : AuthScreen()
    object SignUp : AuthScreen()
    object VerifyOtp : AuthScreen()
    object ForgotPassword : AuthScreen()
    object Dashboard : AuthScreen()
}
