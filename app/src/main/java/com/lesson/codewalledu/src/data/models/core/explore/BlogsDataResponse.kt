package com.lesson.codewalledu.src.data.models.core.explore

import java.io.Serializable

data class BlogsDataResponse(
    val message:String,
    val data: List<BlogsData>
)

data class BlogsData(
    val id: Int,
    var imageUrl:String,
    var contentTitle:String,
    var readingTime:String,
    var contentBody:String,
    var blogUrl:String,
    var createdAt:String,
): Serializable