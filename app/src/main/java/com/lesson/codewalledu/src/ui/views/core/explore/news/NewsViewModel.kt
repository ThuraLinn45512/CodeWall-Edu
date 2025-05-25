package com.lesson.codewalledu.src.ui.views.core.explore.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesson.codewalledu.src.data.models.core.explore.NewsDataResponse
import com.lesson.codewalledu.src.data.repositories.AuthRepository
import com.lesson.codewalledu.src.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel@Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _newsBlogsDataResponse: MutableLiveData<Resource<NewsDataResponse>> = MutableLiveData()
    val newsBlogsDataResponse: LiveData<Resource<NewsDataResponse>>
        get() = _newsBlogsDataResponse

    init {
        viewModelScope.launch {
            getNews()
        }
    }

    suspend fun getNews()  {
        val response = repository.getNews()
        _newsBlogsDataResponse.value = response
    }
}