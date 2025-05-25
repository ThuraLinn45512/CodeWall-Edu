package com.lesson.codewalledu.src.ui.views.core.explore.books

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesson.codewalledu.src.data.models.core.explore.BooksDataResponse
import com.lesson.codewalledu.src.data.repositories.AuthRepository
import com.lesson.codewalledu.src.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel@Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _booksDataResponse: MutableLiveData<Resource<BooksDataResponse>> = MutableLiveData()
    val booksDataResponse: LiveData<Resource<BooksDataResponse>>
        get() = _booksDataResponse


    init {
        viewModelScope.launch {
            getBooks()
        }
    }

    suspend fun getBooks()  {
        val response = repository.getBooks()
        _booksDataResponse.value = response
    }
}