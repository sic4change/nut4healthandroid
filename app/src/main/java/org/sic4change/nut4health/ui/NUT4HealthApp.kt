package org.sic4change.nut4health.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.annotation.ExperimentalCoilApi
import org.sic4change.nut4health.R
import org.sic4change.nut4health.ui.navigation.Feature
import org.sic4change.nut4health.ui.navigation.NavCommand
import org.sic4change.nut4health.ui.navigation.Navigation
import org.sic4change.nut4health.ui.theme.NUT4HealthTheme

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun NUT4HealthApp() {
    val appState =  rememberNUT4HealthAppState()
    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
    NUT4HealthScreen {
        Scaffold (
            topBar = {
                println("Aqui: ${navBackStackEntry?.destination?.route}")
                if (!NavCommand.ContentType(Feature.LOGIN).route.contains(navBackStackEntry?.destination?.route.toString())) {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.app_name),
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(),
                    )
                }

            }

        ) {
            Navigation(appState.navController)
        }
    }
    /*NUT4HealthScreen {
        if (navBackStackEntry?.destination?.route !=  NavCommand.ContentType(Feature.LOGIN).route) {
            Scaffold (
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.app_name),
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(),
                    )
                    *//*TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.app_name),
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(),
                    ) {
                        Row(Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f)) {
                                if (!appState.currentRoute.contains("login/detail") &&
                                    !appState.currentRoute.contains("settings/home") &&
                                    !appState.currentRoute.contains("nextvisits/home")
                                ) {
                                    AppBarIcon(
                                        imageVector = Icons.Default.ArrowBack,
                                        onClick = { appState.onUpClick() }
                                    )
                                } else {
                                    AppBarIcon(
                                        imageVector = Icons.Default.Menu,
                                        onClick = { appState.onMenuClick() }
                                    )
                                }
                            }
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Image(
                                    modifier = Modifier.size(120.dp, 120.dp),
                                    painter = painterResource(id = R.mipmap.ic_logo_header),
                                    contentDescription = null,
                                )
                            }
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                                IconButton(onClick = { appState.onHomeClick() }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Home,
                                        contentDescription = "home",
                                        tint = colorResource(R.color.white)
                                    )
                                }
                            }
                        }
                    }*//*

                },

                drawerContent = {
                    if (navBackStackEntry?.destination?.route !=  NavCommand.ContentType(Feature.LOGIN).route) {
                        DrawerContent(
                            title = stringResource(R.string.app_name),
                            drawerOptions = NUT4HealthAppState.DRAWER_OPTIONS,
                            selectedIndex = appState.drawerSelectedIndex,
                            onOptionClick = { navItem ->
                                appState.onDrawerOptionClick(navItem)
                            }
                        ) }
                },
                scaffoldState = appState.scaffoldState
            ) {
                Navigation(appState.navController)
            }
        } else {
            Scaffold (
                scaffoldState = appState.scaffoldState
            ) {
                Navigation(appState.navController)
            }
        }

    }*/
}

@Composable
fun NUT4HealthScreen(content: @Composable () -> Unit) {
    NUT4HealthTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = content
        )
    }
}