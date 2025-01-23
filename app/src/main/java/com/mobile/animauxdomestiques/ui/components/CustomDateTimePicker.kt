package com.mobile.animauxdomestiques.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mobile.animauxdomestiques.model.enums.Day
import com.mobile.animauxdomestiques.model.enums.ReminderType
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDateTimePicker(
    reminderType: ReminderType,
    selectedTime: LocalDateTime,
    onSubmit: (LocalDateTime) -> Unit) {

    val selectedDay = remember {
        mutableStateOf(Day.entries.first { it.associatedJavaTimeValue == selectedTime.dayOfWeek })
    }

    val selectedTimeState = rememberTimePickerState(
        initialHour = selectedTime.hour,
        initialMinute = selectedTime.minute,
        is24Hour = true
    );

    val selectedDateMillis = remember {
        selectedTime.atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    val selectedDateState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis
    )

    if (reminderType == ReminderType.UNIQUE) {
        DatePickerDialog(datePicker = selectedDateState, onChange = {onSubmit(buildDate(selectedDateState, selectedTimeState))});
        TimePickerDialog(timeState = selectedTimeState, onChange = {onSubmit(buildDate(selectedDateState, selectedTimeState))});
    }

    if (reminderType == ReminderType.WEEKLY) {
        CustomDropdownMenu(
            label ="Day of the week",
            selectedItem =  selectedDay.value,
            items = Day.entries,
            onItemSelected = { selectedDay.value = it;},
            itemToString = {it.toString()}
        )
        TimePickerDialog(timeState = selectedTimeState, onChange = {onSubmit(buildDate(selectedDay.value, selectedTimeState))});
    }

    if (reminderType == ReminderType.DAILY) {
        TimePickerDialog(timeState = selectedTimeState, onChange = {onSubmit(buildDate(selectedTimeState))});
    }

}

@OptIn(ExperimentalMaterial3Api::class)
fun toString(time: TimePickerState): String {
    var stringTime = ""
    if (time.hour < 10)
        stringTime += "0";
    stringTime += time.hour
    stringTime += ":"
    if (time.minute < 10)
        stringTime += "0";
    stringTime += time.minute

    return stringTime;
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    timeState: TimePickerState,
    onChange : () -> Unit
) {

    var view by remember { mutableStateOf(false) }

    OutlinedTextField(
        label = { Text("Time") },
        value = toString(timeState),
        onValueChange = {},
        modifier = Modifier.clickable {
            view = true
        },
        enabled = false
    )

    if(view) {
        Dialog(
            onDismissRequest = {view = false; onChange()},
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 6.dp,
                modifier =
                Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .background(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.surface
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        text = title,
                        style = MaterialTheme.typography.labelMedium
                    )
                    TimePicker(
                        state = timeState
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    name : String = "Date",
    title : String = "Select a Date",
    datePicker : DatePickerState,
    onChange: ()->Unit
) {
    var showDatePicker by remember { mutableStateOf(false)}

    OutlinedTextField(
        value = datePicker.selectedDateMillis?.let {
            convertMillisToDate(it)
        } ?: "",
        onValueChange = {},
        label = { Text(name) },
        readOnly = true,
        enabled = false,
        modifier = Modifier
            .clickable { showDatePicker = true }
            .fillMaxWidth()
            .height(64.dp)
    )

    if (showDatePicker) {
        Dialog(
            onDismissRequest = { showDatePicker = false; onChange() },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = true
            ),
        ) {
            DatePicker(
                title = {
                    Text(
                        modifier = Modifier.padding(
                            PaddingValues(start = 24.dp, end = 12.dp, top = 16.dp)
                        ),
                        text = title
                    )
                },
                state = datePicker
            )
        }
    }

}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
fun buildDate(targetDay: Day, timePickerState : TimePickerState): LocalDateTime {
    val today = LocalDate.now()
    val daysUntilTarget = (targetDay.associatedJavaTimeValue.value - today.dayOfWeek.value + 7) % 7
    val date = today.plusDays(daysUntilTarget.toLong())

    val time = LocalTime.of(timePickerState.hour, timePickerState.minute);

    return LocalDateTime.of(date, time);
}

@OptIn(ExperimentalMaterial3Api::class)
fun buildDate(dayPickerState: DatePickerState, timePickerState : TimePickerState): LocalDateTime {
    val time = LocalTime.of(timePickerState.hour, timePickerState.minute);
    val date = dayPickerState.selectedDateMillis?.let {
        Date(it)
    };
    return LocalDateTime.of(date?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate(), time);
}

@OptIn(ExperimentalMaterial3Api::class)
fun buildDate(timePickerState : TimePickerState): LocalDateTime {
    val time = LocalTime.of(timePickerState.hour, timePickerState.minute);
    val date = LocalDate.now();
    return LocalDateTime.of(date, time);
}