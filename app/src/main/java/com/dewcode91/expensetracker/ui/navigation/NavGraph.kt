package com.dewcode91.expensetracker.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dewcode91.expensetracker.ui.screens.*
import com.dewcode91.expensetracker.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: MainViewModel = viewModel()
    val bottomItems = listOf(Screen.Dashboard, Screen.Expense, Screen.Settings)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = { TopAppBar(title = { Text("Expense Tracker") }) },
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { screen ->
                    val selected = currentRoute == screen.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            when (screen) {
                                Screen.Dashboard -> androidx.compose.material3.Icon(
                                    imageVector = Icons.Filled.Home,
                                    contentDescription = screen.title
                                )
                                Screen.Expense -> androidx.compose.material3.Icon(
                                    imageVector = Icons.Filled.AddCircle,
                                    contentDescription = screen.title
                                )
                                Screen.Settings -> androidx.compose.material3.Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = screen.title
                                )
                                else -> androidx.compose.material3.Icon(
                                    imageVector = Icons.Filled.Home,
                                    contentDescription = screen.title
                                )
                            }
                        },
                        label = { Text(screen.title) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(viewModel = viewModel)
            }
            composable(Screen.Expense.route) {
                ExpenseScreen(viewModel = viewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = viewModel)
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
                ChartsScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Export.route) {
                ExportScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
            }
            composable(Screen.Receipts.route) {
                ReceiptsScreen(onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}
