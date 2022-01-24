package me.xbilikx.dismoralbot

import com.kotlindiscord.kord.extensions.utils.env
import dev.kord.common.Color

object Constants {
    val BOT_TOKEN = env("TOKEN") // Get environment variable
    val EMBED_COLOR = Color(127,0,0)
    const val LEFT_TEXT_MODE = "left-text-mode"
    const val RIGHT_TEXT_MODE = "right-text-mode"
    const val CENTER_TEXT_MODE = "center-text-mode"
    const val MEMES_PATH = "meta-files\\memes\\"
}