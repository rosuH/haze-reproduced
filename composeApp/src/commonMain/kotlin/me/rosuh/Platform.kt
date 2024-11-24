package me.rosuh

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform