package com.dewcode91.expensetracker.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var instance: ExpenseDatabase? = null

    fun get(context: Context): ExpenseDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                ExpenseDatabase::class.java,
                "expense_tracker.db"
            ).build().also { instance = it }
        }
    }
}
