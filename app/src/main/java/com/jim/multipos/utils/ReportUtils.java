package com.jim.multipos.utils;

import com.jim.multipos.data.db.model.products.Product;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class ReportUtils {
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static String getProductName(Product product){
        StringBuilder productName = new StringBuilder(product.getName());
        productName.append(" (");
        productName.append(product.getSku());
        productName.append(")");
        return productName.toString();
    }
    public static String getQty(double amount, Product product, DecimalFormat decimalFormat){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(decimalFormat.format(amount));
        stringBuilder.append(" ");
        stringBuilder.append(product.getMainUnit().getAbbr());
        return stringBuilder.toString();
    }
}
