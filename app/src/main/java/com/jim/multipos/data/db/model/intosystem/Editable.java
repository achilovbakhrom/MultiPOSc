package com.jim.multipos.data.db.model.intosystem;

/**
 * Created by developer on 17.09.2017.
 */

public interface Editable extends Activatable{
    String getId();
    void setId(String id);
    boolean isActive();
    void setActive(boolean active);
    boolean isDeleted();
    void setDeleted(boolean deleted);
    boolean isNotModifyted();
    void setNotModifyted(boolean notModifyted);
    String getRootId();
    void setRootId(String rootId);
    Long getCreatedDate();
    void setCreatedDate(long createdDate);
}
