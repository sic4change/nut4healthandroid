package org.sic4change.nut4health.ui.screens.login


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.sic4change.nut4health.BuildConfig
import org.sic4change.nut4healthcentrotratamiento.*
import org.sic4change.nut4health.R
import org.sic4change.nut4health.ui.NUT4HealthScreen

import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalAnimationApi
@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel(), onLogin: () -> Unit) {
    val loginState = rememberLoginState()
    val viewModelState by viewModel.state.collectAsState()
    LaunchedEffect(viewModelState.loggedUser) {
        if (viewModelState.loggedUser) {
            onLogin()
        }
    }
    LaunchedEffect(viewModelState.errorLogin) {
        if (viewModelState.errorLogin.isNotEmpty()) {
            loginState.isError.value = true
            loginState.errorType.value = ErrorType.EMAILORPASS
        }
    }

    LaunchedEffect(viewModelState.loading) {
        loginState.showLoginView.value = !viewModelState.loading
    }

    LoginForm(loginState, viewModel::loginUser, viewModel::forgotPassword)


}

@ExperimentalComposeUiApi
@Composable
private fun LoginForm(loginState: LoginState, onLogin: (String, String) -> Unit, onForgotPass: (String) -> Unit) {
    NUT4HealthScreen {
        if (loginState.showLoginView.value) {
            TopView()
            MainView(loginState, onLogin, onForgotPass)
            BottomView()
        }

    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainView(loginState: LoginState,
             onLogin: (String, String) -> Unit, onForgotPass: (String) -> Unit) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            modifier = Modifier
                .wrapContentSize()
                .verticalScroll(loginState.scrollState)
                .padding(16.dp)
        ) {
            AnimatedVisibility(visible = (loginState.email.value.isEmpty() || loginState.email.value.isEmpty())) {
                Image(
                    painter = painterResource(R.mipmap.icon),
                    contentDescription = "nut4health",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(color = colorResource(R.color.colorPrimary))
                        .border(8.dp, colorResource(R.color.white), CircleShape)
                )
            }

            Card {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp)
                ) {
                    UserTextField(
                        value = loginState.email.value,
                        onValueChange = {
                            loginState.email.value = it
                            loginState.isError.value = false},
                        focusRequester = loginState.focusRequester,
                        keyboardActions = KeyboardActions() {loginState.focusRequester.requestFocus()}
                    )
                    PassTextField(
                        value = loginState.pass.value,
                        onValueChange = {
                            loginState.pass.value = it
                            loginState.isError.value = false },
                        focusRequester = loginState.focusRequester,
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                            loginState.loginClicked()
                            onLogin(loginState.email.value, loginState.pass.value)
                        })
                    )

                    AnimatedVisibility(visible = (loginState.email.value.length > 3 && loginState.email.value.contains('@'))) {
                        Text(
                            text = stringResource(R.string.forgot_password),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray,
                            modifier = Modifier.padding(8.dp).clickable {
                                loginState.forgotPasswordClicked()
                            })
                    }

                    AnimatedVisibility(visible = (loginState.isError.value)) {
                        Text(
                            text = stringResource(loginState.errorType.value.message),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red,
                            modifier = Modifier.padding(8.dp))
                    }

                    AnimatedVisibility(visible = (loginState.email.value.length > 3 && loginState.email.value.contains('@') && loginState.pass.value.length > 3)) {
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.colorPrimary)),
                            onClick = {
                                loginState.loginClicked()
                                onLogin(loginState.email.value, loginState.pass.value)
                            }
                        ) {
                            Text(text = stringResource(R.string.login), color = colorResource(R.color.white))
                        }
                    }
                    MessageForgotPassword(loginState.forgotPass.value, loginState::showForgotPassQuestion, loginState.email.value, onForgotPass)
                }
            }
        }

    }
}

@Composable
fun TopView() {
    val configuration = LocalConfiguration.current
    val screenHeight = (configuration.screenHeightDp.dp / 3) + 75.dp

    Box(
        contentAlignment = Alignment.TopCenter,

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(0.dp, 0.dp, 300.dp, 100.dp))
                .background(colorResource(R.color.colorPrimary))
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.height(screenHeight))
        }
    }

}

@Composable
fun BottomView() {
    val uriHandler = LocalUriHandler.current
    Box(
        contentAlignment = Alignment.BottomCenter,

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(100.dp, 100.dp, 0.dp, 0.dp))
                .background(colorResource(R.color.colorPrimary))
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(R.string.terms_and_conditions),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier.clickable {
                    uriHandler.openUri("https://www.sic4change.org/politica-de-privacidad")
                }
            )
            Text(
                text = "Version ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }

    }
}

@Composable
fun MessageForgotPassword(showDialog: Boolean, setShowDialog: () -> Unit, email: String, onForgotPass: (String) -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text(stringResource(R.string.nut4health))
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.colorPrimary)),
                    onClick = {
                        setShowDialog()
                        onForgotPass(email)
                    },
                ) {
                    Text(stringResource(R.string.accept), color = colorResource(R.color.white))
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.colorPrimary)),
                    onClick = {
                        setShowDialog()
                    },
                ) {
                    Text(stringResource(R.string.close),color = colorResource(R.color.white))
                }
            },
            text = {
                Text(stringResource(R.string.forgot_password_question))
            },
        )
    }

}






