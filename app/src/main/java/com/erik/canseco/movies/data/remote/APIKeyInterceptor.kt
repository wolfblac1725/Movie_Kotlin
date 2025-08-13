package com.erik.canseco.movies.data.remote

import com.erik.canseco.movies.data.remote.MovieApi.Companion.API_KEY
import com.erik.canseco.movies.data.remote.MovieApi.Companion.LANGUAGE
import okhttp3.Interceptor
import okhttp3.Response

class APIKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("language", LANGUAGE)
            .build()
        val currentRequest = originalRequest.newBuilder()
        val newRequest = currentRequest.url(newUrl).build()
        return chain.proceed(newRequest)

    }

}