package com.ziichat.simpleapp.utils

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import java.lang.ref.WeakReference

object NavigationUtils {
    private var navControllerRef: WeakReference<NavController>? = null

    fun setNavController(navController: NavController?) {
        navControllerRef = navController?.let { WeakReference(it) }
    }

    fun popBackStack() {
        if (navControllerRef?.get()?.previousBackStackEntry != null) {
            navControllerRef?.get()?.popBackStack()
        }
    }


    fun navigate(route: String) {
        navControllerRef?.get()?.navigate(route)
    }

    fun navigate(route: String, routeRemove: String, inclusive: Boolean) {
        navControllerRef?.get()?.navigate(route) {
            popUpTo(routeRemove) {
                this.inclusive = inclusive
            }
        }
    }

    fun savedStateHandle(id: String, data: Any?) {
        navControllerRef?.get()?.currentBackStackEntry?.savedStateHandle?.set(id, data)
    }

    fun getSavedStateHandle(): SavedStateHandle? {
        return navControllerRef?.get()?.previousBackStackEntry?.savedStateHandle
    }

    fun getSavedStateHandle(route: String): SavedStateHandle? {
        return navControllerRef?.get()?.getBackStackEntry(route)?.savedStateHandle
    }

}