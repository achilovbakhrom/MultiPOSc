package com.jim.multipos.ui.inventory.fragments;

import android.Manifest;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    @BindView(R.id.tvProduct)
    TextView tvProduct;
    @BindView(R.id.tvVendor)
    TextView tvVendor;
    @BindView(R.id.tvLowStockAlert)
    TextView tvLowStockAlert;
    @BindView(R.id.tvInventory)
    TextView tvInventory;
    @BindView(R.id.tvUnit)
    TextView tvUnit;

    SortModes filterMode ;
    @Inject
    DecimalFormat decimalFormat;
   public enum SortModes{
        DEFAULT,FILTERED_BY_PRODUCT,FILTERED_BY_VENDOR,FILTERED_BY_LOWSTOCK,FILTERED_BY_INVENTORY,FILTERED_BY_UNIT
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

        tvProduct.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != FILTERED_BY_PRODUCT){
                filterMode = FILTERED_BY_PRODUCT;
                presenter.filterBy(FILTERED_BY_PRODUCT);
                tvProduct.setTypeface(Typeface.create(tvProduct.getTypeface(), Typeface.BOLD));
            }
            else {
                filterMode = DEFAULT;
                presenter.filterCancel();
            }
        });
        tvVendor.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != FILTERED_BY_VENDOR){
                filterMode = FILTERED_BY_VENDOR;
                presenter.filterBy(FILTERED_BY_VENDOR);
                tvVendor.setTypeface(Typeface.create(tvVendor.getTypeface(), Typeface.BOLD));
            }else {
                filterMode = DEFAULT;
                presenter.filterCancel();
            }
        });
        tvLowStockAlert.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != FILTERED_BY_LOWSTOCK){
                filterMode = FILTERED_BY_LOWSTOCK;
                presenter.filterBy(FILTERED_BY_LOWSTOCK);
                tvLowStockAlert.setTypeface(Typeface.create(tvLowStockAlert.getTypeface(), Typeface.BOLD));
            }else {
                filterMode = DEFAULT;
                presenter.filterCancel();
            }

        });
        tvUnit.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != FILTERED_BY_UNIT){
                filterMode = FILTERED_BY_UNIT;
                presenter.filterBy(FILTERED_BY_UNIT);
                tvUnit.setTypeface(Typeface.create(tvUnit.getTypeface(), Typeface.BOLD));
            }else {
                filterMode = DEFAULT;
                presenter.filterCancel();
            }
        });
        tvInventory.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != FILTERED_BY_INVENTORY){
                filterMode = FILTERED_BY_INVENTORY;
                presenter.filterBy(FILTERED_BY_INVENTORY);
                tvInventory.setTypeface(Typeface.create(tvInventory.getTypeface(), Typeface.BOLD));
            }else {
                filterMode = DEFAULT;
                presenter.filterCancel();
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
        UIUtils.closeKeyboard(tvUnit,getActivity());

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
        tvProduct.setTypeface(Typeface.create(tvProduct.getTypeface(), Typeface.NORMAL));
        tvInventory.setTypeface(Typeface.create(tvInventory.getTypeface(), Typeface.NORMAL));
        tvLowStockAlert.setTypeface(Typeface.create(tvLowStockAlert.getTypeface(), Typeface.NORMAL));
        tvUnit.setTypeface(Typeface.create(tvUnit.getTypeface(), Typeface.NORMAL));
        tvVendor.setTypeface(Typeface.create(tvVendor.getTypeface(), Typeface.NORMAL));
    }
}
