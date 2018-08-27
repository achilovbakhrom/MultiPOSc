package com.jim.multipos.ui.admin_main_page.fragments.dashboard.model;

public class Pos {
    String posID;
    String cash;
    String card;
    String orders;

    public Pos(String posID, String cash, String card, String orders) {
        this.posID = posID;
        this.cash = cash;
        this.card = card;
        this.orders = orders;
    }

    public String getPosID() {
        return posID;
    }

    public String getCash() {
        return cash;
    }

    public String getCard() {
        return card;
    }

    public String getOrders() {
        return orders;
    }
}
