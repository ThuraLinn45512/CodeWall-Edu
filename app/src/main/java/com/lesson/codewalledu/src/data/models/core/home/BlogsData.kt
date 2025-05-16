package com.lesson.codewalledu.src.data.models.core.home

import java.io.Serializable

data class BlogsData(
    val id: Int,
    var imageUrl:String,
    var contentTitle:String,
    var readingTime:String,
    var contentBody:String,
    var blogUrl:String,
    var createdAt:String,
):Serializable