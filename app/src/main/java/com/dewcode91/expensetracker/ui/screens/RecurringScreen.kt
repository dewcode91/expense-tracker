package com.dewcode91.expensetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dewcode91.expensetracker.data.Category
import com.dewcode91.expensetracker.ui.viewmodel.MainViewModel

@Composable
fun RecurringScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val recurring by viewModel.recurring.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("MONTHLY") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var frequencyExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Recurring expenses (${recurring.size})")
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { categoryExpanded = true }) {
                Text(selectedCategory?.name ?: "Select category")
            }
            DropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            selectedCategory = category
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { frequencyExpanded = true }) {
                Text(frequency)
            }
            DropdownMenu(expanded = frequencyExpanded, onDismissRequest = { frequencyExpanded = false }) {
                listOf("DAILY", "WEEKLY", "MONTHLY").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            frequency = option
                            frequencyExpanded = false
                        }
                    )
                }
            }
        }

        Button(onClick = {
            val parsedAmount = amount.toDoubleOrNull() ?: return@Button
            if (title.isNotBlank()) {
                viewModel.addRecurring(
                    title = title.trim(),
                    amount = parsedAmount,
                    categoryId = selectedCategory?.id ?: 0,
                    frequency = frequency
                )
                title = ""
                amount = ""
            }
        }) {
            Text("Save recurring")
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(recurring) { item ->
                Text(text = "${item.title} • ${'$'}${item.amount} • ${item.frequency}")
            }
        }

        Button(onClick = onNavigateBack) { Text("Back") }
    }
}
