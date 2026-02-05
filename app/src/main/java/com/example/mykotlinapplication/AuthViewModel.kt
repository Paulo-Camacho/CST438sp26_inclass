package com.example.mykotlinapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykotlinapplication.data.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userDao: UserDao
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState.LOGGED_OUT)
    val authState: StateFlow<AuthState> = _authState

    private val _loginFailed = MutableStateFlow(false)
    val loginFailed: StateFlow<Boolean> = _loginFailed

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = userDao.login(username, password)

            if (user != null) {
                _loginFailed.value = false
                _authState.value = AuthState.LOGGED_IN
            } else {
                _loginFailed.value = true
            }
        }
    }

    fun logout() {
        _authState.value = AuthState.LOGGED_OUT
        _loginFailed.value = false
    }
}
