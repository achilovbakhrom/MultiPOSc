package com.jim.multipos.ui.consignment_list.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.ui.consignment_list.model.InvoiceListItem;
import com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment;
import com.jim.multipos.ui.consignment_list.view.ConsignmentListView;
import com.jim.multipos.utils.BundleConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 30.11.2017.
 */

public class ConsignmentListPresenterImpl extends BasePresenterImpl<ConsignmentListView> implements ConsignmentListPresenter {

    private DatabaseManager databaseManager;
    private List<InvoiceListItem> invoiceListItems, searchResults;
    private int sorting = 1;
    private Currency currency;
    private Long vendorId;
    private ConsignmentListFragment.SortingStates sortType = ConsignmentListFragment.SortingStates.FILTERED_BY_DATE;
    private Calendar fromDate, toDate;
    private SimpleDateFormat simpleDateFormat;
    private int[] filterConfig;

    @Inject
    protected ConsignmentListPresenterImpl(ConsignmentListView consignmentListView, DatabaseManager databaseManager) {
        super(consignmentListView);
        this.databaseManager = databaseManager;
        invoiceListItems = new ArrayList<>();
        filterConfig = new int[]{1, 1};
    }

    @Override
    public void initConsignmentListRecyclerViewData(Long vendorId) {
        this.vendorId = vendorId;
        Date currentDate = new Date();
        fromDate = new GregorianCalendar();
        toDate = new GregorianCalendar();

        toDate.setTime(currentDate);
        toDate.set(Calendar.HOUR_OF_DAY, 23);
        toDate.set(Calendar.MINUTE, 59);
        toDate.set(Calendar.SECOND, 59);

        fromDate.add(Calendar.MONTH, -1);
        fromDate.set(Calendar.HOUR_OF_DAY, 0);
        fromDate.set(Calendar.MINUTE, 0);
        fromDate.set(Calendar.SECOND, 0);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        view.updateDateIntervalUI(simpleDateFormat.format(fromDate.getTimeInMillis()) + " - " + simpleDateFormat.format(toDate.getTimeInMillis()));
        view.setVendorName(databaseManager.getVendorById(vendorId).blockingSingle().getName());
        currency = databaseManager.getMainCurrency();
        databaseManager.getInvoiceListItemByVendorId(vendorId).subscribe(invoiceListItems1 -> {
            for (InvoiceListItem item : invoiceListItems1) {
                if (filterConfig[0] == 1 && item.getType() == BundleConstants.INVOICE) {
                    this.invoiceListItems.add(item);
                    continue;
                }
                if (filterConfig[1] == 1 && item.getType() == BundleConstants.OUTVOICE) {
                    this.invoiceListItems.add(item);
                }
            }
            sortList();
            view.seInvoiceListRecyclerViewData(invoiceListItems, currency);
        });
    }

