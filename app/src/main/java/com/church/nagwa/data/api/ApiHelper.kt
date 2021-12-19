package com.church.ministry.data.api

import com.church.ministry.data.model.*

interface ApiHelper {

    suspend fun getItemList(): List<ItemInfo>
}