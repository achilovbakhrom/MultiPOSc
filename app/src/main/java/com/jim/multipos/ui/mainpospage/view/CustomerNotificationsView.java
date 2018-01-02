package com.jim.multipos.ui.mainpospage.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.intosystem.NotificationData;

import java.util.List;

/**
 * Created by Sirojiddin on 26.12.2017.
 */

public interface CustomerNotificationsView extends BaseView {
    void fillNotificationData(List<NotificationData> data);
}
