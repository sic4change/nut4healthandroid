package org.sic4change.nut4health.ui.commons

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import org.sic4change.nut4health.R

@Composable
fun MessageErrorRole(showDialog: Boolean, setShowDialog: () -> Unit, onLogout: () -> Unit, onLogoutSelected: () -> Unit) {
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
                        onLogout()
                        onLogoutSelected()
                    },
                ) {
                    Text(stringResource(R.string.accept), color = colorResource(R.color.white))
                }
            },
            text = {
                Text(stringResource(R.string.credential_error))
            },
        )
    }

}