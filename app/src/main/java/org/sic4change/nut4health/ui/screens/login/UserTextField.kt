package org.sic4change.nut4health.ui.screens.login

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.sic4change.nut4health.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTextField(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    keyboardActions: KeyboardActions = KeyboardActions(),
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            textColor = colorResource(R.color.black),
            containerColor = colorResource(R.color.white),
            cursorColor = colorResource(R.color.colorAccent),
            disabledLabelColor =  Color.LightGray,
            focusedIndicatorColor = colorResource(R.color.colorAccent),
            unfocusedIndicatorColor = Color.LightGray,
        ),
        label = { Text(stringResource(R.string.email), color = colorResource(R.color.black)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = keyboardActions,
        modifier = Modifier.focusRequester(focusRequester)
    )
}