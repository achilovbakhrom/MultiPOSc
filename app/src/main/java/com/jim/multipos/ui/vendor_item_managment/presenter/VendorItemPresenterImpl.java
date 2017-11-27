package com.jim.multipos.ui.vendor_item_managment.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment;
import com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemView;
import com.jim.multipos.ui.vendor_item_managment.model.VendorWithDebt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.VENDOR;


/**
 * Created by developer on 20.11.2017.
 */

public class VendorItemPresenterImpl extends BasePresenterImpl<VendorItemView> implements VendorItemPresenter {
    @Inject
    DatabaseManager databaseManager;

    VendorItemFragment.SortModes searchMode = VENDOR;
    private static final int INCOME = 0;
    private static final int RETURN = 1;
    private Long vendorId;
    private int consignment_type = 0;

    int SORTING = 1;

    List<VendorWithDebt> vendorWithDebts;
    @Inject
    protected VendorItemPresenterImpl(VendorItemView vendorItemView){
        super(vendorItemView);
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        databaseManager.getVendorWirhDebt().subscribe((vendorWithDebts, throwable) -> {
           this.vendorWithDebts = vendorWithDebts;
           sortList();
           view.initRecyclerView(vendorWithDebts);
        });
    }

    @Override
    public void onIncomeProduct(VendorWithDebt vendorWithDebt) {
        consignment_type = INCOME;
        vendorId = vendorWithDebt.getVendor().getId();
        view.sendDataToConsignment(vendorId, consignment_type);
    }

    @Override
    public void onWriteOff(VendorWithDebt vendorWithDebt) {
        consignment_type = RETURN;
        vendorId = vendorWithDebt.getVendor().getId();
        view.sendDataToConsignment(vendorId, consignment_type);
    }

    @Override
    public void onConsigmentStory(VendorWithDebt vendorWithDebt) {

    }

    @Override
    public void onPay(VendorWithDebt vendorWithDebt) {

    }

    @Override
    public void onPayStory(VendorWithDebt vendorWithDebt) {

    }

    @Override
    public void onMore(VendorWithDebt vendorWithDebt) {
        vendorId = vendorWithDebt.getVendor().getId();
        view.openVendorDetails(vendorId);
    }

    List<VendorWithDebt> searchResults;

    @Override
    public void onSearchTyped(String searchText) {
        if(searchText.isEmpty()){
            searchResults = null;
            sortList();
            view.initDefault(vendorWithDebts);
        }else {
            searchResults = new ArrayList<>();
            for(int i = 0;i<vendorWithDebts.size();i++){
                if(vendorWithDebts.get(i).getVendor().getName().toUpperCase().contains(searchText.toUpperCase())){
                    searchResults.add(vendorWithDebts.get(i));
                    continue;
                }
                if(vendorWithDebts.get(i).getVendor().getContactName().toUpperCase().contains(searchText.toUpperCase())){
                    searchResults.add(vendorWithDebts.get(i));
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (Product product:vendorWithDebts.get(i).getVendor().getProducts()) {
                    builder.append(product.getName());
                    builder.append(",");
                    builder.append(" ");
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.deleteCharAt(builder.length() - 1);
                if(builder.toString().toUpperCase().contains(searchText.toUpperCase())){
                    searchResults.add(vendorWithDebts.get(i));
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


    private void sortList(){

        List<VendorWithDebt> vendorWithDebtsTemp;
        if(searchResults !=null){
            vendorWithDebtsTemp = searchResults;
        }else {
            vendorWithDebtsTemp = vendorWithDebts;
        }

        switch (searchMode){
            case DEBT:
                Collections.sort(vendorWithDebtsTemp,(vendorWithDebt, t1) -> t1.getDebt().compareTo(vendorWithDebt.getDebt())*SORTING);
                break;
            case PRODUCTS:
                Collections.sort(vendorWithDebtsTemp,(vendorWithDebt, t1) -> {
                    Integer size = vendorWithDebt.getVendor().getProducts().size();
                    Integer size2 = t1.getVendor().getProducts().size();
                    return size2.compareTo(size)*SORTING;
                });
                break;
            case VENDOR:
                Collections.sort(vendorWithDebtsTemp,(vendorWithDebt, t1) -> vendorWithDebt.getVendor().getName().compareTo(t1.getVendor().getName())*SORTING);
                break;

        }

    }

}
