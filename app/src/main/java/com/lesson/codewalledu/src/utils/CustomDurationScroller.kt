package com.lesson.codewalledu.src.utils

import android.content.Context
import android.widget.Scroller
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.Field

class CustomDurationScroller(context: Context?, private val duration: Int) : Scroller(context) {

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, this.duration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, this.duration)
    }
}

fun setViewPagerScrollSpeed(viewPager: ViewPager2, duration: Int) {
    try {
        val scroller: Field = ViewPager2::class.java.getDeclaredField("mScroller")
        scroller.isAccessible = true
        val customScroller = CustomDurationScroller(viewPager.context, duration)
        scroller.set(viewPager, customScroller)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}