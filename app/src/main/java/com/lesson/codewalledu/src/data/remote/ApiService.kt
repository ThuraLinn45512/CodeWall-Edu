package com.lesson.codewalledu.src.data.remote


import com.lesson.codewalledu.src.data.models.auth.AuthResponse
import com.lesson.codewalledu.src.data.models.auth.User
import com.lesson.codewalledu.src.data.models.core.UserDataResponse
import com.lesson.codewalledu.src.data.models.core.explore.BlogsDataResponse
import com.lesson.codewalledu.src.data.models.core.explore.BooksDataResponse
import com.lesson.codewalledu.src.data.models.core.explore.CheatSheetsDataResponse
import com.lesson.codewalledu.src.data.models.core.explore.NewsDataResponse
import com.lesson.codewalledu.src.data.models.core.home.BannerDataResponse
import com.lesson.codewalledu.src.data.models.core.home.CoursesDataResponse
import com.lesson.codewalledu.src.data.models.core.home.EachCourseDataResponse
import com.lesson.codewalledu.src.data.models.core.home.TopicDataResponse
import com.lesson.codewalledu.src.data.models.core.home.enroll.EnrollDataResponse
import com.lesson.codewalledu.src.data.models.core.home.search.SearchDataResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    
    @FormUrlEncoded
    @POST("login")
    suspend fun login(@Field("email") email: String, @Field("password") password: String ):Response<AuthResponse>


    @FormUrlEncoded
    @POST("register")
    suspend fun register(@Field("name") name: String, @Field("email") email: String, @Field("password") password: String):Response<AuthResponse>


    @FormUrlEncoded
    @POST("verify-registration")
    suspend fun otpVerification(@Field("email") email: String, @Field("otp") otp: String):Response<AuthResponse>


    @FormUrlEncoded
    @POST("resend-verification-code")
    suspend fun resendVerificationCode(@Field("email") email: String):Response<AuthResponse>


    /**
     * Forget Password Section
     */

    @FormUrlEncoded
    @POST("forgot-password")
    suspend fun forgetPassword(@Field("email") email: String):Response<AuthResponse>

    @FormUrlEncoded
    @POST("verify-forget-password")
    suspend fun otpForgetPassword(@Field("email") email: String, @Field("otp") otp: String):Response<AuthResponse>

    @FormUrlEncoded
    @POST("resend-reset-code")
    suspend fun resendResetCode(@Field("email") email: String):Response<AuthResponse>

    @FormUrlEncoded
    @POST("reset-password")
    suspend fun resetPassword(@Field("email") email: String, @Field("password") password: String,@Field("token") token:String):Response<AuthResponse>


    /**
     *
     *Home Page Section
     */

    @GET("user")
    suspend fun getUser(@Header("Authorization") token: String): UserDataResponse

    @GET("user/search")
    suspend fun getSearchData(@Header("Authorization") token: String,@Query ("key") key:String) : SearchDataResponse

    @GET("banners")
    suspend fun getBanners(@Header("Authorization") token: String): BannerDataResponse

    @GET("tags")
    suspend fun getTopics(@Header("Authorization") token: String): TopicDataResponse

    @GET("courses")
    suspend fun getTopicCoursesData(@Header("Authorization") token: String,@Query("tag") slug: String): CoursesDataResponse

    @GET("courses?category=popular-courses")
    suspend fun getPopularCourses(@Header("Authorization") token: String): CoursesDataResponse

    @GET("courses/free")
    suspend fun getFreeCourses(@Header("Authorization") token: String): CoursesDataResponse

    @GET("courses/upcoming")
    suspend fun getUpcomingCourses(@Header("Authorization") token: String): CoursesDataResponse

    @GET("courses/{courseId}")
    suspend fun getEachCourseData(@Header("Authorization") token: String,@Path("courseId") courseId:Int) : EachCourseDataResponse

    @GET("user/enroll/{course}")
    suspend fun getEnrollData(@Header("Authorization") token: String,@Path("course") courseId:Int = 1) : EnrollDataResponse

    /**
     * Explore Page Section
     */

    @GET("blogs")
    suspend fun getBlogs(@Header("Authorization") token: String): BlogsDataResponse

    @GET("news")
    suspend fun getNews(@Header("Authorization") token: String): NewsDataResponse

    @GET("books")
    suspend fun getBooks(@Header("Authorization") token: String): BooksDataResponse

    @GET("cheat-sheets")
    suspend fun getCheatSheets(@Header("Authorization") token: String): CheatSheetsDataResponse



}