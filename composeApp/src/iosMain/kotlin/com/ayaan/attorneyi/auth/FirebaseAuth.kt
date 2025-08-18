package com.ayaan.attorneyi.auth

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.*
import cocoapods.FirebaseAuth.*
import kotlin.coroutines.resume

actual class FirebaseAuth {
    private val firebaseAuth = FIRAuth.auth()

    actual fun getCurrentUser(): AuthUser? {
        return firebaseAuth.currentUser?.toAuthUser()
    }

    actual fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    actual suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult {
        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithEmail(email, password) { result, error ->
                if (error != null) {
                    continuation.resume(AuthResult.Error(error.localizedDescription ?: "Sign in failed"))
                } else {
                    result?.user?.let { user ->
                        continuation.resume(AuthResult.Success(user.toAuthUser()))
                    } ?: continuation.resume(AuthResult.Error("Sign in failed"))
                }
            }
        }
    }

    actual suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult {
        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.createUserWithEmail(email, password) { result, error ->
                if (error != null) {
                    continuation.resume(AuthResult.Error(error.localizedDescription ?: "Account creation failed"))
                } else {
                    result?.user?.let { user ->
                        continuation.resume(AuthResult.Success(user.toAuthUser()))
                    } ?: continuation.resume(AuthResult.Error("Account creation failed"))
                }
            }
        }
    }

    actual suspend fun signOut(): Boolean {
        return try {
            val error = firebaseAuth.signOut()
            error == null
        } catch (_: Exception) {
            false
        }
    }

    actual suspend fun sendPasswordResetEmail(email: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.sendPasswordResetWithEmail(email) { error ->
                continuation.resume(error == null)
            }
        }
    }

    actual fun getAuthStateFlow(): Flow<AuthUser?> = callbackFlow {
        val listener = firebaseAuth.addAuthStateDidChangeListener { _, user ->
            trySend(user?.toAuthUser())
        }
        awaitClose {
            firebaseAuth.removeAuthStateDidChangeListener(listener)
        }
    }

    private fun FIRUser.toAuthUser(): AuthUser {
        return AuthUser(
            uid = this.uid,
            email = this.email,
            displayName = this.displayName,
            isEmailVerified = this.emailVerified
        )
    }
}

actual object FirebaseAuthProvider {
    actual fun getFirebaseAuth(): FirebaseAuth = FirebaseAuth()
}
