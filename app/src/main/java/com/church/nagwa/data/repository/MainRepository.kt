package com.church.nagwa.data.repository

import com.church.nagwa.data.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getItemList() = apiHelper.getItemList()
}