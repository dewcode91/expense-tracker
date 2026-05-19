package com.dewcode91.expensetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dewcode91.expensetracker.ui.navigation.Screen
import com.dewcode91.expensetracker.ui.viewmodel.MainViewModel

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onNavigate: (Screen) -> Unit
) {
    val expenses by viewModel.expenses

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Total expenses: ${'$'}${expenses.sumOf { it.amount }}")
                Text(text = "Transactions: ${expenses.size}")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.addQuickExpense() }) {
                Text("Add sample expense")
            }
            Button(onClick = { onNavigate(Screen.Categories) }) {
                Text("Categories")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
    }
}
