package com.dewcode91.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "recurring_expenses")
data class RecurringExpense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Double,
    val categoryId: Long,
    val startDate: LocalDate,
    val frequency: String // e.g., DAILY, WEEKLY, MONTHLY
)
