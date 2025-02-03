package com.xvantage.rental.ui.auth

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  01/02/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

fun Fragment.navigateTo(destination: Int) {
    findNavController().navigate(destination)
}

fun Fragment.navigateToWithArgs(destination: Int, args: Bundle) {
    findNavController().navigate(destination, args)
}

fun Fragment.navigateBack() {
    findNavController().navigateUp()
}

fun Fragment.popBackStack() {
    findNavController().popBackStack()
}

fun Fragment.popBackStack(destination: Int, inclusive: Boolean) {
    findNavController().popBackStack(destination, inclusive)
}

fun Fragment.navigateWithAnimations(
    destination: Int,
    enterAnim: Int,
    exitAnim: Int,
    popEnterAnim: Int,
    popExitAnim: Int
) {
    val options = NavOptions.Builder()
        .setEnterAnim(enterAnim)
        .setExitAnim(exitAnim)
        .setPopEnterAnim(popEnterAnim)
        .setPopExitAnim(popExitAnim)
        .build()
    findNavController().navigate(destination, null, options)
}

fun Fragment.isDestinationOnBackStack(destinationId: Int): Boolean {
    return try {
        findNavController().getBackStackEntry(destinationId)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}

