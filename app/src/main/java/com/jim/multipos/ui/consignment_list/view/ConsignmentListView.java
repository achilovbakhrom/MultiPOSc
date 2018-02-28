package com.jim.multipos.ui.consignment_list.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.currency.Currency;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Sirojiddin on 30.11.2017.
 */

public interface ConsignmentListView extends BaseView{
    void setConsignmentListRecyclerViewData(List<Consignment> consignmentList, Currency currency);
    void notifyList();
    void initSearchResults(List<Consignment> searchResults, String searchText, Currency currency);
    void openConsignment(Long consignmentId, Integer consignmentType);
    void sendConsignmentEvent(int event);
    void dateIntervalPicked(Calendar fromDate, Calendar toDate);
    void datePicked(Calendar pickedDate);
    void clearInterval();
    void sendInventoryStateEvent(int event);
}
