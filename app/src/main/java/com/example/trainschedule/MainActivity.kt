package com.example.trainschedule

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.FragmentActivity
import com.example.trainschedule.ui.theme.TrainScheduleTheme
import com.example.trainschedule.ui.theme.Typography
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrainScheduleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                    ) {
                    BuildUi()
                }
            }
        }
    }
}

@Composable
fun BuildUi() {
    Box(
        modifier = Modifier
            .padding(all = 24.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (column, button) = createRefs()
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(bottom = 64.dp)
                    .constrainAs(column) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            ) {
                HeaderText(text = "From:")
                DropdownView()
                HeaderText(text = "To:")
                DropdownView()
                HeaderText(text = "When:")
                DatePickerView(datePicked = "Select Date", updatedDate = {  })
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .constrainAs(button) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            ) {
                Text(text = "See Times")
            }
        }
    }
}

@Composable
fun HeaderText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = Typography.h1,
        modifier = modifier.padding(
            top = 32.dp,
            bottom = 8.dp
        )
    )
}

@Composable
fun DropdownView() {
    val expanded = remember { mutableStateOf(false) }
    val items = listOf("A", "B", "C", "D", "E", "F")
    val selectedIndex = remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
            .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            .clickable(onClick = { expanded.value = true })
    ) {
        Text(
            items[selectedIndex.value], modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex.value = index
                    expanded.value = false
                }) {
                    Text(text = s)
                }
            }
        }
    }
}

@Composable
fun DatePickerView(
    datePicked: String?,
    updatedDate: (date: Long?) -> Unit,
    activity: FragmentActivity = LocalContext.current as FragmentActivity
) {
    val selectedDateValue = remember { mutableStateOf(datePicked) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
//            .padding(top = 10.dp, bottom = 64.dp)
            .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            .clickable {
                showDatePicker(activity) {
                    updatedDate(it)
                    it?.let {
                        selectedDateValue.value = it.toStringDate()
                    }
                }
            }
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            val (label, iconView) = createRefs()

            Text(
                text = selectedDateValue.value ?: "Date Picker",
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(label) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(iconView.start)
                        width = Dimension.fillToConstraints
                    },
            )

            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp, 20.dp)
                    .constrainAs(iconView) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                tint = MaterialTheme.colors.onSurface
            )

        }

    }
}

private fun showDatePicker(
    activity: FragmentActivity,
    updatedDate: (Long?) -> Unit
) {
    val picker = MaterialDatePicker.Builder.datePicker().build()
    picker.show(activity.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener {
        updatedDate(it)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TrainScheduleTheme {
        BuildUi()
    }
}

fun Long.toStringDate(): String = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(this)