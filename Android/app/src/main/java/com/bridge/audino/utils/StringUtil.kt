package com.bridge.audino.utils

object StringUtil {

    fun maskFollowers(followers: String): String {
        return followers.replace(("(?=((...)+)$)").toRegex(), "$0 ")
    }
}