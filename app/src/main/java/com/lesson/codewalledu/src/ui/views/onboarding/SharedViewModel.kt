package com.lesson.codewalledu.src.ui.views.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val position = MutableLiveData(0)
}