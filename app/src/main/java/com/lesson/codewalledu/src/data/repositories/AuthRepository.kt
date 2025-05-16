package com.lesson.codewalledu.src.data.repositories

import com.lesson.codewalledu.src.data.models.auth.AuthResponse
import com.lesson.codewalledu.src.data.models.core.home.DataResponse
import com.lesson.codewalledu.src.data.models.core.home.CoursesDataResponse
import com.lesson.codewalledu.src.data.models.core.home.TopicDataResponse
import com.lesson.codewalledu.src.data.remote.ApiService
import com.lesson.codewalledu.src.data.remote.RetrofitInstance.api
import com.lesson.codewalledu.src.utils.Resource
import com.lesson.codewalledu.src.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response


class AuthRepository(private val apiService: ApiService,private val tokenManager: TokenManager) {


    /**
     * Group 1
     */

    suspend fun login(
        email:String, password:String
    ): Response<AuthResponse> {
        val response = apiService.login(email,password)
        tokenManager.saveToken(response.body()?.data?.token.toString())
        return response
    }

    suspend fun register(
        name: String, email: String, password: String
    ): Response<AuthResponse> = apiService.register(name,email,password)

    suspend fun otpVerification(
        email: String, otp: String
    ): Response<AuthResponse> {
        val response = apiService.otpVerification(email,otp)
        tokenManager.saveToken(response.body()?.data?.token.toString())
        return response
    }

    suspend fun resendVerificationCode(
        email: String
    ): Response<AuthResponse> = apiService.resendVerificationCode(email)



    /**
     * Group 2
     */

    suspend fun forgetPassword(
        email: String
    ): Response<AuthResponse> = apiService.forgetPassword(email)

    suspend fun otpForgetPassword(
        email: String, otp: String
    ): Response<AuthResponse> = apiService.otpForgetPassword(email,otp)

    suspend fun resendResetCode(
        email: String
    ): Response<AuthResponse> = apiService.resendResetCode(email)

    suspend fun resetPassword(
        email: String, password: String, token:String
    ): Response<AuthResponse> = apiService.resetPassword(email,password,token)


    /**
     * Group Three
     */


    suspend fun getTopics(): Resource<TopicDataResponse> = responseResource(api.getTopic())
    suspend fun getPopularCourses(): Resource<CoursesDataResponse> = responseResource(api.getPopularCourses())
    suspend fun getFreeCourses(): Resource<CoursesDataResponse> = responseResource(api.getFreeCourses())
    suspend fun getUpcomingCourses(): Resource<CoursesDataResponse> = responseResource(api.getUpcomingCourses())


    suspend fun getBlogs():Resource<DataResponse> {
        return responseResource(apiService.getBlogs())
    }


    private suspend fun <T> responseResource(apiCall:  T): Resource<T> =
        withContext(Dispatchers.IO) {
            Resource.Loading
            try {
                Resource.Success(apiCall)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
                    }
                    else -> {
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }


    /**
     * Group Four
     */

}


