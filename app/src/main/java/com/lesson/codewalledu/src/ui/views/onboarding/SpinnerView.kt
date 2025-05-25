package com.lesson.codewalledu.src.ui.views.onboarding

import android.content.Context
import android.widget.Spinner
import com.lesson.codewalledu.R
import com.lesson.codewalledu.src.utils.adapters.SpinnerAdapter


fun createSpinner(context: Context, spinner: Spinner ) {
        val languages = listOf("English", "Myanmar", "Japanese")
        val flagImageIds = listOf(R.drawable.flag_american, R.drawable.flag_myanmar, R.drawable.flag_japanese)
        val adapter = SpinnerAdapter(context, languages, flagImageIds)
        spinner.adapter = adapter
}



