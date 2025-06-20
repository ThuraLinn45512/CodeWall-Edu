package com.lesson.codewalledu.src.utils.helpers



fun String.stringToDouble(): Double {
    var fees = this.split(",")
    var courseFees = ""
    var i = 0
    while (i < fees.size) {
        courseFees = courseFees + fees[i]
        i++
    }
    var courseFeesInDouble = courseFees.toDouble()
    println(courseFeesInDouble)
    return courseFeesInDouble
}