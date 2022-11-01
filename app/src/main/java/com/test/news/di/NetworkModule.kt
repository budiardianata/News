package com.test.news.di

import com.google.gson.GsonBuilder
import com.test.news.data.source.remote.NewsApi
import com.test.news.data.source.remote.interceptor.TokenInterceptor
import com.test.news.util.BaseUrl
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

object NetworkModule {

    @Provides
    @Singleton
    fun provideTokenInterceptor(): TokenInterceptor {
        return TokenInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenInterceptor: TokenInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return builder
            .addInterceptor(logging)
            .addInterceptor(tokenInterceptor)
            .build()
    }


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, @BaseUrl baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().create())
            )
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }
}