package com.ayaan.attorneyi.utils

import com.google.firebase.FirebaseApp
import android.content.Context

actual object FirebaseInitializer {
    actual fun initialize() {
        // Firebase is automatically initialized on Android
        // but we can add explicit initialization if needed
    }
}
