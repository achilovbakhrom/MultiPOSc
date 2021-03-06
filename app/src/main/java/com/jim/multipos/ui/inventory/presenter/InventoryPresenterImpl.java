package com.jim.multipos.ui.inventory.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.fragments.InventoryFragment;
import com.jim.multipos.ui.inventory.fragments.InventoryView;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.utils.BundleConstants;
import com.jim.multipos.utils.SurplusProductDialog;
import com.jim.multipos.utils.WriteOffProductDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.data.db.model.consignment.Consignment.INCOME_CONSIGNMENT;
import static com.jim.multipos.data.db.model.consignment.Consignment.RETURN_CONSIGNMENT;
import static com.jim.multipos.ui.inventory.fragments.InventoryFragment.SortModes.FILTERED_BY_PRODUCT;

/**
 * Created by developer on 09.11.2017.
 */

public class InventoryPresenterImpl extends BasePresenterImpl<InventoryView> implements InventoryPresenter {
    @Inject
    DatabaseManager databaseManager;

    InventoryFragment.SortModes searchMode = FILTERED_BY_PRODUCT;

    int SORTING = 1;

    List<InventoryItem> inventoryItems;
    private Long vendorId, productId;
    private int consignment_type = 0;

    @Inject
    protected InventoryPresenterImpl(InventoryView inventoryView) {
        super(inventoryView);
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);

