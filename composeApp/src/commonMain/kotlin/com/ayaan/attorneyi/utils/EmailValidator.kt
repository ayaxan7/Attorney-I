package com.ayaan.attorneyi.utils

object EmailValidator {
    private val emailRegex = Regex(
        "^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"
    )

    fun isValidEmail(email: String): Boolean {
        return email.trim().isNotEmpty() && email.trim().matches(emailRegex)
    }
}
