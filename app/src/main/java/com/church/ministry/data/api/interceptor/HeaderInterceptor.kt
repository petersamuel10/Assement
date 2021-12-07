package com.church.ministry.data.api.interceptor

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Base64.encodeToString
import androidx.core.content.pm.PackageInfoCompat
import com.church.ministry.util.Preferences
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeaderInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferences: Preferences
) :
    Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        var userToken = ""
        var version = ""
        var build = ""
      //  val credentialData = "Basic " + toBase64("XmkURAxPe2jXM1Lq" + ":" + "dRAtq7mmuaQsQvNN")
      //  if (preferences.contain(CodingKeys.ACCESS_TOKEN))
           // userToken = preferences.getString(CodingKeys.ACCESS_TOKEN)!!
            userToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjEiLCJuYmYiOjE2MzUwMDczNzQsImV4cCI6MTY0Mjk1NjE3NCwiaWF0IjoxNjM1MDA3Mzc0LCJpc3MiOiJuY2NhbC5nb3Yua3ciLCJhdWQiOiJuY2NhbC5nb3Yua3cifQ.mlC0kfWsR5ji6P8fQibbF0H6ZXFzueVi9_oAuXs_p0c"

        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo.versionName
            build = PackageInfoCompat.getLongVersionCode(pInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


        val request = original.newBuilder()
            .addHeader("Content-Type", "application/json")
            //.addHeader("Authorization", credentialData)
            .addHeader("token", userToken)
            .addHeader("device", "1")
            .addHeader("API-Version", "2.0")
            .addHeader("lang", "1")
            .addHeader("debug", "0")
            .addHeader("version", version)
            .addHeader("build", build)


        return chain.proceed(request.build())
    }


    fun toBase64(string: String): String {

        return encodeToString(string.toByteArray(charset("UTF-8")), Base64.NO_WRAP)
    }
}