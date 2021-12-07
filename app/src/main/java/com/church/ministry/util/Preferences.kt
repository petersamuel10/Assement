package com.church.ministry.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(@ApplicationContext private val context: Context) {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "ministry"
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    private val editor = sharedPref.edit()


    fun contain(key: CodingKeys) = sharedPref.contains(key.key)

    fun saveString(key: CodingKeys, string: String) {
        editor.putString(key.key, string)
        editor.apply()
    }

    fun getString(key: CodingKeys) = sharedPref.getString(key.key, "")

    fun removeString(key: CodingKeys) {
        editor.remove(key.key)
        editor.apply()
    }
}