package com.jim.multipos.ui.inventory.presenter;

import android.os.Bundle;
import android.util.Log;

import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.inventory.fragments.InventoryView;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by developer on 09.11.2017.
 */

public class InventoryPresenterImpl extends BasePresenterImpl<InventoryView> implements InventoryPresenter {
    @Inject
    DatabaseManager databaseManager;
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
            view.initRecyclerView(inventoryItems);
        });
    }
}
