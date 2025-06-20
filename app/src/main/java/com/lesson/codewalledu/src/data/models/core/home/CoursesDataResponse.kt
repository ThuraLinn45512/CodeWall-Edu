package com.lesson.codewalledu.src.data.models.core.home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class CoursesDataResponse(
    val data: List<CoursesData>,
    val message: String
)


@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class CoursesData(
    val chapterCount: Int,
    val courseFees: String,
    val courseTitle: String,
    val id: Int,
    val imageUrl: String,
    val isPublished: Int,
    val length: String,
    val slug: String
):Parcelable, Serializable