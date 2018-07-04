package com.jim.multipos.ui.products_expired.expired_products;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

public class ExpiredProductsFragmentPresenterImpl extends BasePresenterImpl<ExpiredProductsFragmentView> implements ExpiredProductsFragmentPresenter {

    private DatabaseManager databaseManager;
    private List<StockQueue> stockQueueList, searchResults;
    private SimpleDateFormat simpleDateFormat;
    private int[] filterConfig;


    @Inject
    protected ExpiredProductsFragmentPresenterImpl(ExpiredProductsFragmentView expiredProductsFragmentView, DatabaseManager databaseManager) {
        super(expiredProductsFragmentView);
        this.databaseManager = databaseManager;
        stockQueueList = new ArrayList<>();
        filterConfig = new int[]{1, 1};
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        Date currentDate = new Date();
        currentDate.setTime(System.currentTimeMillis());
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        view.setTime(simpleDateFormat.format(currentDate));

        GregorianCalendar now = new GregorianCalendar();
        databaseManager.getExpiredStockQueue().subscribe(stockQueues -> {
            for (StockQueue stockQueue : stockQueues) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(new Date(stockQueue.getExpiredProductDate()));
                if (filterConfig[0] == 1 && (now.after(calendar) || now.equals(calendar))) {
                    stockQueueList.add(stockQueue);
                    continue;
                }
                if (filterConfig[1] == 1 && (now.before(calendar))) {
                    stockQueueList.add(stockQueue);
                }
            }
            Collections.sort(stockQueueList, (o1, o2) -> o1.getExpiredProductDate().compareTo(o2.getExpiredProductDate()));
            view.setExpiredProducts(stockQueueList);
        });
    }

    @Override
    public void onFilterClicked() {
        view.openFilterDialog(filterConfig);
    }

    @Override
    public void search(String searchText) {
        if (searchText.isEmpty()) {
            searchResults = null;
            view.setExpiredProducts(stockQueueList);
        } else {
            searchResults = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm MM/dd/yyyy");
            for (int i = 0; i < stockQueueList.size(); i++) {
                if (stockQueueList.get(i).getVendor().getName().toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(stockQueueList.get(i));
                    continue;
                }
                if (stockQueueList.get(i).getProduct().getName().toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(stockQueueList.get(i));
                    continue;
                }
                if (stockQueueList.get(i).getIncomeProduct().getInvoiceId() != null)
                    if (String.valueOf(stockQueueList.get(i).getIncomeProduct().getInvoiceId()).toUpperCase().contains(searchText.toUpperCase())) {
                        searchResults.add(stockQueueList.get(i));
                        continue;
                    }
                if (String.valueOf(stockQueueList.get(i).getAvailable()).toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(stockQueueList.get(i));
                    continue;
                }
                Date date = new Date(stockQueueList.get(i).getIncomeProductDate());
                if (formatter.format(date).toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(stockQueueList.get(i));
                    continue;
                }
                Date expireDate = new Date(stockQueueList.get(i).getExpiredProductDate());
                if (formatter.format(expireDate).toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(stockQueueList.get(i));
                    continue;
                }
            }
            Collections.sort(searchResults, (o1, o2) -> o1.getExpiredProductDate().compareTo(o2.getExpiredProductDate()));
            view.initSearchResults(searchResults, searchText);
        }
    }

    @Override
    public void onWriteOff(StockQueue stockQueue) {
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setInventory(stockQueue.getAvailable());
        inventoryItem.setProduct(stockQueue.getProduct());
        inventoryItem.setVendors(databaseManager.getVendorsByProductId(stockQueue.getProductId()).blockingGet());
        inventoryItem.setLowStockAlert(0);
        view.openWriteOffDialog(inventoryItem, (inventoryItem1, outcomeProduct) -> {
            databaseManager.checkPositionAvailablity(outcomeProduct).subscribe(outcomeWithDetials -> {
                databaseManager.insertAndFillOutcomeProduct(outcomeWithDetials);
                GregorianCalendar now = new GregorianCalendar();
                stockQueueList.clear();
                databaseManager.getExpiredStockQueue().subscribe(stockQueues -> {
                    for (StockQueue queue : stockQueues) {
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime(new Date(queue.getExpiredProductDate()));
                        if (filterConfig[0] == 1 && (now.after(calendar) || now.equals(calendar))) {
                            stockQueueList.add(queue);
                            continue;
                        }
                        if (filterConfig[1] == 1 && (now.before(calendar))) {
                            stockQueueList.add(queue);
                        }
                    }
                    Collections.sort(stockQueueList, (o1, o2) -> o1.getExpiredProductDate().compareTo(o2.getExpiredProductDate()));
                    view.setExpiredProducts(stockQueueList);
                });
            });
        });
    }

    @Override
    public void onOutVoice(StockQueue stockQueue) {
        if (stockQueue.getAvailable() > 0.0009)
            view.openReturnInvoice(stockQueue.getProductId(), stockQueue.getVendorId());
        else view.openWarningDialog("This queue is not available");
    }

    @Override
    public void onFilterApplied(int[] config) {
        filterConfig = config;
        GregorianCalendar now = new GregorianCalendar();
        stockQueueList.clear();
        databaseManager.getExpiredStockQueue().subscribe(stockQueues -> {
            for (StockQueue queue : stockQueues) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(new Date(queue.getExpiredProductDate()));
                if (filterConfig[0] == 1 && (now.after(calendar) || now.equals(calendar))) {
                    stockQueueList.add(queue);
                    continue;
                }
                if (filterConfig[1] == 1 && (now.before(calendar))) {
                    stockQueueList.add(queue);
                }
            }
            Collections.sort(stockQueueList, (o1, o2) -> o1.getExpiredProductDate().compareTo(o2.getExpiredProductDate()));
            view.setExpiredProducts(stockQueueList);
        });
    }
}
