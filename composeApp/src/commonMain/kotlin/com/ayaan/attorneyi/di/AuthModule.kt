package com.ayaan.attorneyi.di

import com.ayaan.attorneyi.auth.AuthRepository
import com.ayaan.attorneyi.auth.FirebaseAuthProvider
import com.ayaan.attorneyi.auth.AuthViewModel
import org.koin.dsl.module
import org.koin.compose.viewmodel.dsl.viewModel

val authModule = module {
    single { FirebaseAuthProvider.getFirebaseAuth() }
    single { AuthRepository(get()) }
    viewModel { AuthViewModel(get()) }
}
