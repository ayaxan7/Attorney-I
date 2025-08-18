package com.ayaan.attorneyi.auth

import kotlinx.coroutines.flow.Flow

data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val isEmailVerified: Boolean
)

sealed class AuthResult {
    data class Success(val user: AuthUser) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

expect class FirebaseAuth {
    fun getCurrentUser(): AuthUser?
    fun isUserSignedIn(): Boolean
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult
    suspend fun signOut(): Boolean
    suspend fun sendPasswordResetEmail(email: String): Boolean
    fun getAuthStateFlow(): Flow<AuthUser?>
}

expect object FirebaseAuthProvider {
    fun getFirebaseAuth(): FirebaseAuth
}
