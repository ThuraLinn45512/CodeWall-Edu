package com.lesson.codewalledu.src.utils.helpers

import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView



fun TextView.applyGradient(vararg colors: Int) {
    val paint = this.paint
    val textWidth = paint.measureText(this.text?.toString() ?: "")
    val shader = LinearGradient(
        0f, 0f, textWidth, 0f,
        colors,
        null,
        Shader.TileMode.CLAMP
    )
    paint.shader = shader
}