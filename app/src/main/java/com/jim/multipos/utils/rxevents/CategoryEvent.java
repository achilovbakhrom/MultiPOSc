package com.jim.multipos.utils.rxevents;

import com.jim.multipos.data.db.model.products.Category;

/**
 * Created by DEV on 08.09.2017.
 */

public class CategoryEvent {
    Category category;
    String eventType;

    public CategoryEvent(Category category, String eventType) {
        this.category = category;
        this.eventType = eventType;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
