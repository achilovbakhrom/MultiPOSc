package com.jim.multipos.ui.reports.tills.model;

import lombok.Data;

@Data
public class DetailsOfTill {
    private String account;
    double payIn = 0, payOut = 0, bankDrop = 0, payToVendor = 0,
            incomeDebt = 0, cashTransactions = 0, tips = 0, totalStartingCash = 0,
            totalAmount = 0, tillAmountVariance = 0, closedAmount = 0, toNextAmount = 0;
}
