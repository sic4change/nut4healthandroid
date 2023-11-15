package org.sic4change.nut4health.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import org.sic4change.nut4health.R

@Composable
fun AppBarIcon(imageVector: ImageVector, onClick: () -> Unit, contentDescription: String? = null) {
    IconButton(onClick = onClick) {
        Icon(imageVector = imageVector, contentDescription = contentDescription, tint = colorResource(
            R.color.white))
    }

}