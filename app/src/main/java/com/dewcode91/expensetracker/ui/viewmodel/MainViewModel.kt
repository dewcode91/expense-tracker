package com.dewcode91.expensetracker.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dewcode91.expensetracker.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = DatabaseProvider.get(application)
    private val repository = ExpenseRepository(
        db.expenseDao(),
        db.categoryDao(),
        db.recurringExpenseDao(),
        db.budgetDao()
    )

    val expenses: StateFlow<List<Expense>> = repository.observeExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val categories: StateFlow<List<Category>> = repository.observeCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val recurring: StateFlow<List<RecurringExpense>> = repository.observeRecurring()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val budgets: StateFlow<List<Budget>> = repository.observeBudgets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addExpense(
        title: String,
        amount: Double,
        categoryId: Long,
        date: LocalDate,
        note: String? = null,
        receiptUri: String? = null,
        isRecurring: Boolean = false,
        recurringId: Long? = null
    ) {
        viewModelScope.launch {
            repository.upsertExpense(
                Expense(
                    title = title,
                    amount = amount,
                    categoryId = categoryId,
                    date = date,
                    note = note,
                    receiptUri = receiptUri,
                    isRecurring = isRecurring,
                    recurringId = recurringId
                )
            )
        }
    }

    fun addCategory(name: String, colorHex: String) {
        viewModelScope.launch {
            repository.upsertCategory(Category(name = name, colorHex = colorHex))
        }
    }

    fun addRecurring(title: String, amount: Double, categoryId: Long, frequency: String) {
        viewModelScope.launch {
            repository.upsertRecurring(
                RecurringExpense(
                    title = title,
                    amount = amount,
                    categoryId = categoryId,
                    startDate = LocalDate.now(),
                    frequency = frequency
                )
            )
        }
    }

    fun addBudget(categoryId: Long, monthlyLimit: Double) {
        viewModelScope.launch {
            repository.upsertBudget(Budget(categoryId = categoryId, monthlyLimit = monthlyLimit))
        }
    }
}
