package com.lesson.codewalledu.src.ui.views.auth.register

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
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {


    private val _signupResponse  = MutableLiveData<Response<AuthResponse>>()
    val registerResponse: LiveData<Response<AuthResponse>>
        get() = _signupResponse

    fun register(name: String, email: String, password: String)= viewModelScope.launch {
        val response = repository.register(name,email,password)
        _signupResponse.value = response
    }

    


}