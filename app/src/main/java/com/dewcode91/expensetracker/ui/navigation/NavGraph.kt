package com.dewcode91.expensetracker.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dewcode91.expensetracker.ui.screens.*
import com.dewcode91.expensetracker.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: MainViewModel = viewModel()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Expense Tracker") }) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(viewModel = viewModel, onNavigate = { navController.navigate(it.route) })
            }
            composable(Screen.Categories.route) {
                CategoriesScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Recurring.route) {
                RecurringScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Budgets.route) {
                BudgetsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Charts.route) {
                ChartsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Export.route) {
                ExportScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Receipts.route) {
                ReceiptsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}
