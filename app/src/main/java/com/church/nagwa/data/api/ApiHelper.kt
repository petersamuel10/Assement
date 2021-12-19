package com.church.nagwa.data.api

import com.church.nagwa.data.model.*

interface ApiHelper {

    suspend fun getItemList(): List<ItemInfo>
}