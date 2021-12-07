package com.church.ministry.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class BookContentResponse(
    val success: String,
    val messageAr: String,
    val messageEn: String,
    val data: BookContent
)


@Entity
data class BookContent(
    @PrimaryKey
    val id: Int,
    var fileNameDb: String,  // name of file saved in internal storage
    val customerId: Int,
    val bookId: Int,
    val bookTitleAr: String,
    val bookTitleEn: String,
    val bookPhoto: String,
    var bookFileBase64: String
)
