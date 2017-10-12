package com.jim.multipos.utils.rxevents;

/**
 * Created by DEV on 08.09.2017.
 */

public class SubCategoryEvent {
    private SubCategory subCategory;
    private String eventType;

    public SubCategoryEvent(SubCategory subCategory, String eventType) {
        this.subCategory = subCategory;
        this.eventType = eventType;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
