package org.sic4change.nut4health.ui.screens.main

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import org.sic4change.nut4health.R
import org.sic4change.nut4health.ui.NUT4HealthScreen

import androidx.lifecycle.viewmodel.compose.viewModel

@ExperimentalFoundationApi
@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@ExperimentalCoilApi
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val mainState = rememberMainState()
    val viewModelState by viewModel.state.collectAsState()
    val activity = (LocalContext.current as? Activity)

    LaunchedEffect(viewModelState.user) {
        if (viewModelState.user != null) {
            mainState.id.value = viewModelState.user!!.id
            mainState.role.value = viewModelState.user!!.role
            mainState.email.value = viewModelState.user!!.email
            mainState.username.value = viewModelState.user!!.username
            mainState.avatar.value = viewModelState.user!!.photo
        }
    }

    BackHandler {
        activity?.finish()
    }

    NUT4HealthScreen {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.background(
                    Brush.verticalGradient(
                        listOf(
                            colorResource(R.color.colorPrimary),
                            colorResource(R.color.white),
                            colorResource(R.color.white),
                            colorResource(R.color.white),
                            colorResource(R.color.white),
                            colorResource(R.color.colorPrimary),
                        )
                    )
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
            ) {
                Card {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp)
                    ) {

                       Text(text = stringResource(R.string.welcome),
                           style = MaterialTheme.typography.bodySmall,
                           color = colorResource(R.color.colorPrimary))

                    }
                }
            }

        }
    }

}



