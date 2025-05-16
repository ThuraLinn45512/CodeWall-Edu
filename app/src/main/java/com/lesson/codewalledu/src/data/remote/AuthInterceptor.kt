package com.lesson.codewalledu.src.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private var authToken: String? = null) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authorizedRequest = originalRequest.newBuilder()
        authToken?.let {
            authorizedRequest.header("Authorization", "Bearer $it")
        }
        return chain.proceed(authorizedRequest.build())
    }

    fun setToken(token: String?) {
        authToken = token
    }
}