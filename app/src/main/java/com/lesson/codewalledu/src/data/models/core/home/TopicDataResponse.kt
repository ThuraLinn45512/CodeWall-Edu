package com.lesson.codewalledu.src.data.models.core.home

data class TopicDataResponse(
    val data: List<TopicData>,
    val message: String
)

data class TopicData(
    val backgroundColor: String,
    val iconUrl: String,
    val id: Int,
    val name: String,
    val slug: String,
    val textColor: String
)