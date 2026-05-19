package com.dewcode91.expensetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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

    val frequencyOptions: List<String> = listOf("DAILY", "WEEKLY", "MONTHLY")

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
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "Settings", style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "Manage categories, recurring expenses, and budgets",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            SettingsSectionCard(title = "Categories") {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Name") },
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = categoryColor,
                    onValueChange = { categoryColor = it },
                    label = { Text("Color hex") },
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                PrimaryActionButton(text = "Save category") {
                    if (categoryName.isNotBlank()) {
                        viewModel.addCategory(categoryName.trim(), categoryColor.ifBlank { "#FF8A65" })
                        categoryName = ""
                    }
                }

                if (categories.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        categories.forEach { category ->
                            CategoryPill(label = category.name, supporting = category.colorHex)
                        }
                    }
                }
            }
        }

        item {
            SettingsSectionCard(title = "Recurring expenses") {
                OutlinedTextField(
                    value = recurringTitle,
                    onValueChange = { recurringTitle = it },
                    label = { Text("Title") },
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = recurringAmount,
                    onValueChange = { recurringAmount = it },
                    label = { Text("Amount") },
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PillPicker(
                        label = recurringCategory?.name ?: "Select category",
                        onClick = { recurringCategoryExpanded = true }
                    )
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

                    PillPicker(
                        label = recurringFrequency,
                        onClick = { recurringFrequencyExpanded = true }
                    )
                    DropdownMenu(
                        expanded = recurringFrequencyExpanded,
                        onDismissRequest = { recurringFrequencyExpanded = false }
                    ) {
                        for (option in frequencyOptions) {
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

                PrimaryActionButton(text = "Save recurring") {
                    val parsedAmount = recurringAmount.toDoubleOrNull() ?: return@PrimaryActionButton
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
                }

                if (recurring.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        recurring.forEach { item ->
                            InfoRow(
                                title = item.title,
                                subtitle = item.frequency,
                                trailing = "${'$'}${item.amount}"
                            )
                        }
                    }
                }
            }
        }

        item {
            SettingsSectionCard(title = "Budgets") {
                OutlinedTextField(
                    value = budgetLimit,
                    onValueChange = { budgetLimit = it },
                    label = { Text("Monthly limit") },
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PillPicker(
                        label = budgetCategory?.name ?: "Select category",
                        onClick = { budgetCategoryExpanded = true }
                    )
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

                PrimaryActionButton(text = "Save budget") {
                    val parsedLimit = budgetLimit.toDoubleOrNull() ?: return@PrimaryActionButton
                    viewModel.addBudget(categoryId = budgetCategory?.id ?: 0, monthlyLimit = parsedLimit)
                    budgetLimit = ""
                }

                if (budgets.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        budgets.forEach { budget ->
                            InfoRow(
                                title = "Category ${budget.categoryId}",
                                subtitle = "Monthly limit",
                                trailing = "${'$'}${budget.monthlyLimit}"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            content()
        }
    }
}

@Composable
private fun PrimaryActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text)
    }
}

@Composable
private fun PillPicker(label: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(label)
    }
}

@Composable
private fun CategoryPill(label: String, supporting: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = label, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = supporting,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun InfoRow(title: String, subtitle: String, trailing: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = trailing, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
