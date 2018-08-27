package com.jim.multipos.ui.admin_main_page.fragments.dashboard.model;

public class Orders {
    String sum;
    String date;
    String products;
    String status;

    public Orders(String sum, String date, String products, String status) {
        this.sum = sum;
        this.date = date;
        this.products = products;
        this.status = status;
    }

    public String getSum() {
        return sum;
    }

    public String getDate() {
        return date;
    }

    public String getProducts() {
        return products;
    }

    public String getStatus() {
        return status;
    }
}
