import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.utils.respond
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.rest.builder.message.EmbedBuilder

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
                channel.createEmbed("asdasd", message.author, commandDescription, message.timestamp)
            }
        }
    }
}
