package com.example.teammate

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

//로그인 인터페이스
interface AuthService {
    @POST("api/login")
    fun loginUser(@Body userData: UserLogin): Call<UserResponse>
}

//회원가입 인터페이스
interface RegisterService{

    @POST("api/signUp")
    fun signupUser(@Body userData: UserSignup): Call<UserResponse>
}

//게시글 생성 인터페이스
interface CreatePostService{
    @POST("api/post/create")
    fun createPost(@Body postData: PostCreate): Call<PostResponse>
}

data class UserResponse(val message: String)

data class PostResponse(
    val postId: String,
    val message: String
)
data class UserLogin(val email: String, val password: String)
data class UserSignup(val email: Any, val password: Any, val major: Any, val grade: Any, val region: Any)
data class PostCreate(val uid: Any, val title: Any, val teamNumber: Any, val content: Any, val category: Any, val hashtags: Any)
