package com.jim.multipos.utils.rxevents.product_events;

import com.jim.multipos.data.db.model.products.Category;


/**
 * Created by Sirojiddin on 27.02.2018.
 */
public class CategoryEvent {
    private Category category;
    private int type;

    public CategoryEvent(Category category, int type) {
        this.category = category;
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
