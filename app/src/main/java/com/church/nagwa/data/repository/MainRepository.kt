package com.church.ministry.data.repository

import com.church.ministry.data.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getItemList() = apiHelper.getItemList()
}