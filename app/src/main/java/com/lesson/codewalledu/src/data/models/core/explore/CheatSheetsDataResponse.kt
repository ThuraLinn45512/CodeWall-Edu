package com.lesson.codewalledu.src.data.models.core.explore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class CheatSheetsDataResponse(
    val data: List<CheatSheetData>,
    val message: String
)


@Parcelize
data class CheatSheetData(
    val id: Int,
    val techCategoryName: String,
    val description: String,
    val logoImageUrl: String,
    val createdAt: String,
    val content: String,
    val cheatSheetContents: List<DiscoveryContent>,
): Parcelable, Serializable

@Parcelize
data class DiscoveryContent(
    val cheatSheetId: Int,
    val content: String,
    val contentOrder: Int,
    val description: String,
    val id: Int,
    val imageUrl: String,
    val title: String
): Parcelable