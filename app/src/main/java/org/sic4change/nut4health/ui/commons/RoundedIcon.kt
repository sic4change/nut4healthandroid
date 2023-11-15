package org.sic4change.nut4health.ui.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun RoundedIcon(modifier: Modifier = Modifier, icon: ImageVector) {
    val backgroundColor = Color.White
    val cornerRadius = 16.dp
    val padding = 8.dp

    Image(
        imageVector = icon,
        contentDescription = "Rounded Icon",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(padding)
    )
}