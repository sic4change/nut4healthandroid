package org.sic4change.nut4health.ui.navigation


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.sic4change.nut4health.BuildConfig
import org.sic4change.nut4health.ui.theme.ColorBackground
import org.sic4change.nut4health.ui.theme.ColorPrimary
import org.sic4change.nut4health.R

@Composable
fun DrawerContent(
    title: String,
    drawerOptions: List<NavItem>,
    selectedIndex: Int,
    onOptionClick: (NavItem) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    Box(
        Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxWidth()
            .align(Alignment.TopCenter)) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                ColorPrimary,
                                ColorBackground
                            )
                        )
                    )
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(R.mipmap.icon),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(130.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Transparent, CircleShape)
                )

            }
            Text(
                title,
                color = ColorPrimary,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            drawerOptions.forEachIndexed { index, navItem ->
                val selected = selectedIndex == index
                val colors = MaterialTheme.colorScheme

                val localContentColor = if (selected) colors.primary else colors.onBackground

                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    LocalContentColor provides localContentColor
                ) {
                    Row (
                        modifier = Modifier
                            .clickable { onOptionClick(navItem) }
                            .fillMaxWidth()
                            .padding(8.dp, 4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(color = if (selected) LocalContentColor.current.copy(alpha = 0.12f) else colors.surface)
                            .padding(16.dp)
                    ){
                        //CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = navItem.name
                            )
                            Spacer(modifier = Modifier.width(24.dp))
                            Text(
                                text = stringResource(navItem.title),
                            )
                        //}
                    }
                }
        }


        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            ColorPrimary,
                            ColorPrimary
                        )
                    )
                )
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
