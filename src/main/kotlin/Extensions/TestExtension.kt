package Extensions

import AddTextImage
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.utils.respond
import createEmbed
import dev.kord.core.behavior.channel.createMessage
import dev.kord.rest.NamedFile
import dev.kord.rest.builder.message.EmbedBuilder
import java.awt.Color
import java.awt.Font
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.Consumer


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
//                channel.createEmbed("asdasd", message.author, commandDescription, message.timestamp)
                val img = AddTextImage(
                    "meta-files\\memes\\Sonic.png", Color.WHITE, Font.BOLD, "Arial",
                    "left-text-mode", "Temp.png"
                )
                img.addTextToImage(40, 100, 705, null, 40, message.content)
                val file = File(img.save()!!)
                channel.createMessage {
                    files.add(NamedFile(file.name, file.inputStream()))

                }
            }
        }
    }
}