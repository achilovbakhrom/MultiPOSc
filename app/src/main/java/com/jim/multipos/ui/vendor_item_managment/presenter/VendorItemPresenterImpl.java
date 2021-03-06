package com.jim.multipos.ui.vendor_item_managment.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment;
import com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemView;
import com.jim.multipos.ui.vendor_item_managment.model.VendorManagmentItem;
import com.jim.multipos.utils.BundleConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.data.db.model.consignment.Consignment.INCOME_CONSIGNMENT;
import static com.jim.multipos.data.db.model.consignment.Consignment.RETURN_CONSIGNMENT;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.VENDOR;


/**
 * Created by developer on 20.11.2017.
 */

public class VendorItemPresenterImpl extends BasePresenterImpl<VendorItemView> implements VendorItemPresenter {
    @Inject
    DatabaseManager databaseManager;

    VendorItemFragment.SortModes searchMode = VENDOR;
    private Long vendorId;

    int SORTING = 1;

    List<VendorManagmentItem> vendorManagmentItems;
    @Inject
    protected VendorItemPresenterImpl(VendorItemView vendorItemView){
        super(vendorItemView);
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        databaseManager.getVendorItemManagmentItem().subscribe((vendorWithDebts, throwable) -> {
           this.vendorManagmentItems = vendorWithDebts;
           sortList();
           view.initRecyclerView(vendorWithDebts);
        });
    }

    @Override
    public void onIncomeProduct(VendorManagmentItem vendorManagmentItem) {
        vendorId = vendorManagmentItem.getVendor().getId();
        view.sendDataToConsignment(vendorId, BundleConstants.INVOICE);
    }

    @Override
    public void onWriteOff(VendorManagmentItem vendorManagmentItem) {
        vendorId = vendorManagmentItem.getVendor().getId();
        view.sendDataToConsignment(vendorId, BundleConstants.OUTVOICE);
    }

    @Override
    public void onConsigmentStory(VendorManagmentItem vendorManagmentItem) {
        vendorId = vendorManagmentItem.getVendor().getId();
        view.openVendorConsignmentsStory(vendorId);
    }

    @Override
    public void onPay(VendorManagmentItem vendorManagmentItem) {
        view.openPaymentDialog(databaseManager, vendorManagmentItem.getVendor());
    }

    @Override
    public void onPayStory(VendorManagmentItem vendorManagmentItem, Double totalDebt) {
        view.openVendorBillingStory(vendorManagmentItem.getVendor().getId(),totalDebt);
    }

    @Override
    public void onMore(VendorManagmentItem vendorManagmentItem) {
        vendorId = vendorManagmentItem.getVendor().getId();
        view.openVendorDetails(vendorId);
    }

    @Override
    public void onStockQueueForVendor(VendorManagmentItem vendorManagmentItem) {
        view.openStockQueueForVendor(vendorManagmentItem.getVendor().getId());
    }

    List<VendorManagmentItem> searchResults;

    @Override
    public void onSearchTyped(String searchText) {
        if(searchText.isEmpty()){
            searchResults = null;
            sortList();
            view.initDefault(vendorManagmentItems);
        }else {
            searchResults = new ArrayList<>();
            for(int i = 0; i< vendorManagmentItems.size(); i++){
                if(vendorManagmentItems.get(i).getVendor().getName().toUpperCase().contains(searchText.toUpperCase())){
                    searchResults.add(vendorManagmentItems.get(i));
                    continue;
                }
                if(vendorManagmentItems.get(i).getVendor().getContactName().toUpperCase().contains(searchText.toUpperCase())){
                    searchResults.add(vendorManagmentItems.get(i));
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (Product product: vendorManagmentItems.get(i).getProducts()) {
                    builder.append(product.getName());
                    builder.append(",");
                    builder.append(" ");
                }
                if(builder.toString().length()>2) {
                    builder.deleteCharAt(builder.length() - 1);
                    builder.deleteCharAt(builder.length() - 1);
                }
                if(builder.toString().toUpperCase().contains(searchText.toUpperCase())){
                    searchResults.add(vendorManagmentItems.get(i));
                }

            }
            sortList();
            view.initSearchResults(searchResults,searchText);
        }
    }

    @Override
    public void filterBy(VendorItemFragment.SortModes searchMode) {
        this.searchMode = searchMode;
        SORTING =1;
        sortList();
        view.notifyList();
    }

    @Override
    public void filterInvert() {
        SORTING *=-1;
        sortList();
        view.notifyList();
    }

    @Override
    public void updateData() {
        databaseManager.getVendorItemManagmentItem().subscribe((vendorWithDebts, throwable) -> {
            this.vendorManagmentItems = vendorWithDebts;
            sortList();
            view.initRecyclerView(vendorWithDebts);
        });
    }

    private void sortList(){

        List<VendorManagmentItem> vendorWithDebtsTemp;
        if(searchResults !=null){
            vendorWithDebtsTemp = searchResults;
        }else {
            vendorWithDebtsTemp = vendorManagmentItems;
        }

        switch (searchMode){
            case DEBT:
                Collections.sort(vendorWithDebtsTemp,(vendorWithDebt, t1) -> t1.getDebt().compareTo(vendorWithDebt.getDebt())*SORTING);
                break;
            case PRODUCTS:
                Collections.sort(vendorWithDebtsTemp,(vendorWithDebt, t1) -> {
                    Integer size = vendorWithDebt.getProducts().size();
                    Integer size2 = t1.getProducts().size();
                    return size2.compareTo(size)*SORTING;
                });
                break;
            case VENDOR:
                Collections.sort(vendorWithDebtsTemp,(vendorWithDebt, t1) -> vendorWithDebt.getVendor().getName().compareTo(t1.getVendor().getName())*SORTING);
                break;

        }

    }

}
