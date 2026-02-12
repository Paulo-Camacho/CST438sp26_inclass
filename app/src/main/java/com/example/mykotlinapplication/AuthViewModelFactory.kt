package com.example.mykotlinapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mykotlinapplication.data.UserDao

class AuthViewModelFactory(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(userDao, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
