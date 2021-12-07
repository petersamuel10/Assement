package com.church.ministry.di

import android.content.Context
import com.church.ministry.base.App
import com.church.ministry.BuildConfig
import com.church.ministry.data.api.ApiHelper
import com.church.ministry.data.api.ApiHelperImpl
import com.church.ministry.data.api.ApiService
import com.church.ministry.util.Preferences
import com.church.ministry.data.api.interceptor.ErrorInterceptor
import com.church.ministry.data.api.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context): App {
        return context as App
    }

    @Provides
    fun provideBaseUrl() = "http://api.nccal.vavisa-kw.com/"

    @Provides
    @Singleton
    fun providePreference(context: App) =
        Preferences(context)

    @Provides
    @Singleton
    fun provideInterceptor(context: App) =
        HeaderInterceptor(context, preferences = Preferences(context))

    @Provides
    @Singleton
    fun provideOkHttpClient(headerInterceptor: HeaderInterceptor, context: App) =
        if (BuildConfig.DEBUG) { // debug ON
            val logger = HttpLoggingInterceptor()
            logger.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(ErrorInterceptor(context = context))
                .addInterceptor(headerInterceptor)
                .build()
        } else // debug OFF
            OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(ErrorInterceptor(context))
                .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient,baseURL: String) =
        Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

}