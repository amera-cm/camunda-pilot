package io.cmt.gradle

import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object CmtBuildUtils {

    const val PRE_SNAPSHOT = "SNAPSHOT"
    const val PRE_BUILD = "BUILD"
    val PRE_NONE = null

    fun semver(major: Int, minor: Int, patch: Int, pre: String?): String {
        val version = "$major.$minor.$patch"
        val suffix = when {
            pre == null -> ""
            pre.trim().uppercase(Locale.getDefault()) == "BUILD" -> "-" + ZonedDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))
            else -> "-" + pre.trim().uppercase(Locale.getDefault())
        }
        return version + suffix
    }
}