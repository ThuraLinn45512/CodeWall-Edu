package com.lesson.codewalledu.src.data.models.auth

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val balance: String,
    val isPremiumMember: Boolean,
    val points: String,
    val premiumMemberUntil: String
)