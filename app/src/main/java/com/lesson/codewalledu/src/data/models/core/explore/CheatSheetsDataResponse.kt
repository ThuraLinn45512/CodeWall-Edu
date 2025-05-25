package com.lesson.codewalledu.src.data.models.core.explore

data class CheatSheetsDataResponse(
    val data: List<Data>,
    val message: String
)

data class Data(
    val content: String,
    val description: String,
    val cheatSheetContents: List<DiscoveryContent>,
    val id: Int,
    val logoImageUrl: String,
    val techCategoryName: String
)

data class DiscoveryContent(
    val cheatSheetId: Int,
    val content: String,
    val contentOrder: Int,
    val description: String,
    val id: Int,
    val imageUrl: String,
    val title: String
)