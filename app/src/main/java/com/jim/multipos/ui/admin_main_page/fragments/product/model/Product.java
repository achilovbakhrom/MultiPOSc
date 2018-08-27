package com.jim.multipos.ui.admin_main_page.fragments.product.model;

public class Product {
    String name;
    String barcode;
    String sku;
    String priceUnit_1;
    String priceUnit_2;
    String imageUrl;


    public Product(String name, String barcode, String sku, String priceUnit_1, String priceUnit_2, String imageUrl) {
        this.name = name;
        this.barcode = barcode;
        this.sku = sku;
        this.priceUnit_1 = priceUnit_1;
        this.priceUnit_2 = priceUnit_2;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getSku() {
        return sku;
    }

    public String getPriceUnit_1() {
        return priceUnit_1;
    }

    public String getPriceUnit_2() {
        return priceUnit_2;
    }
}
