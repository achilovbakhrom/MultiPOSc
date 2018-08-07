package com.jim.multipos.utils.rxevents.admin_auth_events;

public class GeneralEvent {
    String username;
    String password;

    public GeneralEvent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
