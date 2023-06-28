package dev.bytefuck.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageDataModel {
    @JsonProperty("@type")
    private String type;

    @JsonProperty("text")
    private String text;
}
