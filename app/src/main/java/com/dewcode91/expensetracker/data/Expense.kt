package com.dewcode91.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Double,
    val categoryId: Long,
    val date: LocalDate,
    val note: String? = null,
    val receiptUri: String? = null,
    val isRecurring: Boolean = false,
    val recurringId: Long? = null
)
