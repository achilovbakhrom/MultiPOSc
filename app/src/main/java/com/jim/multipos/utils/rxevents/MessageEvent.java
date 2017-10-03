package com.jim.multipos.utils.rxevents;

/**
 * Created by DEV on 16.08.2017.
 */

public class MessageEvent {
    private String category;

    public MessageEvent(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
