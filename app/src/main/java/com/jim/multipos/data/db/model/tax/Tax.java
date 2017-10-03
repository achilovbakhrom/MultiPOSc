package com.jim.multipos.data.db.model.tax;


public class Tax {
    private String name;
    private double roundingType;

    public Tax(String name, double roundingType) {
        this.name = name;
        this.roundingType = roundingType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRoundingType() {
        return roundingType;
    }

    public void setRoundingType(double roundingType) {
        this.roundingType = roundingType;
    }
}
