package com.jim.multipos.data.db.model.intosystem;

/**
 * Created by developer on 17.09.2017.
 */

public interface Editable extends Activatable{
    String getNewVersionId();
    void setNewVersionId(String newVersionId);
    boolean isNewVersion();
    void isNewVersion(boolean isNewVersion);

}
