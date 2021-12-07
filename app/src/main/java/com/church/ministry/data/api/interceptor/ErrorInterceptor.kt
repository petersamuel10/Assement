package com.church.ministry.data.api.interceptor

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject


class ErrorInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)

        when (response.code) {
            400 -> {
                //Show Bad Request Error Message
//                Toast.makeText(context, "Bad Request Error", Toast.LENGTH_LONG).show()
            }
            401 -> {
                //Show UnauthorizedError Message
                context.run {
                    Toast.makeText(context, "Unauthorized Error", Toast.LENGTH_LONG).show()
                }

            }

            403 -> {
                //Show Forbidden Message
                Toast.makeText(context, "Forbidden", Toast.LENGTH_LONG).show()
            }

            404 -> {

//                Handler().postDelayed(Runnable {
//                    AlertDialog.Builder(context)
//                        .setTitle("")
//                        .setMessage("fghtr")
//                        .setCancelable(false)
//                        .setPositiveButton(
//                            "Ok"
//                        ) { dialogInterface, i -> dialogInterface.dismiss() }
//                        .show()
//                }, 100)




                //Show NotFound Message
//                context.run {
//                    Toast.makeText(context, "Not Found", Toast.LENGTH_LONG).show()
//                }
            }

            // ... and so on

        }
        return response
    }
}