package com.church.ministry.data.api

import com.church.ministry.data.model.*

interface ApiHelper {

    suspend fun login(userName:String, password:String): User
    suspend fun logout()
    suspend fun getUserInfo(): User
    suspend fun checkNewBooks(lastId: Int): CheckNewBookResponse
    suspend fun getBooksList(online: Boolean, lastId: Int): List<Book>
    suspend fun getSearchBooksList(searchTxt:String): List<Book>
    suspend fun getBookContent(bookId:Int): BookContent

}