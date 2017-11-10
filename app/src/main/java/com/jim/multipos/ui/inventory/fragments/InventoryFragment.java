package com.jim.multipos.ui.inventory.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.inventory.adapters.InventoryItemAdapter;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.inventory.presenter.InventoryPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by developer on 09.11.2017.
 */

public class InventoryFragment extends BaseFragment implements InventoryView{
    @BindView(R.id.rvInventory)
    RecyclerView rvInventory;
    @Inject
    InventoryPresenter presenter;
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
        inventoryItemAdapter = new InventoryItemAdapter(inventoryItemList);
        rvInventory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvInventory.setAdapter(inventoryItemAdapter);
    }
}
