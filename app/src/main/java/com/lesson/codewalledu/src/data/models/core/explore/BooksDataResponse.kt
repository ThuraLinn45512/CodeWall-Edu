package com.lesson.codewalledu.src.data.models.core.explore

import java.io.Serializable

data class BooksDataResponse(
    val data: List<BookData>,
    val message: String
)

data class BookData(
    val id: Int,
    val author: String,
    val contentUrl: String,
    val description: String,
    val imageUrl: String,
    val isbn: String,
    val mimeType: String,
    val publisher: String,
    val title: String,
    val year: Int
): Serializable