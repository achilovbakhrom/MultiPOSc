package com.jim.multipos.data.db.model.intosystem;

/**
 * Created by developer on 17.09.2017.
 */

public interface Activatable {
    boolean isActive();
    void setActive(boolean isActive);
    String getId();
}
