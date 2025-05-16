package com.lesson.codewalledu.src.utils.helpers

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.activity.OnBackPressedCallback


fun Fragment.handleOnBackPressed(destinationId: Int) {
    val onBackPressedDispatcher = requireActivity().onBackPressedDispatcher
    onBackPressedDispatcher.addCallback(
        viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(destinationId)
            }
        })
}