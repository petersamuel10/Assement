package com.church.ministry.data.api

import com.church.ministry.base.App
import com.church.ministry.data.cache.BookDao
import com.church.ministry.data.model.Book
import com.church.ministry.data.model.BookContent
import com.church.ministry.data.model.CheckNewBookResponse
import com.church.ministry.data.model.User
import com.church.ministry.util.NetworkHelper
import com.church.ministry.util.StorageUtils
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val apiService: ApiService,
    private val bookDao: BookDao,
    private val context: App
) : ApiHelper {

    override suspend fun login(userName: String, password: String): User {
        val map = HashMap<String, String>()
        map["Email"] = userName
        map["Password"] = password

        val result = apiService.login(map)

        if (result.success == "true") {
            result.data.isLogged = true

            // no user registered
            if (bookDao.checkUserExist() == 0) {
                bookDao.insertUser(result.data)
            } else {
                val previousUser = bookDao.getUserInfo()!!

                if (result.data.id != previousUser.id) {
                    StorageUtils.deleteBookFiles(context)
                    bookDao.deleteUser()
                    bookDao.deleteAllBooks()
                    bookDao.insertUser(result.data)
                } else
                    bookDao.logout(true) // to register user as login in
            }
        }

        return bookDao.getUserInfo()!!
    }

    override suspend fun logout() {
        bookDao.logout(false)
    }

    override suspend fun getUserInfo(): User {
        return bookDao.getUserInfo() ?: User()
    }

    override suspend fun checkNewBooks(lastId: Int): CheckNewBookResponse {
        return apiService.checkNewBooks(lastId)
    }

    override suspend fun getBooksList(online: Boolean, lastId: Int): List<Book> {

        if (networkHelper.isNetworkConnected() && online) {
            val result = apiService.getBooks(lastId)
            if (result.success == "true")
                bookDao.insertAllBooks(result.data)
            return result.data
        } else
            return bookDao.getAllBooks()
    }

    override suspend fun getSearchBooksList(searchTxt: String): List<Book> {
        val rr = bookDao.getAllSearchedBooks(searchTxt)
        return rr
    }

    override suspend fun getBookContent(bookId: Int): BookContent {

        val bb = bookDao.checkExistBook(bookId)
        if (bb)
            return bookDao.getBookContent(bookId)

        if (networkHelper.isNetworkConnected()) {
            val result = apiService.getBookContent(bookId)
            if (result.success == "true") {
                result.data.fileNameDb = System.currentTimeMillis().toString()
                StorageUtils.setTextInStorage(
                    context,
                    result.data.fileNameDb,
                    result.data.bookFileBase64
                )
                result.data.bookFileBase64 = ""
                bookDao.insertBookContent(result.data)
                bookDao.setDownloadStatus(result.data.bookId, true)
            }
        }

        return bookDao.getBookContent(bookId)
    }

}