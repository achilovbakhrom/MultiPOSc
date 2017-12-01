package com.jim.multipos.ui.consignment_list.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment;

/**
 * Created by Sirojiddin on 30.11.2017.
 */

public interface ConsignmentListPresenter extends Presenter {
    void initConsignmentListRecyclerViewData(Long vendorId);
    void filterBy(ConsignmentListFragment.SortingStates sortingStates);
    void filterInvert();
    void search(String searchText);
    void setConsignment(Consignment consignment);
}
