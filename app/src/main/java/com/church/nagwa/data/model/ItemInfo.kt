package com.church.nagwa.data.model

data class ItemInfo(
    val id: Int = 0,
    val type: String = "",
    val url: String = "",
    val name: String = "",
    var isDown: Boolean = false
)
