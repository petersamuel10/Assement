package com.church.ministry.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.church.ministry.data.model.Book
import com.church.ministry.data.model.BookContent
import com.church.ministry.data.model.User


@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT COUNT(*) FROM user")
    suspend fun checkUserExist(): Int

    @Query("SELECT * FROM user")
    suspend fun getUserInfo(): User?

    @Query("DELETE FROM user")
    suspend fun deleteUser()

    @Query("UPDATE user SET isLogged = :isLogged")
    suspend fun logout(isLogged: Boolean)

    @Query("DELETE FROM book")
    suspend fun deleteAllBooks()

    @Query("DELETE FROM BookContent")
    suspend fun deleteBookContent()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBooks(booksList: List<Book>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookContent(book: BookContent)

    @Query("SELECT * FROM book")
    suspend fun getAllBooks(): List<Book>

    @Query("SELECT * FROM book WHERE bookTitleEn LIKE :searchTxt")
    suspend fun getAllSearchedBooks(searchTxt: String): List<Book>

    @Query("SELECT EXISTS(SELECT * FROM BookContent WHERE bookId =:bookId)")
    suspend fun checkExistBook(bookId: Int): Boolean

    @Query("UPDATE book SET isDownload = :downStatus WHERE bookId = :bookId")
    suspend fun setDownloadStatus(bookId: Int, downStatus: Boolean)

    @Query("SELECT * FROM BookContent WHERE bookId =:bookId")
    suspend fun getBookContent(bookId: Int): BookContent


//
//    @Query("SELECT * FROM queue WHERE isInside = 1 AND (status = 'Rest_Queued' OR status = 'Queued')")
//    suspend fun getInsideQueue(): List<Queue>


}