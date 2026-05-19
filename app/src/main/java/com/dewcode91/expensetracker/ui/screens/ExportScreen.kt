package com.dewcode91.expensetracker.ui.screens

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dewcode91.expensetracker.ui.viewmodel.MainViewModel
import java.io.OutputStreamWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ExportScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val expenses by viewModel.expenses.collectAsState()

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Export CSV")
        Button(onClick = { exportCsv(context, expenses) }) {
            Text("Export to Downloads")
        }
        Button(onClick = onNavigateBack) { Text("Back") }
    }
}

private fun exportCsv(context: Context, expenses: List<com.dewcode91.expensetracker.data.Expense>) {
    val filename = "expenses_${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))}.csv"
    val resolver = context.contentResolver
    val values = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, filename)
        put(MediaStore.Downloads.MIME_TYPE, "text/csv")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }
    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values) ?: return
    resolver.openOutputStream(uri)?.use { outputStream ->
        OutputStreamWriter(outputStream).use { writer ->
            writer.appendLine("id,title,amount,categoryId,date,note,receiptUri,isRecurring,recurringId")
            expenses.forEach { expense ->
                writer.appendLine(
                    listOf(
                        expense.id,
                        expense.title,
                        expense.amount,
                        expense.categoryId,
                        expense.date,
                        expense.note ?: "",
                        expense.receiptUri ?: "",
                        expense.isRecurring,
                        expense.recurringId ?: ""
                    ).joinToString(",")
                )
            }
        }
    }
}
