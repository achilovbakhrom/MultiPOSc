package com.jim.multipos.ui.inventory.presenter;

import android.os.Bundle;
import android.util.Log;

import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.fragments.InventoryFragment;
import com.jim.multipos.ui.inventory.fragments.InventoryView;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.utils.CommonUtils;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.WriteOffProductDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.ui.inventory.fragments.InventoryFragment.SortModes.*;

/**
 * Created by developer on 09.11.2017.
 */

public class InventoryPresenterImpl extends BasePresenterImpl<InventoryView> implements InventoryPresenter {
    @Inject
    DatabaseManager databaseManager;

    InventoryFragment.SortModes searchMode = FILTERED_BY_PRODUCT;

    int SORTING = 1;

    List<InventoryItem> inventoryItems;
    @Inject
    protected InventoryPresenterImpl(InventoryView inventoryView) {
        super(inventoryView);
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);

        databaseManager.getInventoryItems().subscribe((inventoryItems, throwable) -> {
            this.inventoryItems = inventoryItems;
            sortList();
            view.initRecyclerView(inventoryItems);
        });
    }

    @Override
    public void onStockAlertChange(double newAlertCount, InventoryItem inventoryItem) {
        for (int i = 0; i < inventoryItems.size(); i++) {
            if(inventoryItems.get(i).getId() == inventoryItem.getId()){
                inventoryItems.get(i).setLowStockAlert(newAlertCount);
                break;
            }
        }
    }

    @Override
    public void onIncomeProduct(InventoryItem inventoryItem) {
        view.openAddDialog(inventoryItem,(inventoryItem1, vendor, v, etReason) -> {
            List<InventoryItem> inventoryItemsTemp;
            if(searchResults !=null){
                inventoryItemsTemp = searchResults;
            }else {
                inventoryItemsTemp = inventoryItems;
            }
            for (int i=0;i<inventoryItemsTemp.size();i++) {
                if(inventoryItemsTemp.get(i).getProduct().getId().equals(inventoryItem1.getProduct().getId())){
                    inventoryItemsTemp.get(i).setInventory(v);
                    break;
                }
            }
            view.notifyList();

        });
    }

    @Override
    public void onWriteOff(InventoryItem inventoryItem) {
        view.openWriteOffDialog(inventoryItem, (inventoryItem1, vendor, v, etReason) -> {
            List<InventoryItem> inventoryItemsTemp;
            if(searchResults !=null){
                inventoryItemsTemp = searchResults;
            }else {
                inventoryItemsTemp = inventoryItems;
            }
            for (int i=0;i<inventoryItemsTemp.size();i++) {
                if(inventoryItemsTemp.get(i).getProduct().getId().equals(inventoryItem1.getProduct().getId())){
                    inventoryItemsTemp.get(i).setInventory(v);
                    break;
                }
            }
            view.notifyList();

        });
    }

    @Override
    public void onSetActually(InventoryItem inventoryItem) {

    }

    @Override
    public void onConsigmentIn(InventoryItem inventoryItem) {

    }

    @Override
    public void onConsigmentOut(InventoryItem inventoryItem) {

    }
    List<InventoryItem> searchResults;
    @Override
    public void onSearchTyped(String searchText) {
        if(searchText.isEmpty()){
            searchResults = null;
            sortList();
            view.initDefault(inventoryItems);
        }else {
            searchResults = new ArrayList<>();
            for(int i = 0;i<inventoryItems.size();i++){
                if(inventoryItems.get(i).getProduct().getName().toUpperCase().contains(searchText.toUpperCase())){
                    searchResults.add(inventoryItems.get(i));
                    continue;
                }
                if(inventoryItems.get(i).getProduct().getSku().toUpperCase().contains(searchText.toUpperCase())){
                    searchResults.add(inventoryItems.get(i));
                    continue;
                }
                if(inventoryItems.get(i).getProduct().getBarcode().toUpperCase().contains(searchText.toUpperCase())){
                    searchResults.add(inventoryItems.get(i));
                    continue;
                }
                StringBuilder vendorsName = new StringBuilder();
                for (Vendor vendor : inventoryItems.get(i).getProduct().getVendor()) {
                    if (vendorsName.length() == 0)
                        vendorsName = new StringBuilder(vendor.getName());
                    else vendorsName.append(", ").append(vendor.getName());
                }
                if(vendorsName.toString().toUpperCase().contains(searchText.toUpperCase())){
                    searchResults.add(inventoryItems.get(i));
                }
            }
            sortList();
            view.initSearchResults(searchResults,searchText);
        }
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
        SORTING *=-1;
        sortList();
        view.notifyList();
    }



    private void sortList(){

       List<InventoryItem> inventoryItemsTemp;
       if(searchResults !=null){
            inventoryItemsTemp = searchResults;
        }else {
            inventoryItemsTemp = inventoryItems;
        }
        switch (searchMode){
            case FILTERED_BY_PRODUCT:
                Collections.sort(inventoryItemsTemp,(inventoryItem, t1) -> inventoryItem.getProduct().getName().compareTo(t1.getProduct().getName())*SORTING);
                break;
            case FILTERED_BY_VENDOR:
                Collections.sort(inventoryItemsTemp,(inventoryItem, t1) -> {
                    StringBuilder vendorsName = new StringBuilder();
                    for (Vendor vendor : inventoryItem.getProduct().getVendor()) {
                        if (vendorsName.length() == 0)
                            vendorsName = new StringBuilder(vendor.getName());
                        else vendorsName.append(", ").append(vendor.getName());
                    }
                    StringBuilder vendorsName2 = new StringBuilder();
                    for (Vendor vendor : t1.getProduct().getVendor()) {
                        if (vendorsName2.length() == 0)
                            vendorsName2 = new StringBuilder(vendor.getName());
                        else vendorsName2.append(", ").append(vendor.getName());
                    }
                    return vendorsName.toString().compareTo(vendorsName2.toString())*SORTING;
                });
                break;
            case FILTERED_BY_UNIT:
                Collections.sort(inventoryItemsTemp,(inventoryItem, t1) -> t1.getProduct().getMainUnit().getAbbr().compareTo(inventoryItem.getProduct().getMainUnit().getAbbr())*SORTING);
                break;
            case FILTERED_BY_LOWSTOCK:
                Collections.sort(inventoryItemsTemp,(inventoryItem, t1) -> t1.getLowStockAlert().compareTo(inventoryItem.getLowStockAlert())*SORTING);
                break;
            case FILTERED_BY_INVENTORY:
                Collections.sort(inventoryItemsTemp,(inventoryItem, t1) -> t1.getInventory().compareTo(inventoryItem.getInventory())*SORTING);
                break;

        }

    }
}
