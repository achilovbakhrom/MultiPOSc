package com.jim.multipos.ui.admin_main_page.fragments.product_class.model;

public class ProductClass {
    String name;
    String description;

    public ProductClass(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
