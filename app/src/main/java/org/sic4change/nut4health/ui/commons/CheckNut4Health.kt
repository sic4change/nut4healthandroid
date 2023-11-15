package org.sic4change.nut4health.ui.commons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import org.sic4change.nut4health.R

@Composable
fun CheckNUT4H(text: String, checked: Boolean, onCheckedChange : (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp)
            .clickable(
                onClick = {
                    onCheckedChange(!checked)
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

        ) {
        Text(
            color = colorResource(R.color.colorPrimary),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )

        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(colorResource(R.color.colorPrimaryDark)),
        )
    }
}


@Composable
fun CheckNUT4HDisabled(text: String, checked: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        Text(
            color = colorResource(R.color.colorPrimary),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )

        Checkbox(
            checked = checked,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(colorResource(R.color.colorPrimaryDark)),
        )
    }
}