    @Override
    public void filterBy(ConsignmentListFragment.SortingStates sortingStates) {
        this.sortType = sortingStates;
        sorting = 1;
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
            view.seInvoiceListRecyclerViewData(invoiceListItems, currency);
        } else {
            searchResults = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm MM/dd/yyyy");
            for (int i = 0; i < invoiceListItems.size(); i++) {
                if (invoiceListItems.get(i).getInvoice() != null) {
                    if (invoiceListItems.get(i).getInvoice().getConsigmentNumber().toUpperCase().contains(searchText.toUpperCase())) {
                        searchResults.add(invoiceListItems.get(i));
                        continue;
                    }
                    Date date = new Date(invoiceListItems.get(i).getCreatedDate());
                    if (formatter.format(date).toUpperCase().contains(searchText.toUpperCase())) {
                        searchResults.add(invoiceListItems.get(i));
                        continue;
                    }
                    if (invoiceListItems.get(i).getInvoice().getTotalAmount().toString().toUpperCase().contains(searchText.toUpperCase())) {
                        searchResults.add(invoiceListItems.get(i));
                        continue;
                    }
                    List<IncomeProduct> productList = invoiceListItems.get(i).getInvoice().getIncomeProducts();
                    for (int j = 0; j < productList.size(); j++) {
                        if (productList.get(j).getProduct().getSku().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(invoiceListItems.get(i));
                            break;
                        }
                        if (productList.get(j).getProduct().getName().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(invoiceListItems.get(i));
                            break;
                        }
                        if (productList.get(j).getCountValue().toString().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(invoiceListItems.get(i));
                            break;
                        }
                    }
                } else {
                    if (invoiceListItems.get(i).getOutvoice().getConsigmentNumber().toUpperCase().contains(searchText.toUpperCase())) {
                        searchResults.add(invoiceListItems.get(i));
                        continue;
                    }
                    Date date = new Date(invoiceListItems.get(i).getCreatedDate());
                    if (formatter.format(date).toUpperCase().contains(searchText.toUpperCase())) {
                        searchResults.add(invoiceListItems.get(i));
                        continue;
                    }
                    if (invoiceListItems.get(i).getOutvoice().getTotalAmount().toString().toUpperCase().contains(searchText.toUpperCase())) {
                        searchResults.add(invoiceListItems.get(i));
                        continue;
                    }
                    List<OutcomeProduct> productList = invoiceListItems.get(i).getOutvoice().getOutcomeProducts();
                    for (int j = 0; j < productList.size(); j++) {
                        if (productList.get(j).getProduct().getSku().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(invoiceListItems.get(i));
                            break;
                        }
                        if (productList.get(j).getProduct().getName().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(invoiceListItems.get(i));
                            break;
                        }
                        if (productList.get(j).getSumCountValue().toString().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(invoiceListItems.get(i));
                            break;
                        }
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
    public void dateIntervalPicked(Calendar fromDate, Calendar toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        view.updateDateIntervalUI(simpleDateFormat.format(fromDate.getTimeInMillis()) + " - " + simpleDateFormat.format(toDate.getTimeInMillis()));
        databaseManager.getInvoiceListItemsInIntervalByVendor(vendorId, fromDate, toDate).subscribe(invoiceListItems1 -> {
            invoiceListItems.clear();
            for (InvoiceListItem item : invoiceListItems1) {
                if (filterConfig[0] == 1 && item.getType() == BundleConstants.INVOICE) {
                    this.invoiceListItems.add(item);
                    continue;
                }
                if (filterConfig[1] == 1 && item.getType() == BundleConstants.OUTVOICE) {
                    this.invoiceListItems.add(item);
                }
            }
            sortList();
            view.seInvoiceListRecyclerViewData(invoiceListItems, currency);
        });
    }

    @Override
    public void datePicked(Calendar pickedDate) {
        Calendar temp = new GregorianCalendar();
        temp.setTimeInMillis(pickedDate.getTimeInMillis());
        this.fromDate = temp;
        this.toDate = null;
        view.updateDateIntervalUI(simpleDateFormat.format(fromDate.getTimeInMillis()));
        databaseManager.getInvoiceListItemsInIntervalByVendor(vendorId, fromDate, toDate).subscribe(invoiceListItems1 -> {
            invoiceListItems.clear();
            for (InvoiceListItem item : invoiceListItems1) {
                if (filterConfig[0] == 1 && item.getType() == BundleConstants.INVOICE) {
                    this.invoiceListItems.add(item);
                    continue;
                }
                if (filterConfig[1] == 1 && item.getType() == BundleConstants.OUTVOICE) {
                    this.invoiceListItems.add(item);
                }
            }
            sortList();
            view.seInvoiceListRecyclerViewData(invoiceListItems, currency);
        });

    }

    @Override
    public void clearIntervals() {
        fromDate = null;
        toDate = null;
        databaseManager.getInvoiceListItemByVendorId(vendorId).subscribe(invoiceListItems1 -> {
            invoiceListItems.clear();
            for (InvoiceListItem item : invoiceListItems1) {
                if (filterConfig[0] == 1 && item.getType() == BundleConstants.INVOICE) {
                    this.invoiceListItems.add(item);
                    continue;
                }
                if (filterConfig[1] == 1 && item.getType() == BundleConstants.OUTVOICE) {
                    this.invoiceListItems.add(item);
                }
            }
            sortList();
            view.seInvoiceListRecyclerViewData(invoiceListItems, currency);
        });
    }

    @Override
    public void openDateIntervalPicker() {
        view.openDateIntervalPicker(fromDate, toDate);
    }

    @Override
    public void onFilterClicked() {
        view.openFilterDialog(filterConfig);
    }

    @Override
    public void onFilterConfigChanged(int[] config) {
        filterConfig = config;
        databaseManager.getInvoiceListItemByVendorId(vendorId).subscribe(invoiceListItems1 -> {
            invoiceListItems.clear();
            for (InvoiceListItem item : invoiceListItems1) {
                if (filterConfig[0] == 1 && item.getType() == BundleConstants.INVOICE) {
                    this.invoiceListItems.add(item);
                    continue;
                }
                if (filterConfig[1] == 1 && item.getType() == BundleConstants.OUTVOICE) {
                    this.invoiceListItems.add(item);
                }
            }
            sortList();
            view.seInvoiceListRecyclerViewData(invoiceListItems, currency);
        });
    }

    private void sortList() {
        List<InvoiceListItem> invoiceListItemTemp;
        if (searchResults != null)
            invoiceListItemTemp = searchResults;
        else invoiceListItemTemp = invoiceListItems;
        switch (sortType) {
            case FILTERED_BY_DATE:
                Collections.sort(invoiceListItemTemp, (consignment, t1) -> consignment.getCreatedDate().compareTo(t1.getCreatedDate()) * sorting);
                break;
            case FILTERED_BY_CONSIGNMENT:
                Collections.sort(invoiceListItemTemp, (consignment, t1) -> consignment.getNumber().compareTo(t1.getNumber()) * sorting);
                break;
            case FILTERED_BY_DEBT:
                Collections.sort(invoiceListItemTemp, (consignment, t1) -> consignment.getTotalAmount().compareTo(t1.getTotalAmount()) * sorting);
                break;
            case FILTERED_BY_STATUS:
                Collections.sort(invoiceListItemTemp, (consignment, t1) -> consignment.getType().compareTo(t1.getType()) * sorting);
                break;
        }
    }
}
