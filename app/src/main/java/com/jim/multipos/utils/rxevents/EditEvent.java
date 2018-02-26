package com.jim.multipos.utils.rxevents;

/**
 * Created by Sirojiddin on 17.02.2018.
 */

public class EditEvent {
    private Long oldId;
    private Long newId;
    private String message;

    public EditEvent(Long oldId, Long newId, String message) {
        this.oldId = oldId;
        this.newId = newId;
        this.message = message;
    }

    public Long getOldId() {
        return oldId;
    }

    public void setOldId(Long oldId) {
        this.oldId = oldId;
    }

    public Long getNewId() {
        return newId;
    }

    public void setNewId(Long newId) {
        this.newId = newId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
