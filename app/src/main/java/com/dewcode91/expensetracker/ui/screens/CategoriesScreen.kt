package com.dewcode91.expensetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dewcode91.expensetracker.ui.viewmodel.MainViewModel

@Composable
fun CategoriesScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val categories by viewModel.categories
    var name by remember { mutableStateOf("") }
    var colorHex by remember { mutableStateOf("#FF8A65") }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Categories (${categories.size})")
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = colorHex,
            onValueChange = { colorHex = it },
            label = { Text("Color hex") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            if (name.isNotBlank()) {
                viewModel.addCategory(name.trim(), colorHex.ifBlank { "#FF8A65" })
                name = ""
            }
        }) {
            Text("Save category")
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { category ->
                Text(text = "${category.name} (${category.colorHex})")
            }
        }
        Button(onClick = onNavigateBack) { Text("Back") }
    }
}
