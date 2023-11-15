package org.sic4change.nut4health.ui.screens.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel  : ViewModel() {

    private var _state = MutableStateFlow(UiState())

    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            _state.value = UiState(errorLogin = "")
            //_state.value = UiState(loggedUser = FirebaseDataSource.isLogged())
            if (!_state.value.loggedUser) {
                _state.value = UiState(loading = false)
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val loggedUser: Boolean = false,
        val forgotPass: Boolean = false,
        val errorLogin: String = "",
        val errorForgotPass: String = ""
    )

    fun loginUser(email: String, pass: String) {
        viewModelScope.launch {
            _state.value = UiState(errorLogin = "")
            //_state.value = UiState(loggedUser = FirebaseDataSource.login(email.filter { !it.isWhitespace() }, pass.filter { !it.isWhitespace() }))
            if (!_state.value.loggedUser) {
                _state.value = UiState(errorLogin = "Email o pass invalid")
            } else {
                //FirebaseDataSource.loadInitialData()
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            //_state.value = UiState(forgotPass = FirebaseDataSource.forgotPassword(email.filter { !it.isWhitespace() }))
        }
    }
}