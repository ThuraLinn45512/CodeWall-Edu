package com.lesson.codewalledu.src.data.repositories

import com.lesson.codewalledu.src.data.models.auth.AuthResponse
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
import com.lesson.codewalledu.src.data.remote.ApiService
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

    suspend fun getUser(): Resource<UserDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getUser("Bearer $token"))
    }

    suspend fun getSearchData(key: String): Resource<SearchDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getSearchData("Bearer $token",key))
    }


    suspend fun getBanners(): Resource<BannerDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getBanners("Bearer $token"))
    }

    suspend fun getTopics(): Resource<TopicDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getTopics("Bearer $token"))
    }

    suspend fun getTopicCoursesData(courseSlug: String): Resource<CoursesDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getTopicCoursesData("Bearer $token",courseSlug))
    }

    suspend fun getPopularCourses(): Resource<CoursesDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getPopularCourses("Bearer $token"))
    }

    suspend fun getFreeCourses(): Resource<CoursesDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getFreeCourses("Bearer $token"))
    }

    suspend fun getUpcomingCourses(): Resource<CoursesDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getUpcomingCourses("Bearer $token"))
    }

    suspend fun getEachCourseData(courseId: Int): Resource<EachCourseDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getEachCourseData("Bearer $token",courseId))
    }

    suspend fun getEnrollData(course:Int): Resource<EnrollDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getEnrollData("Bearer $token",course))
    }


    /**
     * Group Five
     */
    suspend fun getBlogs():Resource<BlogsDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getBlogs("Bearer $token"))
    }


    suspend fun getNews():Resource<NewsDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getNews("Bearer $token"))
    }

    suspend fun getBooks():Resource<BooksDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getBooks("Bearer $token"))
    }

    suspend fun getCheatSheets():Resource<CheatSheetsDataResponse> {
        val token = tokenManager.getToken()
        return responseResource(apiService.getCheatSheets("Bearer $token"))
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

    }


    /**
     * Group Four
     */




