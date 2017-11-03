package com.jim.multipos.data.db.model.intosystem;

import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;

/**
 * Created by Sirojiddin on 31.10.2017.
 */

public class FolderItem {

    private Category category;
    private Product product;
    private int size;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
