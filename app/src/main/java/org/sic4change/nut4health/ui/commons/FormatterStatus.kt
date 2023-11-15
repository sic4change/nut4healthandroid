package org.sic4change.nut4health.ui.commons

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.sic4change.nut4health.R

@Composable
fun formatStatus(status: String) : String {
    if (status.equals("Normopeso") || (status.equals("Poids Normal"))) {
        return stringResource(R.string.normopeso)
    } else if (status.equals("Desnutrición Aguda Moderada") || (status.equals("Malnutrition Aiguë modérée"))) {
        return stringResource(R.string.aguda_moderada)
    } else if (status.equals("Desnutrición Aguda Severa") || (status.equals("Malnutrition Aiguë Sévère"))) {
        return stringResource(R.string.aguda_severa)
    } else {
        return stringResource(R.string.objetive_weight)
    }
}