package dev.bytefuck.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppConfigModel {
    @JsonProperty("base_url")
    private String baseURL;

    @JsonProperty("api_key")
    private String apiKey;

    @JsonProperty("fragment_wallet")
    private String fragmentWallet;
}
