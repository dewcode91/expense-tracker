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
fun BudgetsScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val budgets by viewModel.budgets

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Budgets (${budgets.size})")
        Button(onClick = { viewModel.addBudget(categoryId = 0, monthlyLimit = 250.0) }) {
            Text("Add sample budget")
        }
        Button(onClick = onNavigateBack) { Text("Back") }
    }
}
