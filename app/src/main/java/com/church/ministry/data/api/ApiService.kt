package com.church.ministry.data.api

import com.church.ministry.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("account/login")
    suspend fun login(@Body body: HashMap<String,String>): UserResponse

    @GET("customer/books/count")
    suspend fun checkNewBooks(@Query("lastId") lastId: Int): CheckNewBookResponse

    @GET("customer/books/list")
    suspend fun getBooks(@Query("lastId") lastId: Int): BookResponse

    @GET("customer/books/book")
    suspend fun getBookContent(@Query("id") bookId: Int): BookContentResponse
}