package com.ayaan.attorneyi.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.ayaan.attorneyi.utils.EmailValidator

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false,
    val currentUser: AuthUser? = null,
    val errorMessage: String? = null
)

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    init {
        // Initialize with current auth state
        _uiState.value = _uiState.value.copy(
            isSignedIn = authRepository.isUserSignedIn(),
            currentUser = authRepository.getCurrentUser()
        )

        // Listen to auth state changes
        viewModelScope.launch {
            authRepository.getAuthStateFlow().collect { user ->
                _uiState.value = _uiState.value.copy(
                    isSignedIn = user != null,
                    currentUser = user
                )
            }
        }
    }

    fun updateEmail(newEmail: String) {
        email = newEmail
        clearError()
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        clearError()
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        clearError()
    }

    fun signIn() {
        if (!validateSignInInput()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = authRepository.signIn(email.trim(), password)) {
                is AuthResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSignedIn = true,
                        currentUser = result.user
                    )
                    clearInputs()
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun signUp() {
        if (!validateSignUpInput()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = authRepository.signUp(email.trim(), password)) {
                is AuthResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSignedIn = true,
                        currentUser = result.user
                    )
                    clearInputs()
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val success = authRepository.signOut()
            if (success) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSignedIn = false,
                    currentUser = null
                )
                clearInputs()
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to sign out"
                )
            }
        }
    }

    fun resetPassword() {
        if (email.trim().isEmpty()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter your email address")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val success = authRepository.resetPassword(email.trim())
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = if (success) null else "Failed to send reset email"
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun validateSignInInput(): Boolean {
        when {
            email.trim().isEmpty() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Email is required")
                return false
            }
            !EmailValidator.isValidEmail(email.trim()) -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Invalid email format")
                return false
            }
            password.isEmpty() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Password is required")
                return false
            }
        }
        return true
    }

    private fun validateSignUpInput(): Boolean {
        when {
            email.trim().isEmpty() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Email is required")
                return false
            }
            !EmailValidator.isValidEmail(email.trim()) -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Invalid email format")
                return false
            }
            password.length < 6 -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Password must be at least 6 characters")
                return false
            }
            password != confirmPassword -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Passwords do not match")
                return false
            }
        }
        return true
    }

    private fun clearInputs() {
        email = ""
        password = ""
        confirmPassword = ""
    }
}
