package com.ayaan.attorneyi.auth

import com.ayaan.attorneyi.auth.AuthResult
import com.ayaan.attorneyi.auth.AuthUser
import com.google.firebase.auth.FirebaseAuth as GoogleFirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

actual class FirebaseAuth {
    private val firebaseAuth = GoogleFirebaseAuth.getInstance()

    actual fun getCurrentUser(): AuthUser? {
        return firebaseAuth.currentUser?.toAuthUser()
    }

    actual fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    actual suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                AuthResult.Success(user.toAuthUser())
            } ?: AuthResult.Error("Sign in failed")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    actual suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                AuthResult.Success(user.toAuthUser())
            } ?: AuthResult.Error("Account creation failed")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    actual suspend fun signOut(): Boolean {
        return try {
            firebaseAuth.signOut()
            true
        } catch (e: Exception) {
            false
        }
    }

    actual suspend fun sendPasswordResetEmail(email: String): Boolean {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    actual fun getAuthStateFlow(): Flow<AuthUser?> = callbackFlow {
        val listener = GoogleFirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toAuthUser())
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    private fun FirebaseUser.toAuthUser(): AuthUser {
        return AuthUser(
            uid = uid,
            email = email,
            displayName = displayName,
            isEmailVerified = isEmailVerified
        )
    }
}

actual object FirebaseAuthProvider {
    actual fun getFirebaseAuth(): FirebaseAuth = FirebaseAuth()
}
