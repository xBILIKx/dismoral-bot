package me.xbilikx.dismoralbot

import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.rest.builder.message.create.embed
import kotlinx.datetime.Instant
import kotlin.io.path.Path

suspend fun MessageChannelBehavior.createEmbedMessage(_description: String?, user: User? = null,
                                                      commandDescription: String, time: Instant? = null,
                                                      imageUrl: String? = null){
    val awaitEmbed = this.createAwaitEmbed()

    this.createMessage {
        embed {
            description = _description
            color = Constants.EMBED_COLOR
            image = "attachment://$imageUrl"
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
        imageUrl?.let{addFile(Path(imageUrl))}
        awaitEmbed.delete()
    }
}

suspend fun MessageChannelBehavior.createAwaitEmbed(): Message {
    return this.createEmbed {
        description = "Message loading, pls wait"
        author{
            name = "Wait pls"
            icon = "http://pa1.narvii.com/7021/a62adb44ea367558f5b4a01fb12d6aadddf11742r1-320-320_00.gif"
        }

        color = Constants.EMBED_COLOR
    }
}