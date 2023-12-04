package com.example.teammate

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/login")
    fun loginUser(@Body userData: UserLogin): Call<UserResponse>
}

data class UserLogin(val email: String, val password: String)
data class UserResponse(val message: String)
