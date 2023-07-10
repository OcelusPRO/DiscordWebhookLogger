package fr.ftnl.discordWebhookLogger

import club.minnced.discord.webhook.send.WebhookMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class DiscordLogManager(private val url: String, private val delay: Long = 5000) {
    val postQueue: LinkedList<WebhookMessage> = LinkedList()
    init {
        Thread {
            runBlocking {
                launch {
                    postingService()
                }
            }
        }.start()
    }
    
    private suspend fun postingService(){
        while (true) {
            if (postQueue.isNotEmpty()) {
                val wMessage = postQueue.poll()
                WebhookManager.sender(wMessage, url)
            }
            delay(delay)
        }
    }
}
