package com.motodo.todo.di

import android.content.Context
import androidx.room.Room
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.motodo.todo.data.repositories.BaseRepositoryImpl
import com.motodo.todo.data.source.TodoDataBase
import com.motodo.todo.domain.useCases.DeleteTodoUseCase
import com.motodo.todo.domain.useCases.GetDateToDosUseCase
import com.motodo.todo.domain.useCases.GetPreviousTodosUseCase
import com.motodo.todo.domain.useCases.InsertTodoUseCase
import com.motodo.todo.domain.useCases.TodoUseCases
import com.motodo.todo.domain.useCases.UpdateTodoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun provideTodoDatabase(
       @ApplicationContext appContext : Context
    ): TodoDataBase {
        return Room.databaseBuilder(
            appContext,
            TodoDataBase::class.java,
            "TodoDB"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: TodoDataBase): BaseRepositoryImpl {
        return BaseRepositoryImpl(db.myDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: BaseRepositoryImpl): TodoUseCases {
        return TodoUseCases(
            DeleteTodoUseCase(repository),
            GetDateToDosUseCase(repository),
            InsertTodoUseCase(repository),
            GetPreviousTodosUseCase(repository),
            UpdateTodoUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext appContext: Context
    ): DataStoreImpl {
        return DataStoreImpl(appContext)
    }

}