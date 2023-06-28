package dev.bytefuck.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TransactionModel {
    @JsonProperty("@type")
    private String type;

    @JsonProperty("utime")
    private Long uTime;

    @JsonProperty("data")
    private String data;

    @JsonProperty("transaction_id")
    private TransactionIdModel transactionId;

    @JsonProperty("fee")
    private String fee;

    @JsonProperty("storage_fee")
    private String storageFee;

    @JsonProperty("other_fee")
    private String otherFee;

    @JsonProperty("in_msg")
    private MessageModel inMsg;

    @JsonProperty("out_msgs")
    private List<MessageModel> outMsgs;
}
