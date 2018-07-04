package com.jim.multipos.ui.product_queue_list.product_queue;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import org.reactivestreams.Subscription;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

public class ProductQueueListFragmentPresenterImpl extends BasePresenterImpl<ProductQueueListFragmentView> implements ProductQueueListFragmentPresenter {

    private DatabaseManager databaseManager;
    private List<StockQueue> stockQueueList, searchResults;
    private Calendar fromDate, toDate;
    private SimpleDateFormat simpleDateFormat;
    private Long productId;
    private Long vendorId;
    private int[] filterConfig;

    @Inject
    protected ProductQueueListFragmentPresenterImpl(ProductQueueListFragmentView productQueueListFragmentView, DatabaseManager databaseManager) {
        super(productQueueListFragmentView);
        this.databaseManager = databaseManager;
        stockQueueList = new ArrayList<>();
        filterConfig = new int[]{1, 1};
    }

    @Override
    public void initData(Long productId, Long vendorId) {
        this.productId = productId;
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
        initStockQueueList();
    }

    private void initStockQueueList() {
        stockQueueList.clear();
        if (productId != null) {
            databaseManager.getAllStockQueuesByProductIdInInterval(productId, fromDate, toDate).subscribe(stockQueues -> {
                for (StockQueue stockQueue : stockQueues) {
                    if (filterConfig[0] == 1 && stockQueue.getAvailable() > 0.0009) {
                        this.stockQueueList.add(stockQueue);
                        continue;
                    }
                    if (filterConfig[1] == 1 && stockQueue.getAvailable() <= 0.0009) {
                        this.stockQueueList.add(stockQueue);
                    }
                }
                view.fillRecyclerView(stockQueueList);
            });
            view.setTitle(databaseManager.getProductById(productId).blockingSingle().getName());
        } else if (vendorId != null) {
            databaseManager.getAllStockQueuesByVendorIdInInterval(vendorId, fromDate, toDate).subscribe(stockQueues -> {
                for (StockQueue stockQueue : stockQueues) {
                    if (filterConfig[0] == 1 && stockQueue.getAvailable() > 0.0009) {
                        this.stockQueueList.add(stockQueue);
                        continue;
                    }
                    if (filterConfig[1] == 1 && stockQueue.getAvailable() <= 0.0009) {
                        this.stockQueueList.add(stockQueue);
                    }
                }
                view.fillRecyclerView(stockQueueList);
            });
            view.setTitle(databaseManager.getVendorById(vendorId).blockingSingle().getName());
        }
        view.updateDateIntervalUI(simpleDateFormat.format(fromDate.getTimeInMillis()) + " - " + simpleDateFormat.format(toDate.getTimeInMillis()));
    }

    @Override
    public void search(String searchText) {
        if (searchText.isEmpty()) {
            searchResults = null;
            view.fillRecyclerView(stockQueueList);
        } else {
            searchResults = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm MM/dd/yyyy");
            for (int i = 0; i < stockQueueList.size(); i++) {
                if (stockQueueList.get(i).getVendor().getName().toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(stockQueueList.get(i));
                    continue;
                }
                if (stockQueueList.get(i).getIncomeProduct().getInvoiceId() != null)
                    if (String.valueOf(stockQueueList.get(i).getIncomeProduct().getInvoiceId()).toUpperCase().contains(searchText.toUpperCase())) {
                        searchResults.add(stockQueueList.get(i));
                        continue;
                    }
                if (String.valueOf(stockQueueList.get(i).getCost()).toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(stockQueueList.get(i));
                    continue;
                }
                if (String.valueOf(stockQueueList.get(i).getAvailable()).toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(stockQueueList.get(i));
                    continue;
                }
                if (String.valueOf(stockQueueList.get(i).getStockId()).toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(stockQueueList.get(i));
                    continue;
                }
                Date date = new Date(stockQueueList.get(i).getIncomeProductDate());
                if (formatter.format(date).toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(stockQueueList.get(i));
                    continue;
                }
            }
            view.initSearchResults(searchResults, searchText);
        }
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
    public void dateIntervalPicked(Calendar fromDate1, Calendar toDate1) {
        this.fromDate = fromDate1;
        this.toDate = toDate1;
        initStockQueueList();
    }

    @Override
    public void onFilterApplied(int[] config) {
        filterConfig = config;
        initStockQueueList();
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
                initStockQueueList();
            });
        });
    }

    @Override
    public void onOutVoice(StockQueue stockQueue) {
        if (stockQueue.getAvailable() > 0.0009)
            view.openReturnInvoice(stockQueue.getProductId(), stockQueue.getVendorId());
        else view.openWarningDialog("This queue is not available");
    }
}
