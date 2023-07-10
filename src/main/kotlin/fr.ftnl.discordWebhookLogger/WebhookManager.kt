package fr.ftnl.discordWebhookLogger

import club.minnced.discord.webhook.WebhookClientBuilder
import club.minnced.discord.webhook.send.WebhookMessage

object WebhookManager {
    fun sender(message: WebhookMessage, webhookUrl: String, threadId: Long? = null) {
        if (webhookUrl.isBlank()) return
        
        val baseWebhook = "https://discord.com/api/webhooks/"
        val regex = Regex("$baseWebhook(?<webhookId>\\d{15,25})/(?<webhookToken>\\w{50,100})(/(?<threadId>\\d{15,25}))?")
        
        val matchResult = regex.find(webhookUrl) ?: return
        val webhookId = matchResult.groups["webhookId"]?.value?.toLongOrNull() ?: return
        val webhookToken = matchResult.groups["webhookToken"]?.value ?: return
        val webhookThread = threadId ?: matchResult.groups["threadId"]?.value?.toLong()
        
        val client = WebhookClientBuilder(webhookId, webhookToken)
        if (webhookThread != null) client.setThreadId(webhookThread)
        client.build().send(message)
    }
}