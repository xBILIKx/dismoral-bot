package me.xbilikx.dismoralbot.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.utils.respond
import me.xbilikx.dismoralbot.AddTextImage
import me.xbilikx.dismoralbot.Constants
import me.xbilikx.dismoralbot.createAwaitEmbed
import me.xbilikx.dismoralbot.createEmbedMessage
import java.awt.Color
import java.io.File


class TestExtension: Extension() {

    override val name: String = "test"

    override suspend fun setup() {
        chatCommand {
            name = "welcome"
            description = "Welcome!"

            action {
                message.respond("Welcum, ${message.author?.mention}!")
                message.delete(null)
            }
        }
        chatCommand{
            name = "havePhoto"
            description = "Check if a message with a photo"

            action {
                message.respond("${message.attachments.size}")
            }
        }
        chatCommand {
            val commandDescription = "Send photo"
            name = "sendMyPhoto"
            description = "Send photo"

            action {
                message.delete()
                val channel = message.channel
                val img = AddTextImage(
                    Constants.MEMES_PATH+ "Sonic.png", Color.WHITE, Constants.LEFT_TEXT_MODE,
                    45, "Temp.png"
                )
                img.addTextToImage(40, 100, 705, null, message.content.substring(13))
                val file = File(img.save())
                channel.createEmbedMessage(null, message.author, "))", message.timestamp,
                    file.name)
            }
        }
    }
}