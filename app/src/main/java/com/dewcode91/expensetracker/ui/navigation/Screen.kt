package com.dewcode91.expensetracker.ui.navigation

sealed class Screen(val route: String, val title: String) {
    data object Dashboard : Screen("dashboard", "Dashboard")
    data object Categories : Screen("categories", "Categories")
    data object Recurring : Screen("recurring", "Recurring")
    data object Budgets : Screen("budgets", "Budgets")
    data object Charts : Screen("charts", "Charts")
    data object Export : Screen("export", "Export")
    data object Receipts : Screen("receipts", "Receipts")
}
