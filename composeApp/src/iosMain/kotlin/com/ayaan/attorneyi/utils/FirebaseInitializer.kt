package com.ayaan.attorneyi.utils

import cocoapods.FirebaseCore.FIRApp
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle

actual object FirebaseInitializer {
    @OptIn(ExperimentalForeignApi::class)
    actual fun initialize() {
        FIRApp.configure()
    }
}

// Update AppContext to include Firebase initialization
//actual object AppContext {
//    fun setUp() {
////        FirebaseInitializer.initialize()
//    }
//
//    fun get(): NSBundle {
//        return NSBundle.mainBundle
//    }
//}
