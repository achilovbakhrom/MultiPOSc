package com.jim.multipos.ui.inventory;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class InventoryActivityPresenterImpl extends BasePresenterImpl<InventoryActivityView> implements InventoryActivityPresenter {

    @Inject
    protected InventoryActivityPresenterImpl(InventoryActivityView inventoryActivityView) {
        super(inventoryActivityView);
    }
}
