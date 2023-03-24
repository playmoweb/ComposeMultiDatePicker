package com.playmoweb.multidatepickerexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.playmoweb.multidatepicker.MultiDatePicker
import java.util.Calendar
import kotlin.time.Duration.Companion.days

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                    primary = Color(0xFF200580),
                    secondary = Color.Gray,
                    onPrimary = Color.White,
                    surface = Color(0xFFF4F6FF),
                    onSurface = Color.Gray,
                )
            ) {
                val min = Calendar.getInstance()
                min.add(Calendar.DAY_OF_MONTH, -10)
                val max = Calendar.getInstance()
                max.add(Calendar.DAY_OF_MONTH, 10)

                Scaffold { paddings ->
                    Column(
                        modifier = Modifier
                            .padding(paddings)
                            .padding(horizontal = 10.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        MultiDatePicker(
                            minDate = min.time,
                            maxDate = max.time
                        )
                    }
                }
            }
        }
    }
}
