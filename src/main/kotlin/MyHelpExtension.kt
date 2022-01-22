import com.kotlindiscord.kord.extensions.builders.ExtensibleBotBuilder
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.chat.ChatCommand
import com.kotlindiscord.kord.extensions.commands.chat.ChatCommandRegistry
import com.kotlindiscord.kord.extensions.commands.converters.impl.stringList
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.base.HelpProvider
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.i18n.TranslationsProvider
import com.kotlindiscord.kord.extensions.pagination.BasePaginator
import com.kotlindiscord.kord.extensions.pagination.MessageButtonPaginator
import com.kotlindiscord.kord.extensions.pagination.pages.Page
import com.kotlindiscord.kord.extensions.pagination.pages.Pages
import com.kotlindiscord.kord.extensions.utils.deleteIgnoringNotFound
import com.kotlindiscord.kord.extensions.utils.getLocale
import com.kotlindiscord.kord.extensions.utils.respond
import dev.kord.core.event.message.MessageCreateEvent
import mu.KotlinLogging
import org.koin.core.component.inject

private val logger = KotlinLogging.logger {}

/** Number of commands to show per page. */
public const val HELP_PER_PAGE: Int = 10

private const val COMMANDS_GROUP = ""
private const val ARGUMENTS_GROUP = "Arguments"

class MyHelpExtension : HelpProvider, Extension(){
    override val name: String = "help"


    /** Message command registry. **/
    public val messageCommandsRegistry: ChatCommandRegistry by inject()

    /** Translations provider, for retrieving translations. **/
    public val translationsProvider: TranslationsProvider by inject()

    /** Bot settings. **/
    public val botSettings: ExtensibleBotBuilder by inject()

    /** Help extension settings, from the bot builder. **/
    public val settings: ExtensibleBotBuilder.ExtensionsBuilder.HelpExtensionBuilder =
        botSettings.extensionsBuilder.helpExtensionBuilder

    override suspend fun formatCommandHelp(
        prefix: String,
        event: MessageCreateEvent,
        command: ChatCommand<out Arguments>,
        longDescription: Boolean
    ): Triple<String, String, String> {
        TODO("Not yet implemented")
    }

    override suspend fun gatherCommands(event: MessageCreateEvent): List<ChatCommand<out Arguments>> =
        messageCommandsRegistry.commands
            .filter { !it.hidden && it.enabled && it.runChecks(event, false) }
            .sortedBy { it.name }

    override suspend fun getCommand(event: MessageCreateEvent, args: List<String>): ChatCommand<out Arguments>? {
        TODO("Not yet implemented")
    }

    override suspend fun getCommandHelpPaginator(
        event: MessageCreateEvent,
        prefix: String,
        command: ChatCommand<out Arguments>?
    ): BasePaginator {
        TODO("Not yet implemented")
    }

    override suspend fun getCommandHelpPaginator(
        event: MessageCreateEvent,
        prefix: String,
        args: List<String>
    ): BasePaginator {
        TODO("Not yet implemented")
    }

    override suspend fun getMainHelpPaginator(event: MessageCreateEvent, prefix: String): BasePaginator {
        var totalCommands = 0
        val locale = event.getLocale()

        val pages = Pages(COMMANDS_GROUP)
        val commandPages = gatherCommands(event)
            .chunked(HELP_PER_PAGE)
            .map { list ->
                list.map {
                    totalCommands += 1

                    formatCommandHelp(prefix, event, it)
                }
            }

        for (page in commandPages) {
            pages.addPage(
                COMMANDS_GROUP,

                Page {
                    description = page.joinToString("\n\n") { "${it.first}\n${it.second}" }
                    title = translationsProvider.translate("extensions.help.paginator.title.commands", locale)

                    footer {
                        text = "aaa"
//                        text = translationsProvider.translate(
//                            "hello",
//                            locale,
//                            replacements = arrayOf(totalCommands)
//                        )
                    }

                    color = settings.colourGetter(event)
                }
            )

            pages.addPage(
                ARGUMENTS_GROUP,

                Page {
                    description = page.joinToString("\n\n") { "${it.first}\n${it.third}" }
                    title = translationsProvider.translate("extensions.help.paginator.title.arguments", locale)

                    footer {
                        text = "aaa"
//                        text = translationsProvider.translate(
//                            "hello",
//                            locale,
//                            replacements = arrayOf(totalCommands)
//                        )
                    }

                    color = settings.colourGetter(event)
                }
            )
        }

        if (totalCommands < 1) {
            // This should never happen in most cases, but it's best to be safe about it

            pages.addPage(
                COMMANDS_GROUP,
                Page {
                    description = translationsProvider.translate("extensions.help.paginator.noCommands", locale)
                    title = translationsProvider.translate("extensions.help.paginator.noCommands", locale)
                    footer {
                        text = "aaa"
//                        text = translationsProvider.translate(
//                            "hello",
//                            locale,
//                            replacements = arrayOf(totalCommands)
//                        )
                    }
                    color = settings.colourGetter(event)
                }
            )
        }

        return MessageButtonPaginator(
            keepEmbed = settings.deletePaginatorOnTimeout.not(),
            locale = locale,
            owner = event.message.author,
            pages = pages,
            pingInReply = settings.pingInReply,
            targetMessage = event.message,
            timeoutSeconds = settings.paginatorTimeout,
        ).onTimeout {
            if (settings.deleteInvocationOnPaginatorTimeout) {
                @Suppress("TooGenericExceptionCaught")
                try {
                    event.message.deleteIgnoringNotFound()
                } catch (t: Throwable) {
                    logger.warn(t) { "Failed to delete command invocation." }
                }
            }
        }
    }


    override suspend fun setup() {
        chatCommand(::HelpArguments) {
            name = "extensions.help.commandName"
            aliasKey = "extensions.help.commandAliases"
            description = "extensions.help.commandDescription"

            localeFallback = true

            action {
                if (arguments.command.isEmpty()) {
                    getMainHelpPaginator(this).send()
                } else {
                    getCommandHelpPaginator(this, arguments.command).send()
                }
                message.respond("a")
            }
        }
    }

    public class HelpArguments : Arguments() {
        /** Command to get help for. **/
        public val command: List<String> by stringList(
            "command",
            "extensions.help.commandArguments.command",
            false
        )
    }
}