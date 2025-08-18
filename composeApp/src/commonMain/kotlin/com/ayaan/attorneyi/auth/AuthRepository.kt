package com.ayaan.attorneyi.auth

import kotlinx.coroutines.flow.Flow

class AuthRepository(private val firebaseAuth: FirebaseAuth) {

    fun getCurrentUser(): AuthUser? = firebaseAuth.getCurrentUser()

    fun isUserSignedIn(): Boolean = firebaseAuth.isUserSignedIn()

    suspend fun signIn(email: String, password: String): AuthResult =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    suspend fun signUp(email: String, password: String): AuthResult =
        firebaseAuth.createUserWithEmailAndPassword(email, password)

    suspend fun signOut(): Boolean = firebaseAuth.signOut()

    suspend fun resetPassword(email: String): Boolean =
        firebaseAuth.sendPasswordResetEmail(email)

    fun getAuthStateFlow(): Flow<AuthUser?> = firebaseAuth.getAuthStateFlow()
}
