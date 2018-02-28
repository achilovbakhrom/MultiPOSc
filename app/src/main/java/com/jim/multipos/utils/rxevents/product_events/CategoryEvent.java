package com.jim.multipos.utils.rxevents.product_events;

import com.jim.multipos.data.db.model.products.Category;

import lombok.Data;

/**
 * Created by Sirojiddin on 27.02.2018.
 */
@Data
public class CategoryEvent {
    private Category category;
    private int type;

    public CategoryEvent(Category category, int type) {
        this.category = category;
        this.type = type;
    }
}
