package com.betaapps.expensetracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.betaapps.expensetracker.data.local.dao.ExpenseDao
import com.betaapps.expensetracker.data.local.entity.ExpenseEntity

@Database(entities = [ExpenseEntity::class], version = 2)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
}
