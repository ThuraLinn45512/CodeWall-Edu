package com.lesson.codewalledu.src.data.models.core.home.search

data class SearchDataResponse(
    val data: SearchData,
    val message: String
)

data class SearchData(
    val blogs: List<BlogDataSearch>,
    val books: List<BookDataSearch>,
    val cheatSheets: List<CheatSheetDataSearch>,
    val courses: List<CourseDataSearch>,
    val news: List<NewsDataSearch>
)

data class BlogDataSearch(
    val blogUrl: String,
    val contentBody: String,
    val contentTitle: String,
    val createdAt: String,
    val id: Int,
    val imageUrl: String,
    val readingTime: String
)

data class BookDataSearch(
    val author: String,
    val contentUrl: String,
    val description: String,
    val id: Int,
    val imageUrl: String,
    val isbn: String,
    val mimeType: String,
    val publisher: String,
    val title: String,
    val year: Int
)

data class CheatSheetDataSearch(
    val cheatSheetContents: List<CheatSheetContentSearch>,
    val content: String,
    val createdAt: String,
    val description: String,
    val id: Int,
    val logoImageUrl: String,
    val techCategoryName: String
)

data class CheatSheetContentSearch(
    val cheatSheetId: Int,
    val content: String,
    val contentOrder: Int,
    val description: String,
    val id: Int,
    val imageUrl: String,
    val title: String
)

data class CourseDataSearch(
    val chapterCount: Int,
    val courseFees: String,
    val coursePoint: String,
    val courseTitle: String,
    val id: Int,
    val imageUrl: String,
    val isPublished: Int,
    val length: String,
    val slug: String
)

data class NewsDataSearch(
    val content: String,
    val description: String,
    val id: Int,
    val imageUrl: String,
    val publishedAt: String,
    val referenceUrl: String,
    val title: String
)