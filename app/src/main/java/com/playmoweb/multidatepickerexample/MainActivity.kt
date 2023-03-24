package com.playmoweb.multidatepickerexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.playmoweb.multidatepicker.MultiDatePicker
import com.playmoweb.multidatepicker.utils.extensions.ISO8601
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
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

                val startDate: MutableState<Date?> = remember { mutableStateOf(null) }
                val endDate: MutableState<Date?> = remember { mutableStateOf(null) }

                Scaffold { paddings ->
                    Column(
                        modifier = Modifier
                            .padding(paddings)
                            .padding(horizontal = 10.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "startDate : ",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = startDate.value?.asString("dd/MM/YYYY") ?: "null",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Spacer(Modifier.height(5.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "endDate : ",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = endDate.value?.asString("dd/MM/YYYY") ?: "null",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Spacer(Modifier.height(20.dp))
                        MultiDatePicker(
                            minDate = min.time,
                            maxDate = max.time,
                            startDate = startDate,
                            endDate = endDate,
                        )
                    }
                }
            }
        }
    }
}

fun Date.asString(format: String = ISO8601): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(this)
}
