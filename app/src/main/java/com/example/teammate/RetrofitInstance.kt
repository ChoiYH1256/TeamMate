package com.example.teammate

import androidx.annotation.RestrictTo
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/" // 서버 URL로 변경하세요

    private val gson = GsonBuilder().setLenient().create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }



    val postService: PostService by lazy{
        retrofit.create(PostService::class.java)
    }
}