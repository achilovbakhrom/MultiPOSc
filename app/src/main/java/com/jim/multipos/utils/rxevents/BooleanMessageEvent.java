package com.jim.multipos.utils.rxevents;

/**
 * Created by DEV on 16.08.2017.
 */

public class BooleanMessageEvent {
    private boolean state;
    private String msg;

    public BooleanMessageEvent(boolean state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
