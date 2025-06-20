package com.lesson.codewalledu.src.ui.views.core.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesson.codewalledu.src.data.models.core.UserDataResponse
import com.lesson.codewalledu.src.data.models.core.home.BannerDataResponse
import com.lesson.codewalledu.src.data.models.core.home.CoursesDataResponse
import com.lesson.codewalledu.src.data.models.core.home.TopicDataResponse
import com.lesson.codewalledu.src.data.repositories.AuthRepository
import com.lesson.codewalledu.src.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _userDataResponse: MutableLiveData<Resource<UserDataResponse>> = MutableLiveData()
    val userDataResponse: LiveData<Resource<UserDataResponse>>
        get() = _userDataResponse

    private val _bannersDataResponse: MutableLiveData<Resource<BannerDataResponse>> = MutableLiveData()
    val bannersDataResponse: LiveData<Resource<BannerDataResponse>>
        get() = _bannersDataResponse

    // NEW LiveData for SHUFFLED topics that the Fragment will observe
    private val _shuffledTopicDataResponse: MutableLiveData<Resource<TopicDataResponse>> = MutableLiveData()
    val shuffledTopicDataResponse: LiveData<Resource<TopicDataResponse>>
        get() = _shuffledTopicDataResponse


    private val _popularCoursesDataResponse: MutableLiveData<Resource<CoursesDataResponse>> = MutableLiveData()
    val popularCoursesDataResponse: LiveData<Resource<CoursesDataResponse>>
        get() = _popularCoursesDataResponse



    private val _freeCoursesDataResponse: MutableLiveData<Resource<CoursesDataResponse>> = MutableLiveData()
    val freeCoursesDataResponse: LiveData<Resource<CoursesDataResponse>>
        get() = _freeCoursesDataResponse




    private val _upcomingCoursesDataResponse: MutableLiveData<Resource<CoursesDataResponse>> = MutableLiveData()
    val upcomingCoursesDataResponse: LiveData<Resource<CoursesDataResponse>>
        get() = _upcomingCoursesDataResponse


    init {
        viewModelScope.launch {
            getUser()
            getBanners()
            getTopics()
            getPopularCourses()
            getFreeCourses()
            getUpcomingCourses()
        }
    }

    suspend fun getUser()  {
        val response = repository.getUser()
        _userDataResponse.value = response
    }


    suspend fun getBanners()  {
        val response = repository.getBanners()
        _bannersDataResponse.value = response
    }


    // Modified getTopics to handle shuffling
    private suspend fun getTopics() {
        // Only fetch and shuffle if shuffled data hasn't been set yet
        // This prevents re-shuffling on subsequent ViewModel initializations (e.g., process death)
        if (_shuffledTopicDataResponse.value == null || _shuffledTopicDataResponse.value is Resource.Loading) {
            val response = repository.getTopics()


            if (response is Resource.Success) {
                // Shuffle the data here, ONCE, upon successful fetch
                val shuffledList = response.value.data.shuffled()
                _shuffledTopicDataResponse.value = Resource.Success(TopicDataResponse(shuffledList,response.value.message.toString()))
            } else {
                // Pass through loading/failure states directly to the shuffled LiveData
                _shuffledTopicDataResponse.value = Resource.Loading
            }
        }
    }

    suspend fun getPopularCourses()  {
        val response = repository.getPopularCourses()
        _popularCoursesDataResponse.value = response
    }

    suspend fun getFreeCourses()  {
        val response = repository.getFreeCourses()
        _freeCoursesDataResponse.value = response
    }

    suspend fun getUpcomingCourses()  {
        val response = repository.getUpcomingCourses()
        _upcomingCoursesDataResponse.value = response
    }

    // You might want to add a public refresh function if you need to manually refresh
    // all data from the Fragment later, but for "shuffle once", this is enough.
    fun refreshAllData() {
        viewModelScope.launch {
            getBanners()
            getTopics() // This will now fetch and then shuffle again if needed (based on the if condition)
            getPopularCourses()
            getFreeCourses()
            getUpcomingCourses()
        }
    }



}