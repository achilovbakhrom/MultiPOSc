package com.jim.multipos.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalUtils {
    public static double roundDouble(double v){
        return (double)Math.round(v * 1000000d) / 1000000d;
    }
    public static double plus(double x,double y){
       return roundDouble(x+y);
    }
    public static double minus(double x,double y){
        return roundDouble(x-y);
    }
    public static double multiply(double x,double y){
        return roundDouble(x*y);
    }
    public static double divide(double x,double y){
        return roundDouble(x/y);
    }
}
