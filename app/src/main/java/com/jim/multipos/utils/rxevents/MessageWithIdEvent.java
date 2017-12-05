package com.jim.multipos.utils.rxevents;

/**
 * Created by DEV on 16.08.2017.
 */

public class MessageWithIdEvent {
    private Long id;
    private String message;

    public MessageWithIdEvent(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
