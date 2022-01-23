import Extensions.MyHelpExtension
import Extensions.TestExtension
import com.kotlindiscord.kord.extensions.ExtensibleBot
import dev.kord.common.entity.PresenceStatus
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.User
import kotlinx.datetime.Instant
import org.koin.core.component.get

lateinit var botClient: User
private set

suspend fun main() {
    val bot = ExtensibleBot(Keys.API_KEY){
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

suspend fun MessageChannelBehavior.createEmbed(_description: String, user: User? = null,
                                               commandDescription: String, time: Instant? = null,
                                               imageUrl: String? = null){
    this.createEmbed {
        description = _description
        color = Constants.EMBED_COLOR
        image = imageUrl
        author {
            name = user?.username
            icon = user?.avatar?.url
        }
        footer {
            text = commandDescription
            icon = botClient.avatar?.url
        }
        timestamp = time
    }
}