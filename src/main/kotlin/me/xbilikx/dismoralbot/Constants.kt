package me.xbilikx.dismoralbot

import com.kotlindiscord.kord.extensions.utils.env
import dev.kord.common.Color

object Constants {
    val BOT_TOKEN = env("TOKEN") // Get environment variable
    val EMBED_COLOR = Color(127,0,0)
}