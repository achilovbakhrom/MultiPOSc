package com.jim.multipos.ui.reports.summary_report.adapter;

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

    public String getFirstString() {
        return firstString;
    }

    public void setFirstString(String firstString) {
        this.firstString = firstString;
    }

    public String getSecondString() {
        return secondString;
    }

    public void setSecondString(String secondString) {
        this.secondString = secondString;
    }

    public String getThirdString() {
        return thirdString;
    }

    public void setThirdString(String thirdString) {
        this.thirdString = thirdString;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }
}
