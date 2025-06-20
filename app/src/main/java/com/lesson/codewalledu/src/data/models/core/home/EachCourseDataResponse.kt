package com.lesson.codewalledu.src.data.models.core.home

data class EachCourseDataResponse(
    val data: Data,
    val message: String
)

data class Data(
    val course: Course,
    val isEnrolled: Boolean,
    val totalChapters: Int,
    val totalModules: Int
)

data class Course(
    val id: Int,
    val courseTitle: String,
    val imageUrl: String,
    val chapterCount: Int,
    val courseFees: String,
    val curriculums: List<Curriculum>,
    val headerSections: List<HeaderSection>,
    val isPublished: Int,
    val length: String,
    val otherSections: List<OtherSection>,
    val slug: String
)

data class Curriculum(
    val chapters: List<Chapter>,
    val description: String,
    val id: Int,
    val moduleOrder: Int,
    val title: String
)



data class HeaderSection(
    val content: String,
    val id: Int,
    val sectionName: String
)

data class OtherSection(
    val content: String,
    val id: Int,
    val sectionName: String
)

data class Chapter(
    val chapterOrder: Int,
    val id: Int,
    val title: String
)