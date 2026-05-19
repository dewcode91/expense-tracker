package com.dewcode91.expensetracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Expense::class, Category::class, RecurringExpense::class, Budget::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
    abstract fun recurringExpenseDao(): RecurringExpenseDao
    abstract fun budgetDao(): BudgetDao
}
