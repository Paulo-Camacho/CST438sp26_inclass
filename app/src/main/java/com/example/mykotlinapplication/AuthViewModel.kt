package com.example.mykotlinapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykotlinapplication.data.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState =
        MutableStateFlow(
            if (sessionManager.isLoggedIn())
                AuthState.LOGGED_IN
            else
                AuthState.LOGGED_OUT
        )

    val authState: StateFlow<AuthState> = _authState

    private val _loginFailed = MutableStateFlow(false)
    val loginFailed: StateFlow<Boolean> = _loginFailed

    //  ADD: admin flag (does NOT change existing authState behavior)
    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = userDao.login(username.trim(), password.trim())

            if (user != null) {
                sessionManager.saveLogin()
                _loginFailed.value = false
                _authState.value = AuthState.LOGGED_IN

                //  ADD: mark admin if username is "admin"
                _isAdmin.value = user.username.equals("admin", ignoreCase = true)

            } else {
                _loginFailed.value = true
            }
        }
    }

    fun logout() {
        sessionManager.logout()
        _authState.value = AuthState.LOGGED_OUT
        _loginFailed.value = false

        //  ADD: reset admin flag
        _isAdmin.value = false
    }
}
