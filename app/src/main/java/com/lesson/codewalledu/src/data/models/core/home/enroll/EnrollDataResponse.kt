package com.lesson.codewalledu.src.data.models.core.home.enroll

data class EnrollDataResponse(
    val data: EnrollData,
    val message: String
)

data class EnrollData(
    val course: Course,
    val enrollmentInfo: EnrollmentInfo
)

data class Course(
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

data class EnrollmentInfo(
    val enrollmentMethod: EnrollmentMethod,
    val promotions: Promotions
)

data class EnrollmentMethod(
    val balance: Balance,
    val point: Point
)

data class Balance(
    val available: String,
    val isNeeded: Boolean,
    val needed: String,
    val summary: Summary,
    val total: String,
    val totalDiscount: String
)

data class Point(
    val available: String,
    val isNeeded: Boolean,
    val needed: String,
    val summary: Summary,
    val total: String,
    val totalDiscount: String
)

data class Summary(
    val discounts: List<Discount>,
    val original: String
)

data class Discount(
    val rate: String,
    val saved: String,
    val title: String
)

data class Promotions(
    val normalPromotions: List<NormalPromotion>,
    val premiumPromotion: PremiumPromotion
)

data class NormalPromotion(
    val rate: String,
    val title: String
)

data class PremiumPromotion(
    val rate: String,
    val title: String
)