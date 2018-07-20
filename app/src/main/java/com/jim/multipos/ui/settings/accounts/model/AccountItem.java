package com.jim.multipos.ui.settings.accounts.model;

import com.jim.multipos.data.db.model.Account;

public class AccountItem {

    private Account account;
    private String name;
    private boolean active;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
