package org.sic4change.nut4health.ui.commons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.sic4change.nut4health.R
import java.util.*

@Composable
fun CustomDatePickerDialog(
    value: String,
    onDismissRequest: (String) -> Unit
) {
    Dialog(
        onDismissRequest = {
            onDismissRequest("${value.split("/")[0].toInt()}-${value.split("/")[1].toInt()}-${value.split("/")[2].toInt()}")
        }) {
        DatePickerUI(value, onDismissRequest)
    }
}

@Composable
fun DatePickerUI(
    value: String,
    onDismissRequest: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(),
        colors = CardDefaults.cardColors(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 5.dp)
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            val chosenYear = remember { mutableStateOf(value.split("/")[2].toInt()) }
            val chosenMonth = remember { mutableStateOf(value.split("/")[1].toInt()) }
            val chosenDay = remember { mutableStateOf(value.split("/")[0].toInt()) }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(R.string.day),
                    style = MaterialTheme.typography.bodySmall,
                     color = colorResource(R.color.colorPrimary),
                    textAlign = TextAlign.Center
                )

                Text(
                    stringResource(R.string.month),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.colorPrimary),
                    textAlign = TextAlign.Center
                )

                Text(
                    stringResource(R.string.year),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.colorPrimary),
                    textAlign = TextAlign.Center
                )
            }

            DateSelectionSection(
                value = value,
                onYearChosen = { chosenYear.value = it.toInt() },
                onMonthChosen = { chosenMonth.value = it.toInt()},
                onDayChosen = { chosenDay.value = it.toInt() },
            )


            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {

                Button(
                    modifier = Modifier
                        .fillMaxWidth().padding(4.dp),
                    onClick = {
                        onDismissRequest("${chosenDay.value}-${chosenMonth.value}-${chosenYear.value}")
                    },
                    colors = ButtonDefaults.buttonColors(contentColor = colorResource(R.color.colorPrimary)),
                ) {
                    Text("Ok", color = colorResource(R.color.white))
                }
            }
        }
    }
}

@Composable
fun DateSelectionSection(
    value: String,
    onYearChosen: (String) -> Unit,
    onMonthChosen: (String) -> Unit,
    onDayChosen: (String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        InfiniteItemsPicker(
            items = days,
            firstIndex = Int.MAX_VALUE / 2 + (value.split("/")[0].toInt() - 2),
            onItemSelected =  onDayChosen
        )

        InfiniteItemsPicker(
            items = monthsNumber,
            firstIndex = Int.MAX_VALUE / 2 - 4 + (value.split("/")[1].toInt() - 1),
            onItemSelected =  onMonthChosen
        )

        InfiniteItemsPicker(
            items = years,
            firstIndex = Int.MAX_VALUE / 2 + (value.split("/")[2].toInt() - 1967),
            onItemSelected = onYearChosen
        )
    }
}

@Composable
fun InfiniteItemsPicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    firstIndex: Int,
    onItemSelected: (String) -> Unit,
) {

    val listState = rememberLazyListState(firstIndex)
    val currentValue = remember { mutableStateOf("") }

    LaunchedEffect(key1 = !listState.isScrollInProgress) {
        onItemSelected(currentValue.value)
        listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
    }

    Box(modifier = Modifier.height(106.dp)) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
            content = {
                items(count = Int.MAX_VALUE, itemContent = {
                    val index = it % items.size
                    if (it == listState.firstVisibleItemIndex + 1) {
                        currentValue.value = items[index]
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = items[index],
                        modifier = Modifier.alpha(if (it == listState.firstVisibleItemIndex + 1) 1f else 0.3f),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                })
            }
        )
    }
}

/*val currentYear = Calendar.getInstance().get(Calendar.YEAR)
val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
val currentMonth = Calendar.getInstance().get(Calendar.MONTH)*/

val years = (1950..2050).map { it.toString() }
val monthsNumber = (1..12).map { it.toString() }
val days = (1..31).map { it.toString() }
