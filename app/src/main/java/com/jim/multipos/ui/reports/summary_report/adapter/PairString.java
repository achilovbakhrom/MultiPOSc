package com.jim.multipos.ui.reports.summary_report.adapter;

import lombok.Data;

@Data
public class PairString {
    String firstString;
    String secondString;

    public PairString(String firstString, String secondString) {
        this.firstString = firstString;
        this.secondString = secondString;
    }
}
