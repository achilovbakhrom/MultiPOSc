package com.jim.multipos.utils;

public class DecimalUtils {
    public static double roundDouble(double v) {
        return (double) Math.round(v * 100000d) / 100000d;
    }

    public static double plus(double x, double y) {
        return roundDouble(x + y);
    }

    public static double minus(double x, double y) {
        return roundDouble(x - y);
    }

    public static double multiply(double x, double y) {
        return roundDouble(x * y);
    }

    public static double divide(double x, double y) {
        return roundDouble(x / y);
    }
}
