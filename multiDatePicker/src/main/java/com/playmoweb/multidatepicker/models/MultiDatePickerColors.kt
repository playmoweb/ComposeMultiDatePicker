package com.playmoweb.multidatepicker.models

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class MultiDatePickerColors(
    val cardColor: Color,
    val monthColor: Color,
    val iconColor: Color,
    val weekDayColor: Color,
    val dayNumberColor: Color,
    val selectedDayNumberColor: Color,
    val selectedIndicatorColor: Color,
    val selectedDayBackgroundColor: Color,
) {
    companion object {
        @Composable
        fun defaults() = MultiDatePickerColors(
            cardColor = MaterialTheme.colorScheme.surface,
            monthColor = MaterialTheme.colorScheme.primary,
            iconColor = MaterialTheme.colorScheme.primary,
            weekDayColor = MaterialTheme.colorScheme.onSurface,
            dayNumberColor = MaterialTheme.colorScheme.primary,
            selectedDayNumberColor = MaterialTheme.colorScheme.onPrimary,
            selectedIndicatorColor = MaterialTheme.colorScheme.primary,
            selectedDayBackgroundColor = MaterialTheme.colorScheme.onPrimary,
        )
    }
}
