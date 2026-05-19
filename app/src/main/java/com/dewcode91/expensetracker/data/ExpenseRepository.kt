package com.dewcode91.expensetracker.data

import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
    private val recurringExpenseDao: RecurringExpenseDao,
    private val budgetDao: BudgetDao
) {
    fun observeExpenses(): Flow<List<Expense>> = expenseDao.observeAll()
    fun observeCategories(): Flow<List<Category>> = categoryDao.observeAll()
    fun observeRecurring(): Flow<List<RecurringExpense>> = recurringExpenseDao.observeAll()
    fun observeBudgets(): Flow<List<Budget>> = budgetDao.observeAll()

    suspend fun upsertExpense(expense: Expense) = expenseDao.upsert(expense)
    suspend fun deleteExpense(id: Long) = expenseDao.delete(id)
    suspend fun upsertCategory(category: Category) = categoryDao.upsert(category)
    suspend fun upsertRecurring(recurringExpense: RecurringExpense) = recurringExpenseDao.upsert(recurringExpense)
    suspend fun upsertBudget(budget: Budget) = budgetDao.upsert(budget)
}
