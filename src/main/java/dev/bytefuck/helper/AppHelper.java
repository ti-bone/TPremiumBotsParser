package dev.bytefuck.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bytefuck.model.AppConfigModel;
import lombok.SneakyThrows;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppHelper {
    private AppHelper() {
    }

    @SneakyThrows
    public static void createDirectories() {
        var userDir = Paths.get(System.getProperty("user.dir"));
        var configDir = userDir.resolve("config");

        if (!Files.exists(configDir)) {
            Files.createDirectory(configDir);
        }

        var applicationConfig = configDir.resolve(APP_CONFIG_JSON);

        if (!Files.exists(applicationConfig)) {
            var classLoader = AppHelper.class.getClassLoader();

            try (var in = classLoader.getResourceAsStream(APP_CONFIG_JSON)) {
                if (in == null)
                    throw new IllegalArgumentException("А где конфиг?");

                try (var out = new FileOutputStream(applicationConfig.toFile())) {
                    out.write(in.readAllBytes());
                }
            }
        }
    }

    @SneakyThrows(IOException.class)
    public static AppConfigModel loadConfig() {
        var configDir = Paths.get(System.getProperty("user.dir")).resolve("config");

        var config = new ObjectMapper().readValue(configDir.resolve(APP_CONFIG_JSON).toFile(), AppConfigModel.class);

        checkConfigFieldValidity(config.getBaseURL(), "base_url");
        checkConfigFieldValidity(config.getApiKey(), "api_key");
        checkConfigFieldValidity(config.getFragmentWallet(), "fragment_wallet");

        return config;
    }

    private static void checkConfigFieldValidity(String fieldValue, String fieldName) {
        if (fieldValue == null || fieldValue.isEmpty()) {
            throw new IllegalArgumentException("Config field " + fieldName + " is invalid.");
        }
    }

    private static final String APP_CONFIG_JSON = "appConfig.json";
}
