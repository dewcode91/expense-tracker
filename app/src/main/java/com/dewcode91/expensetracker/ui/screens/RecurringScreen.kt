package com.dewcode91.expensetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dewcode91.expensetracker.ui.viewmodel.MainViewModel

@Composable
fun RecurringScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val recurring by viewModel.recurring

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Recurring expenses (${recurring.size})")
        Button(onClick = { viewModel.addRecurring("Subscription", 9.99, 0, "MONTHLY") }) {
            Text("Add sample recurring")
        }
        Button(onClick = onNavigateBack) { Text("Back") }
    }
}
