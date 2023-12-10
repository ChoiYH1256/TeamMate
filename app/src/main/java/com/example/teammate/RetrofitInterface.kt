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

    @POST("api/signUp")
    fun signupUser(@Body userData: UserSignup): Call<UserResponse>
    @POST("api/login")
    fun loginUser(@Body userData: UserLogin): Call<UserResponse>
    @POST("api/createProfile")
    fun createProfile(@Body profileData: UserProfile): Call<GenericResponse>
    @POST("api/editProfile")
    fun editProfile(@Body profileData: UserProfile): Call<GenericResponse>
    @GET("api/getProfile/{uid}")
    fun getProfile(@Path("uid") uid: String): Call<UserProfile>
    @GET("api/getProfileByCategory/{category}/{value}")
    fun getProfileByCategory(@Path("category") category: String, @Path("value") value: String): Call<List<UserProfile>>

}

//회원가입 인터페이스

//게시글 생성 인터페이스
interface PostService{

    //모든 post 불러오기
    @GET("api/post/getAllPosts")
    fun getAllPosts(): Call<List<Post>>

    //특정 post 불러오기
    @GET("api/post/get/{postId}")
    fun getPost(@Path("postId") postId: String): Call<Post>

    @POST("api/post/create")
    fun createPost(@Body postData: PostCreate): Call<PostResponse>

    @POST("api/post/edit")
    fun editPost(@Path("postId") postId: String, @Body updatedPost: Post): Call<PostResponse>

    @DELETE("api/post/delete")
    fun deletePost(@Path("postId") postId: String): Call<PostResponse>

    @GET("api/post/getPostApplications/{postId}")
    fun getPostApplications(@Path("postId") postId: String): Call<List<Application>>
}

interface ChatService {
    @POST("api/chatRooms")
    fun createChatRoom(@Body users: CreateChatRoomRequest): Call<CreateChatRoomResponse>

    @POST("api/sendMessage")
    fun sendMessage(@Path("chatRoomId") chatRoomId: String, @Body message: SendMessageRequest): Call<Void>

    @GET("api/getChatRoomMessage/{chatRoomId}")
    fun getChatRoomMessages(@Path("chatRoomId") chatRoomId: String): Call<GetChatRoomMessagesResponse>
}




data class UserResponse(val uid: String, val message: String)
data class PostResponse(val postId: String, val message: String)
data class UserLogin(val email: String, val password: String)
data class UserSignup(val email: Any, val password: Any, val major: Any, val grade: Any, val region: Any)
data class UserProfile(val uid: String, val name: String, val birth: String, val phoneNumber: String, val university: String, val experience: String, val major: String, val grade: String, val region: String)
data class GenericResponse(val message: String)
data class PostCreate(val uid: String, val title: String, val teamNumber: Any, val content: String, val major: String)

/////////////////////채팅 관련 클래스 ////////////////////
data class CreateChatRoomRequest(
    val user1: String,
    val user2: String
)

data class CreateChatRoomResponse(
    val chatRoomId: String
)

data class SendMessageRequest(
    val chatRoomId: String,
    val sender: String,
    val message: String
)

data class GetChatRoomMessagesResponse(
    val messages: List<ChatMessage>
)

data class ChatMessage(
    val sender: String,
    val message: String,
    val timestamp: String // timestamp는 일반적으로 서버에서 Date 객체로 반환되며, 클라이언트 측에서 적절하게 포맷팅할 수 있습니다.
)



////////////////////// 데이터 클래스 /////////////////////
data class Post(
    val uid: String,
    val title: String,
    val teamNumber: Any,
    val content: String,
    val major: String,
    val postId: String? = null // 서버에서 생성된 ID를 저장하는 필드
)

data class Application(
    val postId: String, // 지원한 게시물 ID
    val writerId: String, // 지원자의 사용자 ID
    val applicantid: String,
    val selfIntroduction: String,
    val resume: String
    // 기타 필요한 필드 추가
)