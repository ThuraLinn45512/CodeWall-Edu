package com.lesson.codewalledu.src.ui.views.core.explore.blogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesson.codewalledu.src.data.models.core.explore.BlogsDataResponse
import com.lesson.codewalledu.src.data.repositories.AuthRepository
import com.lesson.codewalledu.src.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlogsViewModel@Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _blogsDataResponse: MutableLiveData<Resource<BlogsDataResponse>> = MutableLiveData()
    val blogsDataResponse: LiveData<Resource<BlogsDataResponse>>
        get() = _blogsDataResponse


    init {
        viewModelScope.launch {
            getBlogs()
        }
    }

    suspend fun getBlogs()  {
        val response = repository.getBlogs()
        _blogsDataResponse.value = response
    }

}