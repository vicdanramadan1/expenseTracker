package com.betaapps.expensetracker.di

import android.content.Context
import androidx.room.Room
import com.betaapps.expensetracker.data.local.dao.ExpenseDao
import com.betaapps.expensetracker.data.local.database.ExpenseDatabase
import com.betaapps.expensetracker.data.repository.ExpenseRepositoryImpl
import com.betaapps.expensetracker.domain.repository.ExpenseRepository
import com.betaapps.expensetracker.domain.usecases.AddExpensUsecase
import com.betaapps.expensetracker.domain.usecases.DeleteExpenseUsecase
import com.betaapps.expensetracker.domain.usecases.GetExpensesUsecase
import com.betaapps.expensetracker.presentation.home.HomeScreenViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            "expense_database"
        ).build()
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
    fun provideAddExpenseUseCase(repository: ExpenseRepository): AddExpensUsecase {
        return AddExpensUsecase(repository)
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
    @Singleton
    fun provideHomeScreenViewModel(
        addExpensUsecase: AddExpensUsecase,
        getExpensesUsecase: GetExpensesUsecase
    ) : HomeScreenViewModel {
        return HomeScreenViewModel(addExpensUsecase = addExpensUsecase , getExpensesUsecase = getExpensesUsecase)
    }
}
