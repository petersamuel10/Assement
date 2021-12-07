package com.church.ministry.di

import androidx.room.Room
import com.church.ministry.base.App
import com.church.ministry.data.cache.AppDatabase
import com.church.ministry.data.cache.BookDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideDb(app: App): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideQueueDao(db: AppDatabase): BookDao {
        return db.queueDao()
    }

}