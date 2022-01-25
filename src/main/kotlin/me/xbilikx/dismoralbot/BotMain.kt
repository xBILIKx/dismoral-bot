package me.xbilikx.dismoralbot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import dev.kord.common.entity.PresenceStatus
import dev.kord.core.Kord
import dev.kord.core.entity.User
import kotlinx.coroutines.runBlocking
import me.xbilikx.dismoralbot.extensions.MyHelpExtension
import me.xbilikx.dismoralbot.extensions.TestExtension
import org.koin.core.component.get

lateinit var botClient: User
    private set

suspend fun botStart() {
    val bot = ExtensibleBot(Constants.BOT_TOKEN){
        chatCommands {
            defaultPrefix = "?"
            enabled = true
        }
        presence {
            status = PresenceStatus.Online
            competing("degradation")
        }
        extensions {
            add(::TestExtension)
            add(::MyHelpExtension)
            help{
                color { Constants.EMBED_COLOR }
                enableBundledExtension = false
            }
        }
    }

    botClient = bot.get<Kord>().getSelf()
    bot.start()
}

fun main(args: Array<String>){
    runBlocking {
        botStart()
    }
}