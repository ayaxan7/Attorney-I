package com.ayaan.attorneyi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform