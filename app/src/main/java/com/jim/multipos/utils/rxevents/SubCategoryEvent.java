package com.jim.multipos.utils.rxevents;

import com.jim.multipos.data.db.model.products.Category;

/**
 * Created by DEV on 08.09.2017.
 */

public class SubCategoryEvent {
    private Category subCategory;
    private String eventType;

    public SubCategoryEvent(Category subCategory, String eventType) {
        this.subCategory = subCategory;
        this.eventType = eventType;
    }

    public Category getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(Category subCategory) {
        this.subCategory = subCategory;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
