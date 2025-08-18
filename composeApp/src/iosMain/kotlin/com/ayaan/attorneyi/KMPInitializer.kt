package com.ayaan.attorneyi
import cocoapods.FirebaseCore.FIRApp
import kotlinx.cinterop.ExperimentalForeignApi

object KMPInitializer {
    @OptIn(ExperimentalForeignApi::class)
    fun configureFirebase() {
        FIRApp.configure()
    }
}
