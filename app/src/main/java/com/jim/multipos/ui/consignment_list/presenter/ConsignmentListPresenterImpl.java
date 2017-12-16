package com.jim.multipos.ui.consignment_list.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.ui.consignment_list.model.ConsignmentListItem;
import com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment;
import com.jim.multipos.ui.consignment_list.view.ConsignmentListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.data.db.model.consignment.Consignment.INCOME_CONSIGNMENT;
import static com.jim.multipos.ui.consignment.view.IncomeConsignmentFragment.CONSIGNMENT_UPDATE;

/**
 * Created by Sirojiddin on 30.11.2017.
 */

public class ConsignmentListPresenterImpl extends BasePresenterImpl<ConsignmentListView> implements ConsignmentListPresenter {

    private DatabaseManager databaseManager;
    private List<Consignment> consignmentList, searchResults;
    private int sorting = 1;
    private Currency currency;
    private ConsignmentListFragment.SortingStates sortType = ConsignmentListFragment.SortingStates.FILTERED_BY_DATE;

    @Inject
    protected ConsignmentListPresenterImpl(ConsignmentListView consignmentListView, DatabaseManager databaseManager) {
        super(consignmentListView);
        this.databaseManager = databaseManager;
        consignmentList = new ArrayList<>();
    }

    @Override
    public void initConsignmentListRecyclerViewData(Long vendorId) {
        currency = databaseManager.getMainCurrency();
        databaseManager.getConsignmentsByVendorId(vendorId).subscribe(consignments -> {
            this.consignmentList = consignments;
            sortList();
            view.setConsignmentListRecyclerViewData(consignments, currency);
        });
    }

    @Override
    public void filterBy(ConsignmentListFragment.SortingStates sortingStates) {
        sorting = 1;
        this.sortType = sortingStates;
        sortList();
        view.notifyList();
    }

    @Override
    public void filterInvert() {
        sorting *= -1;
        sortList();
        view.notifyList();
    }

    @Override
    public void search(String searchText) {
        if (searchText.isEmpty()) {
            searchResults = null;
            sortList();
            view.setConsignmentListRecyclerViewData(consignmentList, currency);
        } else {
            searchResults = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm MM/dd/yyyy");
            for (int i = 0; i < consignmentList.size(); i++) {
                if (consignmentList.get(i).getConsignmentNumber().toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(consignmentList.get(i));
                    continue;
                }
                Date date = new Date(consignmentList.get(i).getCreatedDate());
                if (formatter.format(date).toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(consignmentList.get(i));
                    continue;
                }
                if (consignmentList.get(i).getTotalAmount().toString().toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(consignmentList.get(i));
                    continue;
                }
                List<ConsignmentProduct> productList = consignmentList.get(i).getConsignmentProducts();
                for (int j = 0; j < productList.size(); j++) {
                    if (productList.get(j).getProduct().getSku().toUpperCase().contains(searchText.toUpperCase())) {
                        searchResults.add(consignmentList.get(i));
                        break;
                    }
                    if (productList.get(j).getProduct().getName().toUpperCase().contains(searchText.toUpperCase())) {
                        searchResults.add(consignmentList.get(i));
                        break;
                    }
                    if (productList.get(j).getCountValue().toString().toUpperCase().contains(searchText.toUpperCase())) {
                        searchResults.add(consignmentList.get(i));
                        break;
                    }
                }

            }
            sortList();
            view.initSearchResults(searchResults, searchText, currency);
        }
    }

    @Override
    public void setConsignment(Consignment consignment) {
        view.openConsignment(consignment.getId(), consignment.getConsignmentType());
    }

    @Override
    public void deleteConsignment(Consignment consignment) {
        BillingOperations debt = databaseManager.getBillingOperationsByConsignmentId(consignment.getId()).blockingGet();
        debt.setDeleted(true);
        databaseManager.insertBillingOperation(debt).subscribe();
        if (consignment.getFirstPayId() != null) {
            BillingOperations firstPay = databaseManager.getBillingOperationsById(consignment.getFirstPayId()).blockingGet();
            firstPay.setDeleted(true);
            databaseManager.insertBillingOperation(firstPay).subscribe();
        }
        List<ConsignmentProduct> consignmentProductList = consignment.getAllConsignmentProducts();
        for (int i = 0; i < consignmentProductList.size(); i++) {
            consignmentProductList.get(i).setDeleted(true);
            WarehouseOperations warehouseOperations = consignmentProductList.get(i).getWarehouse();
            warehouseOperations.setNotModifyted(false);
            warehouseOperations.setType(WarehouseOperations.CONSIGNMENT_DELETED);
            databaseManager.insertConsignmentProduct(consignmentProductList.get(i)).subscribe();
            databaseManager.replaceWarehouseOperation(warehouseOperations).subscribe();
        }
        consignment.setDeleted(true);
        databaseManager.insertConsignment(consignment, null, null, null).subscribe();
        consignmentList.remove(consignment);
        view.notifyList();
        view.sendEvent(CONSIGNMENT_UPDATE);
    }

    private void sortList() {
        List<Consignment> consignmentListTemp;
        if (searchResults != null)
            consignmentListTemp = searchResults;
        else consignmentListTemp = consignmentList;
        switch (sortType) {
            case FILTERED_BY_DATE:
                Collections.sort(consignmentListTemp, (consignment, t1) -> consignment.getCreatedDate().compareTo(t1.getCreatedDate()) * sorting);
                break;
            case FILTERED_BY_CONSIGNMENT:
                Collections.sort(consignmentListTemp, (consignment, t1) -> consignment.getConsignmentNumber().compareTo(t1.getConsignmentNumber()) * sorting);
                break;
            case FILTERED_BY_DEBT:
                Collections.sort(consignmentListTemp, (consignment, t1) -> consignment.getTotalAmount().compareTo(t1.getTotalAmount()) * sorting);
                break;
            case FILTERED_BY_STATUS:
                Collections.sort(consignmentListTemp, (consignment, t1) -> consignment.getConsignmentType().compareTo(t1.getConsignmentType()) * sorting);
                break;
        }
    }
}
