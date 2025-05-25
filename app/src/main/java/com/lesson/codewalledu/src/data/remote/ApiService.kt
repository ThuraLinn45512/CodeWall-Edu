package com.lesson.codewalledu.src.data.remote


import com.lesson.codewalledu.src.data.models.auth.AuthResponse
import com.lesson.codewalledu.src.data.models.core.explore.BlogsDataResponse
import com.lesson.codewalledu.src.data.models.core.explore.BooksDataResponse
import com.lesson.codewalledu.src.data.models.core.explore.CheatSheetsDataResponse
import com.lesson.codewalledu.src.data.models.core.explore.NewsDataResponse
import com.lesson.codewalledu.src.data.models.core.home.CoursesDataResponse
import com.lesson.codewalledu.src.data.models.core.home.TopicDataResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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

//    @GET("/api/v1/topic")
//    suspend fun getTopic(): TopicDataResponse
//
//    @GET("/api/v1/popular_courses")
//    suspend fun getPopularCourses(): CoursesDataResponse
//
//    @GET("/api/v1/free_courses")
//    suspend fun getFreeCourses():CoursesDataResponse
//
//    @GET("/api/v1/upcoming_courses")
//    suspend fun getUpcomingCourses(): CoursesDataResponse


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