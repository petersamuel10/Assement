package com.church.ministry.data.repository

import com.church.ministry.data.api.ApiHelper
import com.church.ministry.data.model.Book
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun login(userName:String, password:String) = apiHelper.login(userName, password)
    suspend fun logout() = apiHelper.logout()
    suspend fun getUserInfo() = apiHelper.getUserInfo()
    suspend fun checkNewBooks(lastId:Int) = apiHelper.checkNewBooks(lastId)
    suspend fun getBookList(online: Boolean, lastId: Int): List<Book> = apiHelper.getBooksList(online, lastId)
    suspend fun getSearchBookList(searchTxt:String) = apiHelper.getSearchBooksList(searchTxt)
    suspend fun getBookContent(bookId:Int) = apiHelper.getBookContent(bookId)
}