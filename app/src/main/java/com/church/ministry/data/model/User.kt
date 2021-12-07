package com.church.ministry.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class UserResponse(
    val success: String,
    val messageAr: String,
    val messageEn: String,
    val data: User
)

@Entity
data class User(
    @PrimaryKey
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val token: String = "",
    var isLogged: Boolean = false
)
