package com.lesson.codewalledu.src.data.models.auth

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val email_verified_at: String,
    val created_at: String,
    val updated_at: String
)