package com.lesson.codewalledu.src.ui.views.core.home

import androidx.lifecycle.ViewModelProvider
import com.lesson.codewalledu.src.data.repositories.AuthRepository

class HomeViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}