package dev.bytefuck.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageModel {
    @JsonProperty("@type")
    private String type;

    @JsonProperty("source")
    private String source;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("value")
    private String value;

    @JsonProperty("fwd_fee")
    private String fwdFee;

    @JsonProperty("ihr_fee")
    private String ihrFee;

    @JsonProperty("created_lt")
    private String createdLt;

    @JsonProperty("body_hash")
    private String bodyHash;

    @JsonProperty("msg_data")
    private MessageDataModel msgData;

    @JsonProperty("message")
    private String message;
}
