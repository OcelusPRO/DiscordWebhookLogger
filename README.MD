# Discord Webhook Logger
### A simple discord webhook logger for SLF4J

## Installation
### Gradle
```kts
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.OcelusPRO:DiscordWebhookLogger:${LAST_COMMIT}")
}
```

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url> 
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.OcelusPRO</groupId>
        <artifactId>DiscordWebhookLogger</artifactId>
        <version>${LAST_COMMIT}</version>
    </dependency>
</dependencies>
```

## Usage
Sample configuration for logback.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false" scan="true" scanPeriod="15 seconds">
   <appender name="discordWebhookLogger" class="fr.ftnl.discordWebhookLogger.DiscordLogAppender">
        <webhookUrl>https://discord.com/api/webhooks/...</webhookUrl>
        <username>DiscordWebhookLogger-Error</username>
        <avatarUrl>https://cdn.discordapp.com/avatars/...</avatarUrl>
        <baseMessage>a new error has been logged !</baseMessage>
        <embedTitle>DiscordWebhookLogger - Error</embedTitle>
        <minLevel>WARN</minLevel> <!-- minimal level for send webhook --> 
    </appender>
    <root level="DEBUG">
	    <appender-ref ref="discordWebhookLogger"/>
    </root>
</configuration>
```

You can also use env variables for configure the logger
```
DWL_USERNAME        --> The username of the webhook
DWL_URL             --> The url of the webhook
DWL_AVATAR_URL      --> The avatar url of the webhook
DWL_INT-COLOR       --> The color of the embed
DWL_BASE-MESSAGE    --> The base message of the webhook
DWL_MIN-LEVEL       --> The minimal level for send the webhook
DWL_EMBED-TITLE     --> The title of the embed
DWL_DELAY           --> The delay between two webhook send
```

## License
[GNY AGPL-3.0 License](./LICENSE.txt)

