package com.ayaan.attorneyi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.ayaan.attorneyi.utils.AppContext
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppContext.setUp(applicationContext)
        window.statusBarColor = Color.Black.toArgb()
        window.navigationBarColor = Color.Black.toArgb()
        Log.d("Firebase", "Firebase initialized: ${FirebaseApp.getApps(this).isNotEmpty()}")
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
