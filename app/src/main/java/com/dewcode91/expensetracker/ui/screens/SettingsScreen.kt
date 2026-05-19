package com.dewcode91.expensetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val categories by viewModel.categories.collectAsState()
    val recurring by viewModel.recurring.collectAsState()
    val budgets by viewModel.budgets.collectAsState()

    var categoryName by remember { mutableStateOf("") }
    var categoryColor by remember { mutableStateOf("#FF8A65") }

    var recurringTitle by remember { mutableStateOf("") }
    var recurringAmount by remember { mutableStateOf("") }
    var recurringFrequency by remember { mutableStateOf("MONTHLY") }
    var recurringCategory by remember { mutableStateOf<Category?>(null) }
    var recurringCategoryExpanded by remember { mutableStateOf(false) }
    var recurringFrequencyExpanded by remember { mutableStateOf(false) }

    var budgetLimit by remember { mutableStateOf("") }
    var budgetCategory by remember { mutableStateOf<Category?>(null) }
    var budgetCategoryExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(text = "Settings")
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Categories")
                    OutlinedTextField(
                        value = categoryName,
                        onValueChange = { categoryName = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = categoryColor,
                        onValueChange = { categoryColor = it },
                        label = { Text("Color hex") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(onClick = {
                        if (categoryName.isNotBlank()) {
                            viewModel.addCategory(categoryName.trim(), categoryColor.ifBlank { "#FF8A65" })
                            categoryName = ""
                        }
                    }) {
                        Text("Save category")
                    }

                    if (categories.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            categories.forEach { category ->
                                Text(text = "${category.name} (${category.colorHex})")
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Recurring expenses")
                    OutlinedTextField(
                        value = recurringTitle,
                        onValueChange = { recurringTitle = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = recurringAmount,
                        onValueChange = { recurringAmount = it },
                        label = { Text("Amount") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = { recurringCategoryExpanded = true }) {
                            Text(recurringCategory?.name ?: "Select category")
                        }
                        DropdownMenu(
                            expanded = recurringCategoryExpanded,
                            onDismissRequest = { recurringCategoryExpanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        recurringCategory = category
                                        recurringCategoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = { recurringFrequencyExpanded = true }) {
                            Text(recurringFrequency)
                        }
                        DropdownMenu(
                            expanded = recurringFrequencyExpanded,
                            onDismissRequest = { recurringFrequencyExpanded = false }
                        ) {
                            listOf("DAILY", "WEEKLY", "MONTHLY").forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        recurringFrequency = option
                                        recurringFrequencyExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Button(onClick = {
                        val parsedAmount = recurringAmount.toDoubleOrNull() ?: return@Button
                        if (recurringTitle.isNotBlank()) {
                            viewModel.addRecurring(
                                title = recurringTitle.trim(),
                                amount = parsedAmount,
                                categoryId = recurringCategory?.id ?: 0,
                                frequency = recurringFrequency
                            )
                            recurringTitle = ""
                            recurringAmount = ""
                        }
                    }) {
                        Text("Save recurring")
                    }

                    if (recurring.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            recurring.forEach { item ->
                                Text(text = "${item.title} • ${'$'}${item.amount} • ${item.frequency}")
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Budgets")
                    OutlinedTextField(
                        value = budgetLimit,
                        onValueChange = { budgetLimit = it },
                        label = { Text("Monthly limit") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = { budgetCategoryExpanded = true }) {
                            Text(budgetCategory?.name ?: "Select category")
                        }
                        DropdownMenu(
                            expanded = budgetCategoryExpanded,
                            onDismissRequest = { budgetCategoryExpanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        budgetCategory = category
                                        budgetCategoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Button(onClick = {
                        val parsedLimit = budgetLimit.toDoubleOrNull() ?: return@Button
                        viewModel.addBudget(categoryId = budgetCategory?.id ?: 0, monthlyLimit = parsedLimit)
                        budgetLimit = ""
                    }) {
                        Text("Save budget")
                    }

                    if (budgets.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            budgets.forEach { budget ->
                                Text(text = "Category ${budget.categoryId} • ${'$'}${budget.monthlyLimit}")
                            }
                        }
                    }
                }
            }
        }
    }
}
