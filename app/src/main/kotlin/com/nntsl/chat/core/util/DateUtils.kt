package com.nntsl.chat.core.util

import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

fun isTheSameDay(date1: Instant, date2: Instant): Boolean {
    return date1.toJavaInstant().truncatedTo(ChronoUnit.DAYS)
        .equals(date2.toJavaInstant().truncatedTo(ChronoUnit.DAYS))
}

fun isTheSameWeek(date: Instant): Boolean {
    val diffInMillis = Date().time - date.toEpochMilliseconds()
    return TimeUnit.MILLISECONDS.toDays(diffInMillis) < 7
}

fun isDifferenceInSecMore(date1: Instant, date2: Instant, differenceInSec: Int): Boolean {
    val diffInMillis = kotlin.math.abs(date1.toEpochMilliseconds() - date2.toEpochMilliseconds())
    return diffInMillis > (differenceInSec * 1000)
}

fun currentTime(): Instant {
    return Instant.fromEpochMilliseconds(System.currentTimeMillis())
}