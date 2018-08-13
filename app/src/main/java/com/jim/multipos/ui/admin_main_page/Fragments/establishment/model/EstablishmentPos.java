package com.jim.multipos.ui.admin_main_page.fragments.establishment.model;

public class EstablishmentPos {
    String name;
    String address;
    String description;

    public EstablishmentPos(String name, String address, String description) {
        this.name = name;
        this.address = address;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }
}
