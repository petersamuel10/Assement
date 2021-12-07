package com.church.ministry.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.church.ministry.data.model.Book
import com.church.ministry.data.model.BookContent
import com.church.ministry.data.model.User

@Database(
    entities = [User::class, Book::class, BookContent::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun queueDao(): BookDao

    companion object {
        val DATABASE_NAME: String = "books_db"
    }
}