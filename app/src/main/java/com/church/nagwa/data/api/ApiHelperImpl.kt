package com.church.nagwa.data.api

import android.util.Log
import com.church.nagwa.base.App
import com.church.nagwa.data.model.ItemInfo
import com.church.nagwa.util.NetworkHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val context: App
) : ApiHelper {

    override suspend fun getItemList(): List<ItemInfo> {

        val resultJson =
            context.assets.open("data.json").bufferedReader().use { reader -> reader.readText() }

        val listFilesType = object : TypeToken<List<ItemInfo>>() {}.type

        val gson = Gson()
        val fileList = gson.fromJson<List<ItemInfo>>(resultJson, listFilesType)

        Log.d("result1", fileList.toString())

        return fileList
    }
}