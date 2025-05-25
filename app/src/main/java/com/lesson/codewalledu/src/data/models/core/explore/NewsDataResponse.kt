package com.lesson.codewalledu.src.data.models.core.explore

data class NewsDataResponse(
    val message:String,
    val data: List<NewsData>
    )

data class NewsData (
    val id: Int,
    val title: String,
    val description: String,
    val content: String,
    val imageUrl: String,
    val newsCategory: NewsCategory,
    val publishedAt: String,
    val referenceUrl: String
)

data class NewsCategory(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String,
    val imageUrl: String
)
