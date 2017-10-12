package com.jim.multipos.data.db.model.intosystem;

/**
 * Created by developer on 17.09.2017.
 */

public interface Editable extends Activatable{
    void setId(Long id);
    boolean isActive();
    void setActive(boolean active);
    boolean isDeleted();
    void setDeleted(boolean deleted);
    boolean isNotModifyted();
    void setNotModifyted(boolean notModifyted);
    Long getRootId();
    void setRootId(Long rootId);
    Long getCreatedDate();
    void setCreatedDate(long createdDate);
}