        databaseManager.getProductInventoryStatesForNow().subscribe((inventoryItems, throwable) -> {
            this.inventoryItems = inventoryItems;
            sortList();
            view.initRecyclerView(inventoryItems);
        });
    }

    @Override
    public void onStockAlertChange(double newAlertCount, InventoryItem inventoryItem) {
        for (int i = 0; i < inventoryItems.size(); i++) {
            if (inventoryItems.get(i).getId() == inventoryItem.getId()) {
                inventoryItems.get(i).setLowStockAlert(newAlertCount);
                databaseManager.setLowStockAlert(inventoryItems.get(i),newAlertCount).subscribe();
                break;
            }
        }
    }

    @Override
    public void onIncomeProduct(InventoryItem inventoryItem) {
        view.openAddDialog(inventoryItem, (inventoryItem1, incomeProduct, stockQueue) -> {
            databaseManager.insertIncomeProduct(incomeProduct).subscribe(aLong -> {
                stockQueue.setIncomeId(incomeProduct.getId());
                databaseManager.insertStockQueue(stockQueue).subscribe();
                databaseManager.getProductInventoryStatesForNow().subscribe((inventoryItems, throwable) -> {
                    this.inventoryItems = inventoryItems;
                    sortList();
                    view.initRecyclerView(inventoryItems);
                });
            });
        });
    }

    @Override
    public void onWriteOff(InventoryItem inventoryItem) {
            view.openWriteOffDialog(inventoryItem, (inventoryItem1, outcomeProduct) -> {
                databaseManager.checkPositionAvailablity(outcomeProduct).subscribe(outcomeWithDetials -> {
                   databaseManager.insertAndFillOutcomeProduct(outcomeWithDetials);
                    databaseManager.getProductInventoryStatesForNow().subscribe((inventoryItems, throwable) -> {
                        this.inventoryItems = inventoryItems;
                        sortList();
                        view.initRecyclerView(inventoryItems);
                    });
                });
            });
    }



    @Override
    public void onConsigmentIn(InventoryItem inventoryItem) {
        consignment_type = BundleConstants.INVOICE;
        Product product = inventoryItem.getProduct();
        this.productId = product.getId();
        List<Vendor> vendorList = databaseManager.getActiveVendors().blockingGet();
        if (vendorList.size() == 0){
            view.showVendorListEmptyDialog();
            return;
        }
        List<Vendor> vendorsWithProduct = databaseManager.getVendorsByProductId(productId).blockingGet();
        view.openChooseVendorDialog(vendorList, vendorsWithProduct);
    }

    @Override
    public void onConsigmentOut(InventoryItem inventoryItem) {
        consignment_type = BundleConstants.OUTVOICE;
        Product product = inventoryItem.getProduct();
        this.productId = product.getId();
        List<Vendor> vendorsWithProduct = databaseManager.getVendorsByProductId(productId).blockingGet();
        if (vendorsWithProduct.size() == 0){
            view.noVendorsWarning();
            return;
        }
        view.openChooseVendorDialog(null, vendorsWithProduct);
    }

    List<InventoryItem> searchResults;

    @Override
    public void onSearchTyped(String searchText) {
        if (searchText.isEmpty()) {
            searchResults = null;
            sortList();
            view.initDefault(inventoryItems);
        } else {
            searchResults = new ArrayList<>();
            for (int i = 0; i < inventoryItems.size(); i++) {
                if (inventoryItems.get(i).getProduct().getName().toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(inventoryItems.get(i));
                    continue;
                }
                if (inventoryItems.get(i).getProduct().getSku().toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(inventoryItems.get(i));
                    continue;
                }
                if (inventoryItems.get(i).getProduct().getBarcode().toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(inventoryItems.get(i));
                    continue;
                }
                StringBuilder vendorsName = new StringBuilder();
                for (Vendor vendor : inventoryItems.get(i).getVendors()) {
                    if (vendorsName.length() == 0)
                        vendorsName = new StringBuilder(vendor.getName());
                    else vendorsName.append(", ").append(vendor.getName());
                }
                if (vendorsName.toString().toUpperCase().contains(searchText.toUpperCase())) {
                    searchResults.add(inventoryItems.get(i));
                }
            }
            sortList();
            view.initSearchResults(searchResults, searchText);
        }
    }

    @Override
    public void onStockQueueClick(InventoryItem inventoryItem) {
        view.openStockQueueForProduct(inventoryItem.getProduct().getId());
    }

    @Override
    public void filterBy(InventoryFragment.SortModes searchMode) {
        SORTING = 1;
        this.searchMode = searchMode;
        sortList();
        view.notifyList();
    }

    @Override
    public void filterInvert() {
        SORTING *= -1;
        sortList();
        view.notifyList();
    }

    @Override
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
        setProductId(this.productId);
    }

    @Override
    public void setProductId(Long productId) {
        view.sendDataToConsignment(productId, this.vendorId, consignment_type);
    }

    @Override
    public void updateData() {
        databaseManager.getProductInventoryStatesForNow().subscribe((inventoryItems, throwable) -> {
            this.inventoryItems = inventoryItems;
            sortList();
            view.initRecyclerView(inventoryItems);
        });
    }


    private void sortList() {

        List<InventoryItem> inventoryItemsTemp;
        if (searchResults != null) {
            inventoryItemsTemp = searchResults;
        } else {
            inventoryItemsTemp = inventoryItems;
        }
        switch (searchMode) {
            case FILTERED_BY_PRODUCT:
                Collections.sort(inventoryItemsTemp, (inventoryItem, t1) -> inventoryItem.getProduct().getName().compareTo(t1.getProduct().getName()) * SORTING);
                break;
            case FILTERED_BY_VENDOR:
                Collections.sort(inventoryItemsTemp, (inventoryItem, t1) -> {
                    StringBuilder vendorsName = new StringBuilder();
                    for (Vendor vendor : inventoryItem.getVendors()) {
                        if (vendorsName.length() == 0)
                            vendorsName = new StringBuilder(vendor.getName());
                        else vendorsName.append(", ").append(vendor.getName());
                    }
                    StringBuilder vendorsName2 = new StringBuilder();
                    for (Vendor vendor : t1.getVendors()) {
                        if (vendorsName2.length() == 0)
                            vendorsName2 = new StringBuilder(vendor.getName());
                        else vendorsName2.append(", ").append(vendor.getName());
                    }
                    return vendorsName.toString().compareTo(vendorsName2.toString()) * SORTING;
                });
                break;
            case FILTERED_BY_UNIT:
                Collections.sort(inventoryItemsTemp, (inventoryItem, t1) -> t1.getProduct().getMainUnit().getAbbr().compareTo(inventoryItem.getProduct().getMainUnit().getAbbr()) * SORTING);
                break;
            case FILTERED_BY_LOWSTOCK:
                Collections.sort(inventoryItemsTemp, (inventoryItem, t1) -> t1.getLowStockAlert().compareTo(inventoryItem.getLowStockAlert()) * SORTING);
                break;
            case FILTERED_BY_INVENTORY:
                Collections.sort(inventoryItemsTemp, (inventoryItem, t1) -> t1.getInventory().compareTo(inventoryItem.getInventory()) * SORTING);
                break;

        }

    }
}
