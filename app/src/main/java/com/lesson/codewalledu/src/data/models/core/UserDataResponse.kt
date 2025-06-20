package com.lesson.codewalledu.src.data.models.core

data class UserDataResponse(
    val data: UserData,
    val message: String
)

data class UserData(
    val balance: String,
    val email: String,
    val id: Int,
    val isPremiumMember: Boolean,
    val name: String,
    val points: String,
    val premiumMemberUntil: String
)