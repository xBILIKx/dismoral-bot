import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.chatGroupCommand
import com.kotlindiscord.kord.extensions.utils.delete
import com.kotlindiscord.kord.extensions.utils.respond

class SendPhotoExtension : Extension() {
    override val name: String
        get() = "SendPhoto"

    override suspend fun setup() {
        chatGroupCommand {
            name = "greet"
            description = "Get a greeting!"

            chatCommand {
                name = "hello"
                description = "Hello!"



                action {
                    message.respond("Fuck you, ${message.author?.mention}!")
                    message.delete(null)
                }
            }

            chatCommand {
                name = "welcome"
                description = "Welcome!"

                action {
                    message.respond("Welcum, ${message.author?.mention}!")

                }
            }
        }
    }
}