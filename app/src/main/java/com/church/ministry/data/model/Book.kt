package com.church.ministry.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class BookResponse(
    val success: String,
    val messageAr: String,
    val messageEn: String,
    val data: List<Book>
)

@Entity
data class Book(
    @PrimaryKey
    val id: Int,
    val customerId: Int,
    val bookId: Int,
    val bookTitleAr: String,
    val bookTitleEn: String,
    val bookPhoto: String,
    var isDownload: Boolean = false
)


data class CheckNewBookResponse(
    val success: String,
    val messageAr: String,
    val messageEn: String,
    val data: Int
)



