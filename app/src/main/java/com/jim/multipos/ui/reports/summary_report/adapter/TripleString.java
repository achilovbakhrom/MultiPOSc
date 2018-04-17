package com.jim.multipos.ui.reports.summary_report.adapter;

import lombok.Data;

@Data
public class TripleString {
    String firstString;
    String secondString;
    String thirdString;
    boolean bold;

    public TripleString(String firstString, String secondString, String thirdString, boolean bold) {
        this.firstString = firstString;
        this.secondString = secondString;
        this.thirdString = thirdString;
        this.bold = bold;
    }
}
