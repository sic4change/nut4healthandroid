package org.sic4change.nut4health.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sic4change.domain.User

class MainViewModel() : ViewModel() {


    private var _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            //_state.value = UiState(user = FirebaseDataSource.getLoggedUser())
        }
    }

    data class  UiState(
        val loading: Boolean = false,
        val user: User? = null,
        val logout: Boolean = false,
        val changePassword: Boolean = false,
        val exit: Boolean = false,
        var avatarUrl: String = ""
    )


    fun logout() {
        viewModelScope.launch {
            //FirebaseDataSource.logout()
            _state.value = UiState(user = null)
            _state.value = UiState(logout = true)
        }
    }

    fun changePassword(email: String) {
        viewModelScope.launch {
            //_state.value = UiState(changePassword = FirebaseDataSource.forgotPassword(email.filter { !it.isWhitespace() }))
        }
    }

}