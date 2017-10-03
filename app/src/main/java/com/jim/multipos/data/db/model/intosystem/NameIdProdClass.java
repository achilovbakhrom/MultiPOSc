package com.jim.multipos.data.db.model.intosystem;

/**
 * Created by developer on 30.08.2017.
 */

public class NameIdProdClass {
    private String name;
    private String id;

    public NameIdProdClass(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
