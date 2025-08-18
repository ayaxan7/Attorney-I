package com.ayaan.attorneyi.utils

import platform.Foundation.NSBundle

actual object AppContext {
    fun setUp() {
        // No setup needed for iOS
    }

    fun get(): NSBundle {
        return NSBundle.mainBundle
    }
}
