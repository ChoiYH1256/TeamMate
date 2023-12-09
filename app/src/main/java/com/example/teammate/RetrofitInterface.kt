package com.example.teammate

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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
interface PostService{
    @GET("api/post/get")
    fun getAllPosts(): Call<List<Post>>

    @POST("api/post/create")
    fun createPost(@Body postData: PostCreate): Call<PostResponse>

    @PUT("api/post/edit")
    fun editPost(@Path("postId") postId: String, @Body updatedPost: Post): Call<PostResponse>

    @DELETE("api/post/delete")
    fun deletePost(@Path("postId") postId: String): Call<PostResponse>
}


data class UserResponse(val message: String)

data class PostResponse(
    val postId: String,
    val message: String
)
data class UserLogin(val email: String, val password: String)
data class UserSignup(val email: Any, val password: Any, val major: Any, val grade: Any, val region: Any)
data class PostCreate(val uid: Any, val title: Any, val teamNumber: Any, val content: Any, val category: Any, val hashtags: Any)


////////////////////// 데이터 클래스 /////////////////////
data class Post(
    val uid: String,
    val title: String,
    val teamNumber: Any,
    val content: String,
    val category: String,
    val hashtags: Any,
    val postId: String? = null // 서버에서 생성된 ID를 저장하는 필드
)