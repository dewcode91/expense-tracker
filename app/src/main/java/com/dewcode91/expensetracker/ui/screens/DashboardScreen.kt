package com.dewcode91.expensetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.dewcode91.expensetracker.ui.navigation.Screen
import com.dewcode91.expensetracker.ui.viewmodel.MainViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onNavigate: (Screen) -> Unit
) {
    val expenses by viewModel.expenses.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var note by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Total expenses: ${'$'}${expenses.sumOf { it.amount }}")
                Text(text = "Transactions: ${expenses.size}")
            }
        }

        Text(text = "Add expense")
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
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { categoryExpanded = true }) {
                Text(selectedCategory?.name ?: "Select category")
            }
            DropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
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

        Button(
            onClick = {
                val parsedAmount = amount.toDoubleOrNull() ?: return@Button
                val parsedDate = runCatching { LocalDate.parse(date) }.getOrElse { LocalDate.now() }
                viewModel.addExpense(
                    title = title.trim(),
                    amount = parsedAmount,
                    categoryId = selectedCategory?.id ?: 0,
                    date = parsedDate,
                    note = note.ifBlank { null }
                )
                title = ""
                amount = ""
                note = ""
                date = LocalDate.now().toString()
            }
        ) {
            Text("Save expense")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onNavigate(Screen.Categories) }) {
                Text("Categories")
            }
            Button(onClick = { onNavigate(Screen.Recurring) }) {
                Text("Recurring")
            }
            Button(onClick = { onNavigate(Screen.Budgets) }) {
                Text("Budgets")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onNavigate(Screen.Charts) }) {
                Text("Charts")
            }
            Button(onClick = { onNavigate(Screen.Export) }) {
                Text("Export")
            }
            Button(onClick = { onNavigate(Screen.Receipts) }) {
                Text("Receipts")
            }
        }

        Text(text = "Recent expenses")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(expenses) { expense ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = expense.title)
                        Text(text = "${'$'}${expense.amount} • ${expense.date}")
                    }
                }
            }
        }
    }
}
