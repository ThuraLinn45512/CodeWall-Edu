package com.lesson.codewalledu.src.ui.views.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesson.codewalledu.src.data.models.auth.AuthResponse
import com.lesson.codewalledu.src.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {


    private val _loginResponse  = MutableLiveData<Response<AuthResponse>>()
    val loginResponse: LiveData<Response<AuthResponse>>
        get() = _loginResponse

    fun login(email: String, password: String)= viewModelScope.launch {
            val response = repository.login(email,password)
            _loginResponse.value = response
    }

    


}