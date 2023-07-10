package fr.ftnl.discordWebhookLogger


import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.UnsynchronizedAppenderBase
import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import java.awt.Color

class DiscordLogAppender : UnsynchronizedAppenderBase<ILoggingEvent>() {
    private var username:       String? =   System.getenv("DWL_USERNAME")                   ?: "Error logger"
    private var url:            String? =   System.getenv("DWL_URL")                        ?: null
    private var avatarUrl:      String? =   System.getenv("DWL_AVATAR_URL")                 ?: null
    private var clr:            Int?    =   System.getenv("DWL_INT-COLOR")?.toIntOrNull()   ?: 0xFF0000
    private var baseMessage:    String? =   System.getenv("DWL_BASE-MESSAGE")               ?: null
    private var minLevel:       String? =   System.getenv("DWL_MIN-LEVEL")                  ?: "WARN"
    private var embedTitle:     String? =   System.getenv("DWL_EMBED-TITLE")                ?: "DiscordWebhookLogger-%s"
    private val delay:          Long    =   System.getenv("DWL_DELAY")?.toLongOrNull()      ?: 5000L
    private lateinit var instance: DiscordLogManager
    init {
        require(url != null) { "DiscordWebhookLogger: No webhook url provided" }
        require(username?.contains("discord")?.not() == true) { "DiscordWebhookLogger: Username cannot contain 'discord'" }
        if (url != null) instance = DiscordLogManager(url!!, delay)
    }
    
    override fun append(event: ILoggingEvent?) {
        if (event == null) return
        val tClr = event.argumentArray.find { it is Color } as? Color ?: clr.let { if (it != null) Color(it) else null }
        val min = Level.valueOf(minLevel)
        if (event.level.levelInt < min.levelInt) return
        event.message.chunked(4000).forEach {
            post(it, event.level, tClr)
        }
    }
    
    private fun post(message: String, level: Level, msgColor: Color? = null) {
        val clr = msgColor ?: when (level) {
            Level.ERROR -> Color.RED
            Level.WARN -> Color.YELLOW
            Level.INFO -> Color.GREEN
            Level.DEBUG -> Color.BLUE
            Level.TRACE -> Color.GRAY
            else -> Color.BLACK
        }
        val embed = WebhookEmbedBuilder()
        
        val title = embedTitle?.format(level.levelStr)
        if (title != null ) embed.setTitle(WebhookEmbed.EmbedTitle(title, null))
        embed.setDescription(message)
        embed.setColor(clr.toIntColor())
        
        val wMessage = WebhookMessageBuilder()
            .setContent(baseMessage)
            .setUsername(username)
            .setAvatarUrl(avatarUrl)
            .addEmbeds(embed.build())
            .build()
        instance.postQueue.add(wMessage)
    }
    
    private fun Color.toIntColor(): Int {
        var red = this.red
        var green = this.green
        var blue = this.blue
        red = red shl 16 and 0x00FF0000
        green = green shl 8 and 0x0000FF00
        blue = blue and 0x000000FF
        return -0x1000000 or red or green or blue
    }
}
