package com.church.ministry.data.model

data class ItemInfo(
    val id: Int = 0,
    val type: String = "",
    val url: String = "",
    val name: String = "",
    var isLogged: Boolean = false
)
