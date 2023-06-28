package dev.bytefuck.model;

import lombok.Data;

import java.util.List;

@Data
public class TransactionsResponseModel {
    private boolean ok;
    private List<TransactionModel> result;
}
