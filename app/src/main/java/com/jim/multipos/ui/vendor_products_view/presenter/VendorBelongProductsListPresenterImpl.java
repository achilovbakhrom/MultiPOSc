package com.jim.multipos.ui.vendor_products_view.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorBelongProductsListView;
import com.jim.multipos.ui.vendor_products_view.model.ProductState;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.ui.inventory.InventoryActivity.VENDOR_ID;

public class VendorBelongProductsListPresenterImpl extends BasePresenterImpl<VendorBelongProductsListView> implements VendorBelongProductsListPresenter{

    private DatabaseManager databaseManager;
    private DecimalFormat decimalFormat;
    List<ProductState> items;
    Vendor vendor;
    @Inject
    public VendorBelongProductsListPresenterImpl(VendorBelongProductsListView view, DatabaseManager databaseManager, DecimalFormat decimalFormat) {
        super(view);
        this.databaseManager = databaseManager;
        this.decimalFormat = decimalFormat;
        items = new ArrayList<>();
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        if(bundle!=null) {
            databaseManager.getVendorById(bundle.getLong(VENDOR_ID)).subscribe(vendor1 -> {
                vendor = vendor1;
                databaseManager.getVendorProductsWithStates(vendor.getId()).subscribe(productStates -> {
                    items = productStates;
                    view.initRecyclerView(items);
                });
            });

        }
    }

    @Override
    public void onInvoiceClick(ProductState state) {
        view.openVendorInvoiceWithProduct(vendor,state.getProduct());
    }

    @Override
    public void onOutvoiceClick(ProductState state) {
        view.openVendorOutvoiceWithProduct(vendor,state.getProduct());
    }

    List<ProductState> searchResults;
    String textSearched = "";
    @Override
    public void searchText(String textSearched) {
        if(textSearched.isEmpty()){
            searchResults = null;
            this.textSearched = "";
            view.updateProductStates(items);
        }else {
            searchResults = new ArrayList<>();
            this.textSearched = textSearched;
            for(int i = 0; i< items.size(); i++){
                if(items.get(i).getProduct().getName().toUpperCase().contains(textSearched.toUpperCase())){
                    searchResults.add(items.get(i));
                    continue;
                }
                if(items.get(i).getProduct().getSku().toUpperCase().contains(textSearched.toUpperCase())){
                    searchResults.add(items.get(i));
                    continue;
                }
                if(items.get(i).getProduct().getBarcode().toUpperCase().contains(textSearched.toUpperCase())){
                    searchResults.add(items.get(i));
                    continue;
                }
                if(decimalFormat.format(items.get(i).getValue()).contains(textSearched.toUpperCase())){
                    searchResults.add(items.get(i));
                    continue;
                }
                if(items.get(i).getProduct().getMainUnit().getAbbr().toUpperCase().contains(textSearched.toUpperCase())){
                    searchResults.add(items.get(i));
                    continue;
                }

            }
            view.initSearchResults(searchResults,textSearched);
        }
    }

    @Override
    public void updateVendorDetials(Vendor updated) {
        if(vendor.getId().equals(updated.getId())){
                databaseManager.getVendorProductsWithStates(vendor.getId()).subscribe(productStates -> {
                    items = productStates;
                    if(searchResults !=null){
                        searchText(textSearched);
                    }else {
                        view.updateProductStates(items);
                    }
                });
        }
    }
}
