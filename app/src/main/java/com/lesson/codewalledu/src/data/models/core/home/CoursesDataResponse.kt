package com.lesson.codewalledu.src.data.models.core.home

import java.io.Serializable

data class CoursesDataResponse(
    val data: List<CoursesData>
)

data class CoursesData(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val durationIcon: String="",
    val duration: String= "",
    val chapters: String= "",
    val fees: String= ""
): Serializable
