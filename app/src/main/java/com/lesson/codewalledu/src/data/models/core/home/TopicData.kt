package com.lesson.codewalledu.src.data.models.core.home

data class TopicData(
                val id: Int = 0,
//                var icon: Int,
                var iconUrl: String,
                var title: String,
//                var backgroundColor: Int = R.color.light_blue,
                var backgroundColor: String = "#ffffff",
//                var textColor: Int= R.color.dark_blue
                var textColor: String= "#000000"
)