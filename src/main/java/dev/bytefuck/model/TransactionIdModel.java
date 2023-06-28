package dev.bytefuck.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransactionIdModel {
    @JsonProperty("@type")
    private String type;

    @JsonProperty("lt")
    private String lt;

    @JsonProperty("hash")
    private String hash;
}
