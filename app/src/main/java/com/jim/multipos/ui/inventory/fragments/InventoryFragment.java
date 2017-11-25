package com.jim.multipos.ui.inventory.fragments;

import android.Manifest;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.inventory.adapters.InventoryItemAdapter;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.inventory.presenter.InventoryPresenter;
import com.jim.multipos.utils.SurplusProductDialog;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.WriteOffProductDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jim.multipos.ui.inventory.fragments.InventoryFragment.SortModes.*;

/**
 * Created by developer on 09.11.2017.
 */

public class InventoryFragment extends BaseFragment implements InventoryView{
    @BindView(R.id.rvInventory)
    RecyclerView rvInventory;

    @BindView(R.id.llProduct)
    LinearLayout llProduct;
    @BindView(R.id.llVendor)
    LinearLayout llVendor;
    @BindView(R.id.llLowStockAlert)
    LinearLayout llLowStockAlert;
    @BindView(R.id.llInventory)
    LinearLayout llInventory;
    @BindView(R.id.llUnit)
    LinearLayout llUnit;

    @BindView(R.id.ivProductSort)
    ImageView ivProductSort;
    @BindView(R.id.ivVendorSort)
    ImageView ivVendorSort;
    @BindView(R.id.ivLowStockAlertSort)
    ImageView ivLowStockAlertSort;
    @BindView(R.id.ivInventorySort)
    ImageView ivInventorySort;
    @BindView(R.id.ivUnitSort)
    ImageView ivUnitSort;

    SortModes filterMode = FILTERED_BY_PRODUCT;
    @Inject
    DecimalFormat decimalFormat;
    public enum SortModes{
        FILTERED_BY_PRODUCT,FILTERED_BY_PRODUCT_INVERT,FILTERED_BY_VENDOR,FILTERED_BY_VENDOR_INVERT,FILTERED_BY_LOWSTOCK,FILTERED_BY_LOWSTOCK_INVERT,FILTERED_BY_INVENTORY,FILTERED_BY_INVENTORY_INVERT,FILTERED_BY_UNIT,FILTERED_BY_UNIT_INVERT
    }

    @Inject
    InventoryPresenter presenter;
    @Inject
    RxPermissions rxPermissions;
    InventoryItemAdapter inventoryItemAdapter;

    @Override
    protected int getLayout() {
        return R.layout.inventory_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.onCreateView(savedInstanceState);
    }

    @Override
    public void initRecyclerView(List<InventoryItem> inventoryItemList) {
        inventoryItemAdapter = new InventoryItemAdapter(inventoryItemList, new InventoryItemAdapter.OnInvendoryAdapterCallback() {
            @Override
            public void onStockAlertChange(double newAlertCount, InventoryItem inventoryItem) {
                presenter.onStockAlertChange(newAlertCount,inventoryItem);
            }

            @Override
            public void onIncomeProduct(InventoryItem inventoryItem) {
                presenter.onIncomeProduct(inventoryItem);
            }

            @Override
            public void onWriteOff(InventoryItem inventoryItem) {
                presenter.onWriteOff(inventoryItem);
            }

            @Override
            public void onSetActually(InventoryItem inventoryItem) {
                presenter.onSetActually(inventoryItem);
            }

            @Override
            public void onConsigmentIn(InventoryItem inventoryItem) {
                presenter.onConsigmentIn(inventoryItem);
            }

            @Override
            public void onConsigmentOut(InventoryItem inventoryItem) {
                presenter.onConsigmentOut(inventoryItem);
            }
        },getActivity());
        rvInventory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvInventory.setAdapter(inventoryItemAdapter);
        ivProductSort.setVisibility(View.VISIBLE);

        llProduct.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != FILTERED_BY_PRODUCT){
                filterMode = FILTERED_BY_PRODUCT;
                presenter.filterBy(FILTERED_BY_PRODUCT);
                ivProductSort.setVisibility(View.VISIBLE);
                ivProductSort.setImageResource(R.drawable.sorting);
            }
            else {
                filterMode = FILTERED_BY_PRODUCT_INVERT;
                ivProductSort.setVisibility(View.VISIBLE);
                ivProductSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llVendor.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != FILTERED_BY_VENDOR){
                filterMode = FILTERED_BY_VENDOR;
                presenter.filterBy(FILTERED_BY_VENDOR);
                ivVendorSort.setVisibility(View.VISIBLE);
                ivVendorSort.setImageResource(R.drawable.sorting);
            }else {
                filterMode = FILTERED_BY_VENDOR_INVERT;
                ivVendorSort.setVisibility(View.VISIBLE);
                ivVendorSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llLowStockAlert.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != FILTERED_BY_LOWSTOCK){
                filterMode = FILTERED_BY_LOWSTOCK;
                presenter.filterBy(FILTERED_BY_LOWSTOCK);
                ivLowStockAlertSort.setVisibility(View.VISIBLE);
                ivLowStockAlertSort.setImageResource(R.drawable.sorting);
            }else {
                filterMode = FILTERED_BY_LOWSTOCK_INVERT;
                ivLowStockAlertSort.setVisibility(View.VISIBLE);
                ivLowStockAlertSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }

        });
        llUnit.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != FILTERED_BY_UNIT){
                filterMode = FILTERED_BY_UNIT;
                presenter.filterBy(FILTERED_BY_UNIT);
                ivUnitSort.setVisibility(View.VISIBLE);
                ivUnitSort.setImageResource(R.drawable.sorting);
            }else {
                filterMode = FILTERED_BY_UNIT_INVERT;
                ivUnitSort.setVisibility(View.VISIBLE);
                ivUnitSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llInventory.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != FILTERED_BY_INVENTORY){
                filterMode = FILTERED_BY_INVENTORY;
                presenter.filterBy(FILTERED_BY_INVENTORY);
                ivInventorySort.setVisibility(View.VISIBLE);
                ivInventorySort.setImageResource(R.drawable.sorting);
            }else {
                filterMode = FILTERED_BY_INVENTORY_INVERT;
                ivInventorySort.setVisibility(View.VISIBLE);
                ivInventorySort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });

    }

    @Override
    public void initSearchResults(List<InventoryItem> inventoryItems, String searchText) {
        inventoryItemAdapter.setSearchResult(inventoryItems,searchText);
        inventoryItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void initDefault(List<InventoryItem> inventoryItems) {
        inventoryItemAdapter.setData(inventoryItems);
        inventoryItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyList() {
        inventoryItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeKeyboard() {
        UIUtils.closeKeyboard(llUnit,getActivity());

    }

    @Override
    public void openWriteOffDialog(InventoryItem inventoryItem, WriteOffProductDialog.WriteOffCallback writeOffCallback) {
        WriteOffProductDialog writeOffProductDialog = new WriteOffProductDialog(getActivity(),writeOffCallback,inventoryItem,decimalFormat);
        writeOffProductDialog.show();
    }

    @Override
    public void openAddDialog(InventoryItem inventoryItem, SurplusProductDialog.SurplusCallback surplusCallback) {
        SurplusProductDialog surplusProductDialog = new SurplusProductDialog(getActivity(),surplusCallback,inventoryItem,decimalFormat);
        surplusProductDialog.show();
    }


    public void searchText(String searchText){
        presenter.onSearchTyped(searchText);
    }
    private void deselectAll(){
        ivProductSort.setVisibility(View.GONE);
        ivInventorySort.setVisibility(View.GONE);
        ivLowStockAlertSort.setVisibility(View.GONE);
        ivUnitSort.setVisibility(View.GONE);
        ivVendorSort.setVisibility(View.GONE);
    }
}
