import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import org.sic4change.nut4health.R
import java.text.SimpleDateFormat
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.unit.dp
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerView(
        context: Context,
        showMonths: Boolean,
        value: String,
        setValue: (String) -> Unit
) {
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val now = Calendar.getInstance()
    mYear = now.get(Calendar.YEAR)
    mMonth = now.get(Calendar.MONTH)
    mDay = now.get(Calendar.DAY_OF_MONTH)
    now.time = Date()

    val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val cal = Calendar.getInstance()
                cal.set(year, month, dayOfMonth)
                setValue(SimpleDateFormat("dd-MM-yyyy").format(cal.time))
            }, mYear, mMonth, mDay
    )

    val day1= Calendar.getInstance()
    day1.set(1910,1,1)
    datePickerDialog.datePicker.minDate = day1.timeInMillis
    datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
    Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val splitDate = value.split("/")
        val yearsLabel = ChronoUnit.YEARS.between(ZonedDateTime.parse(splitDate[2] + "-" +
                splitDate[1] + "-" + splitDate[0] + "T00:00:00.000Z"), ZonedDateTime.now())
        var monthsLabel = ChronoUnit.MONTHS.between(ZonedDateTime.parse(splitDate[2] + "-" +
                splitDate[1] + "-" + splitDate[0] + "T00:00:00.000Z"), ZonedDateTime.now())
        if (showMonths) {
            monthsLabel -= (yearsLabel * 12)
        }

        TextField(
                value =  value,
                label = {
                    if (!showMonths)
                        Text("≈ ${yearsLabel} ${stringResource(R.string.years)} ", color = colorResource(R.color.colorAccent))
                    else {
                        Text("≈ ${yearsLabel} ${stringResource(R.string.years)} ${monthsLabel} ${stringResource(R.string.months)} ", color = colorResource(R.color.colorAccent))
                    }
                },
                onValueChange = setValue,
                placeholder = { Text(text = stringResource(R.string.date)) },
                enabled = false,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = colorResource(R.color.colorPrimary),
                    cursorColor = colorResource(R.color.colorAccent),
                    disabledLabelColor =  colorResource(androidx.browser.R.color.browser_actions_bg_grey),
                    focusedIndicatorColor = colorResource(R.color.colorAccent),
                    unfocusedIndicatorColor = colorResource(androidx.browser.R.color.browser_actions_bg_grey),
                ),
                textStyle = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp).clickable {
                        datePickerDialog.show()
                    },
                leadingIcon = {
                Icon(Icons.Default.Cake, null, tint = colorResource(R.color.colorPrimary),  modifier = Modifier.clickable { /* .. */})
                }

        )
    }

}
