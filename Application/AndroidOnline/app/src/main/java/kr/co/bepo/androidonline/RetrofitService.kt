package kr.co.bepo.androidonline

import kr.co.bepo.androidonline.Apple.Song
import kr.co.bepo.androidonline.Mytube.Mytube
import kr.co.bepo.androidonline.Outstagram.Post
import kr.co.bepo.androidonline.Outstagram.User
import kr.co.bepo.androidonline.essential_android.PersonForServer
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("json/students/")
    fun getStudentList(): Call<ArrayList<PersonForServer>>

    @POST("json/students/")
    fun createStudent(
        @Body params: HashMap<String, Any>
    ): Call<PersonForServer>

    @POST("json/students/")
    fun createStudentEasy(
        @Body person: PersonForServer
    ): Call<PersonForServer>

    @POST("user/signup/")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String,
        @Field("password1") password1: String,
        @Field("password2") password2: String
    ): Call<User>

    @POST("user/login/")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Call<User>

    @GET("instagram/post/list/all/")
    fun getAllPosts(): Call<ArrayList<Post>>

    @Multipart
    @POST("instagram/post/")
    fun uploadPost(
        @Part image: MultipartBody.Part,
        @Part("content") requestBody: RequestBody
    ): Call<Post>

    @GET("instagram/post/list/")
    fun getUserPostList(): Call<ArrayList<Post>>

    @GET("youtube/list/")
    fun getYoutubeList(): Call<ArrayList<Mytube>>

    @GET("melon/list/")
    fun getSongList(): Call<ArrayList<Song>>
}