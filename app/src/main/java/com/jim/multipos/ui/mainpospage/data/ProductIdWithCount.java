package com.jim.multipos.ui.mainpospage.data;

public class ProductIdWithCount {
    long productId;
    double count; //count can be +/-

    public ProductIdWithCount(long productId, double count) {
        this.productId = productId;
        this.count = count;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }
}
