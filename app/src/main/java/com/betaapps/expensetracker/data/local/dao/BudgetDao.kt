package com.betaapps.expensetracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.betaapps.expensetracker.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budget WHERE id = 1 LIMIT 1")
    fun observeBudget(): Flow<BudgetEntity?>

    @Query("SELECT * FROM budget WHERE id = 1 LIMIT 1")
    suspend fun getBudget(): BudgetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBudget(budget: BudgetEntity)
}
