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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dewcode91.expensetracker.data.Category
import com.dewcode91.expensetracker.ui.viewmodel.MainViewModel

@Composable
fun BudgetsScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val budgets by viewModel.budgets
    val categories by viewModel.categories

    var monthlyLimit by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Budgets (${budgets.size})")
        OutlinedTextField(
            value = monthlyLimit,
            onValueChange = { monthlyLimit = it },
            label = { Text("Monthly limit") },
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

        Button(onClick = {
            val parsedLimit = monthlyLimit.toDoubleOrNull() ?: return@Button
            viewModel.addBudget(categoryId = selectedCategory?.id ?: 0, monthlyLimit = parsedLimit)
            monthlyLimit = ""
        }) {
            Text("Save budget")
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(budgets) { budget ->
                Text(text = "Category ${budget.categoryId} • ${'$'}${budget.monthlyLimit}")
            }
        }

        Button(onClick = onNavigateBack) { Text("Back") }
    }
}
