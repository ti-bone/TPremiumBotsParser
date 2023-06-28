# TPremiumBotsParser

TPremiumBotsParser is a Java application that parses usernames with purchased "bots" upgrades from fragment.com on TON (Telegram Open Network).

To use the parser, you need to obtain an API token. You can get this token from [@tonapibot](https://t.me/tonapibot) on Telegram. Store this token safely, as you will need to provide it when running the TPremiumBotsParser application.

## Building the Application
Ensure that Java is installed on your system before you attempt to run the application. This project has been tested and is confirmed to work with Java 17.

### Unix-like systems
Build:
```
./gradlew clean shadowJar
```
## Windows
Build:
```
.\gradlew.bat clean shadowJar
```

## Configuring and running TPremiumBotsParser

Before starting the application, you'll need to do some initial configuration:

1. Run the application once. This will generate an error, but don't worry - this is expected. The error indicates that you need to set api_key in configuration file, which was just created, `config/appConfig.json`, has been created.

    - Unix:

        ```bash
        java -jar build/libs/TPremiumBotsParser-1.0-SNAPSHOT.jar
        ```

    - Windows:

        ```powershell
        java -jar .\build\libs\TPremiumBotsParser-1.0-SNAPSHOT.jar
        ```

2. Open the `config/appConfig.json` file in a text editor. Inside, you'll find a field for your API key. Set `api_key` to the key you obtained from the [@tonapibot](https://t.me/tonapibot) on Telegram.

3. Save and close `appConfig.json`. Now, when you run the TPremiumBotsParser, it will use your provided API key.

After this initial setup, you can simply run the application, and it'll start parsing.

For any questions or assistance, feel free to open an issue in this repository. Happy parsing!
