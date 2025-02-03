package com.xvantage.rental.ui.auth.fragment.sealed

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  03/02/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */

sealed interface SignInResult {
    data object Failure : SignInResult
    data object Cancelled : SignInResult
    data object NoCredentials : SignInResult
    data object ErrorTypeCredentials : SignInResult
    data class Success(val id: String, val username: String, val avatarUrl: String) : SignInResult
}