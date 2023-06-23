package com.playmoweb.multidatepicker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.playmoweb.multidatepicker.models.MultiDatePickerColors
import com.playmoweb.multidatepicker.utils.*
import com.playmoweb.multidatepicker.utils.extensions.toMonthYear
import com.playmoweb.multidatepicker.utils.extensions.toShortDay
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MultiDatePicker(
    modifier: Modifier = Modifier,
    minDate: Date? = null,
    maxDate: Date? = null,
    startDate: MutableState<Date?> = remember { mutableStateOf(null) },
    endDate: MutableState<Date?> = remember { mutableStateOf(null) },
    colors: MultiDatePickerColors = MultiDatePickerColors.defaults(),
    cardRadius: Dp = mediumRadius,
) {
    val localDensity = LocalDensity.current

    val weekDays = listOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY)
    val allYears = (1900..2100).toList()

    val calendar = remember { mutableStateOf(Calendar.getInstance()) }
    val currDate = remember { mutableStateOf(calendar.value.time) }
    val isSelectYear = remember { mutableStateOf(false) }
    val yearScrollState = rememberLazyListState()
    val pickerHeight = remember { mutableStateOf(0.dp) }

    LaunchedEffect(isSelectYear.value) {
        if (isSelectYear.value) {
            val yearIndex = allYears.indexOf(calendar.value.get(Calendar.YEAR)) - 3
            yearScrollState.scrollToItem(yearIndex)
        }
    }

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
            tint = colors.iconColor,
            modifier = Modifier
                .clip(CircleShape)
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
        modifier
            .fillMaxWidth()
            .background(color = colors.cardColor, RoundedCornerShape(cardRadius))
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
                style = MaterialTheme.typography.bodyMedium.copy(color = colors.monthColor),
                modifier = Modifier.clickable {
                    isSelectYear.value = true
                }
            )

            Row {
                MonthPickerIcon(Operation.MINUS)
                Spacer(Modifier.width(xxsmallPadding))
                MonthPickerIcon(Operation.PLUS)
            }
        }
        Spacer(Modifier.height(xxsmallPadding))

        AnimatedContent(
            isSelectYear.value,
            transitionSpec = {
                if (targetState) {
                    fadeIn() with fadeOut()
                } else {
                    fadeIn() with fadeOut()
                }
            }
        ) { showYearSelector ->
            if (showYearSelector) {
                /**
                 * YEARS SELECTOR
                 */
                LazyColumn(
                    modifier = Modifier
                        .height(pickerHeight.value)
                        .fillMaxWidth(),
                    state = yearScrollState,
                    verticalArrangement = Arrangement.spacedBy(xsmallPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(allYears) { year ->
                        val isSelected = year == calendar.value.get(Calendar.YEAR)

                        Text(
                            year.toString(),
                            style = if (isSelected) MaterialTheme.typography.headlineMedium.copy(color = colors.monthColor, fontWeight = FontWeight.Black)
                            else MaterialTheme.typography.titleLarge.copy(color = colors.weekDayColor),
                            modifier = Modifier
                                .height(pickerHeight.value / 7 - (if (isSelected) 0.dp else xsmallPadding))
                                .clickable {
                                    currDate.value = calendar.value.apply {
                                        set(Calendar.YEAR, year)
                                    }.time
                                    isSelectYear.value = false
                                }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.onGloballyPositioned { pickerHeight.value = with(localDensity) { it.size.height.toDp() } }
                ) {
                    /**
                     * DAYS
                     */
                    Row {
                        weekDays.map {
                            Text(
                                text = Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, it) }.time.toShortDay(),
                                style = MaterialTheme.typography.bodyMedium.copy(color = colors.weekDayColor),
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
                        // ADD EMPTY ITEMS TO THE BEGINNING OF THE LIST IF FIRST WEEK DAY OF MONTH DON'T START ON THE FIRST DAY OF THE WEEK
                        daysItem.first().let {
                            val dayOfWeek = if (it!!.day == 0) 7 else it.day
                            (1 until dayOfWeek).forEach { _ -> daysItem.add(0, null) }
                        }

                        val daysByWeek: List<MutableList<Date?>> = daysItem.chunked(7) { it.toMutableList() }
                        // ADD EMPTY ITEMS TO THE END OF THE LIST IF LAST WEEK DAY OF MONTH DON'T START ON THE FIRST DAY OF THE WEEK
                        daysByWeek.last().let { (1..7 - it.size).forEach { _ -> daysByWeek.last().add(null) } }

                        daysByWeek.map {
                            Row {
                                it.map { day ->
                                    val isSelected = day != null && (day == startDate.value || day == endDate.value)
                                    val isBetween = day != null
                                            && startDate.value != null
                                            && endDate.value != null
                                            && (day.after(startDate.value) && day.before(endDate.value))
                                    val isEnabled = day != null
                                            && (minDate == null || day.after(minDate) || day == minDate)
                                            && (maxDate == null || day.before(maxDate) || day == maxDate)

                                    val selectedBackgroundColor = animateColorAsState(targetValue = if (isSelected) colors.selectedIndicatorColor else Color.Transparent, label = "")
                                    val textColor = animateColorAsState(
                                        targetValue = if (isSelected) {
                                            colors.selectedDayNumberColor
                                        } else if (!isEnabled) {
                                            colors.disableDayColor
                                        } else {
                                            colors.dayNumberColor
                                        }, label = ""
                                    )

                                    Box(
                                        Modifier
                                            .weight(1f / 7f)
                                            .aspectRatio(1f)
                                            .background(
                                                if (isBetween || isSelected && endDate.value != null) colors.selectedDayBackgroundColor else Color.Transparent,
                                                if (isSelected) RoundedCornerShape(
                                                    topStartPercent = if (day == startDate.value) 100 else 0,
                                                    topEndPercent = if (day == endDate.value) 100 else 0,
                                                    bottomEndPercent = if (day == endDate.value) 100 else 0,
                                                    bottomStartPercent = if (day == startDate.value) 100 else 0,
                                                ) else RoundedCornerShape(0)
                                            )
                                            .clip(CircleShape)
                                            .clickable(enabled = isEnabled) {
                                                if (day != null) {
                                                    if (startDate.value == null) {
                                                        startDate.value = day
                                                    } else if (endDate.value == null) {
                                                        if (day.before(startDate.value)) startDate.value = day
                                                        else if (day.after(startDate.value)) endDate.value = day
                                                        else if (day == startDate.value) startDate.value = null
                                                    } else {
                                                        startDate.value = day
                                                        endDate.value = null
                                                    }
                                                }
                                            }
                                    ) {
                                        Box(
                                            Modifier
                                                .fillMaxSize()
                                                .background(selectedBackgroundColor.value, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            var dayNumber: Int? = null
                                            if (day != null) {
                                                val calendarDay = Calendar.getInstance().apply { time = day }
                                                dayNumber = calendarDay.get(Calendar.DAY_OF_MONTH)
                                            }

                                            Text(
                                                text = dayNumber?.toString() ?: "",
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
        }
    }
}