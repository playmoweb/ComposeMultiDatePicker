package com.playmoweb.multidatepicker

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.playmoweb.multidatepicker.utils.*
import com.playmoweb.multidatepicker.utils.extensions.toMonthYear
import com.playmoweb.multidatepicker.utils.extensions.toShortDay
import java.util.*

@Composable
fun MultiDatePicker(
    onStartDate: (Date?) -> Unit = { _ -> },
    onEndDate: (Date?) -> Unit = { _ -> },
) {
    val localDensity = LocalDensity.current
    val itemHeight = remember { mutableStateOf(0.dp) }

    val calendar = remember { mutableStateOf(Calendar.getInstance()) }
    val currDate = remember { mutableStateOf(calendar.value.time) }
    val startDate: MutableState<Date?> = remember { mutableStateOf(null) }
    val endDate: MutableState<Date?> = remember { mutableStateOf(null) }

    val days = listOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY)

    LaunchedEffect(startDate.value) { onStartDate(startDate.value) }
    LaunchedEffect(endDate.value) { onEndDate(endDate.value) }

    @Composable
    fun MonthPickerIcon(operation: Operation) {
        return Icon(
            when (operation) {
                Operation.PLUS -> Icons.Rounded.ChevronRight
                Operation.MINUS -> Icons.Rounded.ChevronLeft
            },
            contentDescription = when (operation) {
                Operation.PLUS -> "next"
                Operation.MINUS -> "previous"
            },
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .clip(RoundedCornerShape((smallRadius)))
                .clickable {
                    currDate.value = calendar.value.apply {
                        add(
                            Calendar.MONTH,
                            when (operation) {
                                Operation.PLUS -> 1
                                Operation.MINUS -> -1
                            }
                        )
                    }.time
                }
        )
    }

    Column(
        Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface, RoundedCornerShape(mediumRadius))
            .padding(innerPadding)
    ) {
        /**
         * HEADER
         */
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currDate.value.toMonthYear(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )

            Row {
                MonthPickerIcon(Operation.MINUS)
                Spacer(Modifier.width(xxsmallPadding))
                MonthPickerIcon(Operation.PLUS)
            }
        }
        Spacer(Modifier.height(xxsmallPadding))

        /**
         * DAYS
         */
        Row {
            days.map {
                Text(
                    text = Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, it) }.time.toShortDay(),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.inverseOnSurface),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f / 7f)
                )
            }
        }
        Spacer(Modifier.height(xxsmallPadding))

        /**
         * BODY
         */
        Column(
            verticalArrangement = Arrangement.spacedBy(xxsmallPadding),
        ) {
            val daysNumber: IntRange = (1..calendar.value.getActualMaximum(Calendar.DAY_OF_MONTH))
            val days: List<Date> = daysNumber.map { calendar.value.apply { set(Calendar.DAY_OF_MONTH, it) }.time }
            val daysItem: MutableList<Date?> = days.toMutableList()
            // ADD EMPTY ITEMS TO THE BEGINING OF THE LIST
            daysItem.first().let {
                val dayOfWeek = if (it!!.day == 0) 7 else it.day
                (1 until dayOfWeek).forEach { _ -> daysItem.add(0, null) }
            }

            val daysByWeek: List<MutableList<Date?>> = daysItem.chunked(7) { it.toMutableList() }
            // ADD EMPTY ITEMS TO THE END OF THE LIST
            daysByWeek.last().let { (1..7 - it.size).forEach { _ -> daysByWeek.last().add(null) } }

            daysByWeek.map {
                Row {
                    it.map { day ->
                        val isSelected = day != null && (day == startDate.value || day == endDate.value)
                        val isBetween = day != null
                                && startDate.value != null
                                && endDate.value != null
                                && (day.after(startDate.value) && day.before(endDate.value))

                        val selectedBackgroundColor = animateColorAsState(targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        val textColor = animateColorAsState(targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary)

                        Box(
                            Modifier
                                .weight(1f / 7f)
                                .onGloballyPositioned { coordinates -> itemHeight.value = with(localDensity) { coordinates.size.width.toDp() } }
                                .height(itemHeight.value)
                                .background(
                                    if (isBetween || isSelected && endDate.value != null) MaterialTheme.colorScheme.outline else Color.Transparent,
                                    if (isSelected) RoundedCornerShape(
                                        topStartPercent = if (day == startDate.value) 100 else 0,
                                        topEndPercent = if (day == endDate.value) 100 else 0,
                                        bottomEndPercent = if (day == endDate.value) 100 else 0,
                                        bottomStartPercent = if (day == startDate.value) 100 else 0,
                                    ) else RoundedCornerShape(0)
                                )
                                .clip(CircleShape)
                                .clickable(enabled = day != null && (if (startDate.value != null && endDate.value == null) day.after(startDate.value) else true)) {
                                    if (startDate.value == null) {
                                        startDate.value = day
                                    } else if (endDate.value == null) {
                                        endDate.value = day
                                    } else {
                                        startDate.value = day
                                        endDate.value = null
                                    }
                                }
                        ) {
                            Box(
                                Modifier
                                    .size(itemHeight.value)
                                    .background(selectedBackgroundColor.value, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day?.date?.toString() ?: "",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor.value),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}