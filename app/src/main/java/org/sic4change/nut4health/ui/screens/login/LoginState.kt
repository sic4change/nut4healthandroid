package org.sic4change.nut4health.ui.screens.login

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusRequester
import org.sic4change.nut4health.R

@Composable
fun rememberLoginState(
    scrollState: ScrollState = rememberScrollState(),
    focusRequester: FocusRequester = remember { FocusRequester() },
    email: MutableState<String> = rememberSaveable { mutableStateOf("") },
    pass: MutableState<String> = rememberSaveable { mutableStateOf("") },
    isError: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    errorType: MutableState<ErrorType> = remember { mutableStateOf(ErrorType.NONE) },
    logIn: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    forgotPass: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    showLoginView: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
) = remember{ LoginState(
    scrollState,
    focusRequester,
    email,
    pass,
    isError,
    errorType,
    logIn,
    forgotPass,
    showLoginView) }

class LoginState(
    val scrollState: ScrollState,
    val focusRequester: FocusRequester,
    val email: MutableState<String>,
    val pass: MutableState<String>,
    val isError: MutableState<Boolean>,
    val errorType: MutableState<ErrorType>,
    val logIn: MutableState<Boolean>,
    val forgotPass: MutableState<Boolean>,
    val showLoginView: MutableState<Boolean>
) {

    fun loginClicked() = when {
        email.value.length < 3 || !email.value.contains('@') -> {
            isError.value = true
            errorType.value = ErrorType.EMAIL
        }
        pass.value.length < 3 -> {
            isError.value = true
            errorType.value = ErrorType.PASSWORD
        }
        else -> {
            isError.value = false
            logIn.value = true
            errorType.value = ErrorType.NONE
        }
    }

    fun forgotPasswordClicked() = when {
        email.value.length < 3 || !email.value.contains('@') -> {
            isError.value = true
            errorType.value = ErrorType.EMAIL
        }
        else -> {
            isError.value = false
            forgotPass.value = true
            errorType.value = ErrorType.NONE
        }
    }

    fun showForgotPassQuestion() {
        forgotPass.value = !forgotPass.value
    }

}

enum class ErrorType(val message: Int) {
    NONE(R.string.no_error),
    EMAIL(R.string.valid_email),
    PASSWORD(R.string.valid_pass),
    EMAILORPASS(R.string.email_or_pass_incorrect),
}