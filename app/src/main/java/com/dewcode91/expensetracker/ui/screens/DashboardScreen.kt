package com.dewcode91.expensetracker.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dewcode91.expensetracker.data.Category
import com.dewcode91.expensetracker.data.Expense
import com.dewcode91.expensetracker.ui.theme.CardDarkAlt
import com.dewcode91.expensetracker.ui.theme.Green
import com.dewcode91.expensetracker.ui.theme.Orange
import com.dewcode91.expensetracker.ui.theme.PrimaryBlue
import com.dewcode91.expensetracker.ui.theme.Purple
import com.dewcode91.expensetracker.ui.theme.Red
import com.dewcode91.expensetracker.ui.theme.Teal
import com.dewcode91.expensetracker.ui.viewmodel.MainViewModel
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MainViewModel
) {
    val expenses by viewModel.expenses.collectAsState()
    val categories by viewModel.categories.collectAsState()
    var selectedRange by remember { mutableStateOf("Week") }

    val totalExpenses = expenses.sumOf { it.amount }
    val totalIncome = 0.0
    val totalBalance = totalIncome - totalExpenses

    val categorySpend = buildCategorySpend(expenses, categories)

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "Dashboard", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = "December 2024",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryBlue)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Total Balance", color = Color.White)
                    Text(
                        text = "${'$'}${formatAmount(kotlin.math.abs(totalBalance))}",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                        StatColumn(label = "Income", value = "${'$'}${formatAmount(totalIncome)}", valueColor = Color.White)
                        StatColumn(label = "Expenses", value = "${'$'}${formatAmount(totalExpenses)}", valueColor = Color.White)
                    }
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Week", "Month", "Year").forEach { range ->
                    TimeRangeChip(
                        label = range,
                        selected = selectedRange == range,
                        onClick = { selectedRange = range }
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Spending by Category", style = MaterialTheme.typography.titleMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DonutChart(
                            modifier = Modifier
                                .size(140.dp)
                                .aspectRatio(1f),
                            segments = categorySpend
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            categorySpend.take(4).forEach { item ->
                                LegendRow(color = item.color, label = item.label, value = "${'$'}${formatAmount(item.amount)}")
                            }
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Recent Transactions", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = { /* TODO */ }) {
                    Text(text = "See All", color = PrimaryBlue)
                }
            }
        }

        items(expenses.take(5)) { expense ->
            TransactionRow(expense = expense, categories = categories)
        }

        if (expenses.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "No transactions yet", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "Add your first expense to see it here.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatColumn(label: String, value: String, valueColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = label, color = valueColor.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
        Text(text = value, color = valueColor, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun TimeRangeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        modifier = Modifier.height(36.dp),
        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
            containerColor = if (selected) PrimaryBlue else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun TransactionRow(expense: Expense, categories: List<Category>) {
    val category = categories.firstOrNull { it.id == expense.categoryId }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BoxIcon(color = PrimaryBlue)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = expense.title, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = category?.name ?: "Uncategorized",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "-${'$'}${formatAmount(expense.amount)}",
                color = Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun BoxIcon(color: Color) {
    Spacer(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.2f))
    )
}

@Composable
private fun LegendRow(color: Color, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.bodySmall)
            Text(text = value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun DonutChart(modifier: Modifier, segments: List<CategorySpend>) {
    val total = segments.sumOf { it.amount }.toFloat().coerceAtLeast(1f)
    Canvas(modifier = modifier) {
        val strokeWidth = min(size.width, size.height) * 0.18f
        var startAngle = -90f
        segments.forEach { segment ->
            val sweep = (segment.amount.toFloat() / total) * 360f
            drawArc(
                color = segment.color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            startAngle += sweep
        }
    }
}

private fun buildCategorySpend(
    expenses: List<Expense>,
    categories: List<Category>
): List<CategorySpend> {
    if (expenses.isEmpty()) {
        return listOf(
            CategorySpend("Food & Dining", 845.2, Orange),
            CategorySpend("Transport", 620.4, Teal),
            CategorySpend("Shopping", 450.8, Purple),
            CategorySpend("Bills", 280.6, Green)
        )
    }

    val totals = expenses.groupBy { it.categoryId }.mapValues { entry ->
        entry.value.sumOf { it.amount }
    }

    val fallbackColors = listOf(PrimaryBlue, Orange, Teal, Purple, Green)
    return totals.entries.mapIndexed { index, entry ->
        val category = categories.firstOrNull { it.id == entry.key }
        CategorySpend(
            label = category?.name ?: "Category ${entry.key}",
            amount = entry.value,
            color = fallbackColors[index % fallbackColors.size]
        )
    }.sortedByDescending { it.amount }
}

private fun formatAmount(amount: Double): String {
    return "%,.2f".format(amount)
}

private data class CategorySpend(
    val label: String,
    val amount: Double,
    val color: Color
)
