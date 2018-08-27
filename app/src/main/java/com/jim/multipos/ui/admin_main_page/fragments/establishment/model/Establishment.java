package com.jim.multipos.ui.admin_main_page.fragments.establishment.model;

public class Establishment {
    String name;
    String address;
    String phone;
    String stocks;
    String mainStock;
    String description;
    String posCount;

    public Establishment(String name, String address, String phone, String stocks, String mainStock, String description, String posCount) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.stocks = stocks;
        this.mainStock = mainStock;
        this.description = description;
        this.posCount = posCount;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getStocks() {
        return stocks;
    }

    public String getMainStock() {
        return mainStock;
    }

    public String getDescription() {
        return description;
    }

    public String getPosCount() {
        return posCount;
    }
}
