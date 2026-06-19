package com.betaapps.expensetracker.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.betaapps.expensetracker.data.local.dao.ExpenseDao
import com.betaapps.expensetracker.data.local.database.ExpenseDatabase
import com.betaapps.expensetracker.data.repository.ExpenseRepositoryImpl
import com.betaapps.expensetracker.domain.repository.ExpenseRepository
import com.betaapps.expensetracker.domain.usecases.AddExpenseUsecase
import com.betaapps.expensetracker.domain.usecases.DeleteExpenseUsecase
import com.betaapps.expensetracker.domain.usecases.GetExpenseHistoryUsecase
import com.betaapps.expensetracker.domain.usecases.GetExpenseUsecase
import com.betaapps.expensetracker.domain.usecases.GetExpensesUsecase
import com.betaapps.expensetracker.domain.usecases.SearchExpensesUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE expenses ADD COLUMN isPaid INTEGER NOT NULL DEFAULT 1")
        }
    }

    @Provides
    @Singleton
    fun provideExpenseDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            "expense_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao {
        return database.expenseDao()
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(expenseDao: ExpenseDao): ExpenseRepository {
        return ExpenseRepositoryImpl(expenseDao)
    }

    @Provides
    fun provideAddExpenseUseCase(repository: ExpenseRepository): AddExpenseUsecase {
        return AddExpenseUsecase(repository)
    }

    @Provides
    fun provideDeleteExpenseUseCase(repository: ExpenseRepository): DeleteExpenseUsecase {
        return DeleteExpenseUsecase(repository)
    }

    @Provides
    fun provideGetExpensesUseCase(repository: ExpenseRepository): GetExpensesUsecase {
        return GetExpensesUsecase(repository)
    }

    @Provides
    fun provideGetExpenseHistoryUseCase(repository: ExpenseRepository): GetExpenseHistoryUsecase {
        return GetExpenseHistoryUsecase(repository)
    }

    @Provides
    fun provideGetExpenseUseCase(repository: ExpenseRepository): GetExpenseUsecase {
        return GetExpenseUsecase(repository)
    }

    @Provides
    fun provideSearchExpensesUseCase(repository: ExpenseRepository): SearchExpensesUsecase {
        return SearchExpensesUsecase(repository)
    }
}
