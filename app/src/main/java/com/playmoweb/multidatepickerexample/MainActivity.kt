package com.playmoweb.multidatepickerexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.playmoweb.multidatepicker.MultiDatePicker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                    primary = Color(0xFF200580),
                    onPrimary = Color.White,
                    surface = Color(0xFFF4F6FF),
                    onSurface = Color.Gray,
                )
            ) {
                Column {
                    MultiDatePicker()
                }
            }
        }
    }
}
