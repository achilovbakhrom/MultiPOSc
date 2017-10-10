package com.jim.multipos.utils.rxevents;

public class FirstConfigureActivityEvent {
    private Object object;
    private String eventType;

    public FirstConfigureActivityEvent(String eventType ) {
        this.object = new Object();
        this.eventType = eventType;
    }

    public FirstConfigureActivityEvent(String eventType, Object object) {
        this.object = object;
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
