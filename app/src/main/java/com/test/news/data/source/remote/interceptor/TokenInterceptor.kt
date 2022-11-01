package com.test.news.data.source.remote.interceptor

import com.test.news.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = BuildConfig.NEWS_API_KEY
        requestBuilder.addHeader("Authorization", "Bearer $token")
        return chain.proceed(requestBuilder.build())
    }
}