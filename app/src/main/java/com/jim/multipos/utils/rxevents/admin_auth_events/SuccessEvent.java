package com.jim.multipos.utils.rxevents.admin_auth_events;

public class SuccessEvent {
    boolean success;

    public SuccessEvent() {
        super();
    }

    public SuccessEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
