package com.lesson.codewalledu.src.ui.views.core


import android.view.View
import androidx.core.widget.NestedScrollView

class BottomNavScrollListener(
    private val view: View,
) : NestedScrollView.OnScrollChangeListener {

    private var isVisible = true

    override fun onScrollChange(
        v: NestedScrollView,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        if (scrollY > oldScrollY && isVisible) { // Scrolling up
            view.animate()
                .translationY(view.height.toFloat())
                .withEndAction {
                    view.visibility = View.GONE // Use View.GONE to completely hide the view
                }
                .start()

            isVisible = false



        } else if (scrollY < oldScrollY && !isVisible) { // Scrolling down

            view.animate()
                .translationY(0f)
                .withStartAction {
                    view.visibility = View.VISIBLE
                }.start()
            isVisible = true


        }



    }

}

