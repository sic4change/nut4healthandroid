package org.sic4change.nut4healthcentrotratamiento.ui.commons

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import org.sic4change.nut4health.R

enum class Gender {
    MALE, FEMALE
}

@Composable
fun GenderToggleButton(
    defaultGender: Gender,
    enabled: Boolean,
    onGenderSelected: (Gender) -> Unit
) {
    val checkedState = remember { mutableStateOf(false) }
    checkedState.value = if (defaultGender == Gender.FEMALE) false else true
    println("${checkedState.value}")
    Column(
        Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconToggleButton(
            enabled = enabled,
            checked =checkedState.value,
            onCheckedChange = {
                checkedState.value = !checkedState.value
                if (!checkedState.value) onGenderSelected(Gender.FEMALE) else onGenderSelected(Gender.MALE)
                println("${checkedState.value}")
            },
            modifier = Modifier.padding(10.dp)
        ) {
            Icon(
                tint = colorResource(R.color.colorPrimary),
                imageVector = if (checkedState.value) Icons.Filled.Male else Icons.Filled.Female,
                contentDescription = "Icon",
                modifier = Modifier.size(45.dp)
            )
        }
    }
}