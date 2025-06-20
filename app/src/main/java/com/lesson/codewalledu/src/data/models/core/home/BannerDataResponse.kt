package com.lesson.codewalledu.src.data.models.core.home

data class BannerDataResponse(
    val data: List<BannerData>,
    val message: String
)


data class BannerData(
    val id: Int,
    val imageUrl: String,
    val subTitle: String,
    val title: String
)