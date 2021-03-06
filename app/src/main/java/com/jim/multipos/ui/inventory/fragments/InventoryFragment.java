package com.jim.multipos.ui.inventory.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.InventoryActivity;
import com.jim.multipos.ui.inventory.adapters.InventoryItemAdapter;
import com.jim.multipos.ui.inventory.dialogs.VendorPickerDialog;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.inventory.presenter.InventoryPresenter;
import com.jim.multipos.utils.BarcodeStack;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.SurplusProductDialog;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.WriteOffProductDialog;
import com.jim.multipos.utils.rxevents.inventory_events.ConsignmentWithVendorEvent;
import com.jim.multipos.utils.rxevents.inventory_events.InventoryStateEvent;
import com.jim.multipos.utils.rxevents.main_order_events.ConsignmentEvent;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;
import com.jim.multipos.utils.rxevents.main_order_events.ProductEvent;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

import static com.jim.multipos.ui.inventory.fragments.InventoryFragment.SortModes.FILTERED_BY_INVENTORY;
import static com.jim.multipos.ui.inventory.fragments.InventoryFragment.SortModes.FILTERED_BY_INVENTORY_INVERT;
import static com.jim.multipos.ui.inventory.fragments.InventoryFragment.SortModes.FILTERED_BY_LOWSTOCK;
import static com.jim.multipos.ui.inventory.fragments.InventoryFragment.SortModes.FILTERED_BY_LOWSTOCK_INVERT;
import static com.jim.multipos.ui.inventory.fragments.InventoryFragment.SortModes.FILTERED_BY_PRODUCT;
import static com.jim.multipos.ui.inventory.fragments.InventoryFragment.SortModes.FILTERED_BY_PRODUCT_INVERT;
import static com.jim.multipos.ui.inventory.fragments.InventoryFragment.SortModes.FILTERED_BY_UNIT;
import static com.jim.multipos.ui.inventory.fragments.InventoryFragment.SortModes.FILTERED_BY_UNIT_INVERT;
import static com.jim.multipos.ui.inventory.fragments.InventoryFragment.SortModes.FILTERED_BY_VENDOR;
import static com.jim.multipos.ui.inventory.fragments.InventoryFragment.SortModes.FILTERED_BY_VENDOR_INVERT;

/**
 * Created by developer on 09.11.2017.
 */

public class InventoryFragment extends BaseFragment implements InventoryView {
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
    @Inject
    BarcodeStack barcodeStack;

    public enum SortModes {
        FILTERED_BY_PRODUCT, FILTERED_BY_PRODUCT_INVERT, FILTERED_BY_VENDOR, FILTERED_BY_VENDOR_INVERT, FILTERED_BY_LOWSTOCK, FILTERED_BY_LOWSTOCK_INVERT, FILTERED_BY_INVENTORY, FILTERED_BY_INVENTORY_INVERT, FILTERED_BY_UNIT, FILTERED_BY_UNIT_INVERT
    }

    @Inject
    InventoryPresenter presenter;
    @Inject
    RxPermissions rxPermissions;
    @Inject
    RxBus rxBus;
    @Inject
    DatabaseManager databaseManager;

    InventoryItemAdapter inventoryItemAdapter;
    private ArrayList<Disposable> subscriptions;

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
                presenter.onStockAlertChange(newAlertCount, inventoryItem);
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
            public void onStockQueueClick(InventoryItem inventoryItem) {
                presenter.onStockQueueClick(inventoryItem);
            }


            @Override
            public void onConsigmentIn(InventoryItem inventoryItem) {
                presenter.onConsigmentIn(inventoryItem);
            }

            @Override
            public void onConsigmentOut(InventoryItem inventoryItem) {
                presenter.onConsigmentOut(inventoryItem);
            }
        }, getActivity());
        rvInventory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvInventory.setAdapter(inventoryItemAdapter);
        ivProductSort.setVisibility(View.VISIBLE);

        llProduct.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != FILTERED_BY_PRODUCT) {
                filterMode = FILTERED_BY_PRODUCT;
                presenter.filterBy(FILTERED_BY_PRODUCT);
                ivProductSort.setVisibility(View.VISIBLE);
                ivProductSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = FILTERED_BY_PRODUCT_INVERT;
                ivProductSort.setVisibility(View.VISIBLE);
                ivProductSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llVendor.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != FILTERED_BY_VENDOR) {
                filterMode = FILTERED_BY_VENDOR;
                presenter.filterBy(FILTERED_BY_VENDOR);
                ivVendorSort.setVisibility(View.VISIBLE);
                ivVendorSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = FILTERED_BY_VENDOR_INVERT;
                ivVendorSort.setVisibility(View.VISIBLE);
                ivVendorSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llLowStockAlert.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != FILTERED_BY_LOWSTOCK) {
                filterMode = FILTERED_BY_LOWSTOCK;
                presenter.filterBy(FILTERED_BY_LOWSTOCK);
                ivLowStockAlertSort.setVisibility(View.VISIBLE);
                ivLowStockAlertSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = FILTERED_BY_LOWSTOCK_INVERT;
                ivLowStockAlertSort.setVisibility(View.VISIBLE);
                ivLowStockAlertSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }

        });
        llUnit.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != FILTERED_BY_UNIT) {
                filterMode = FILTERED_BY_UNIT;
                presenter.filterBy(FILTERED_BY_UNIT);
                ivUnitSort.setVisibility(View.VISIBLE);
                ivUnitSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = FILTERED_BY_UNIT_INVERT;
                ivUnitSort.setVisibility(View.VISIBLE);
                ivUnitSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llInventory.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != FILTERED_BY_INVENTORY) {
                filterMode = FILTERED_BY_INVENTORY;
                presenter.filterBy(FILTERED_BY_INVENTORY);
                ivInventorySort.setVisibility(View.VISIBLE);
                ivInventorySort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = FILTERED_BY_INVENTORY_INVERT;
                ivInventorySort.setVisibility(View.VISIBLE);
                ivInventorySort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });

        barcodeStack.register(barcode -> {
            if(isAdded() && isVisible())
                ((InventoryActivity)getActivity()).setToolbarSearchText(barcode);
        });
    }

    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof ConsignmentWithVendorEvent) {
                        ConsignmentWithVendorEvent event = (ConsignmentWithVendorEvent) o;
                        switch (event.getType()) {
                            case GlobalEventConstants.UPDATE: {
                                presenter.updateData();
                                break;
                            }
                        }
                    }
                    if (o instanceof ConsignmentEvent) {
                        ConsignmentEvent event = (ConsignmentEvent) o;
                        if (event.getType() == GlobalEventConstants.UPDATE) {
                            presenter.updateData();
                        }
                    }
                    if (o instanceof ProductEvent) {
                        ProductEvent event = (ProductEvent) o;
                        switch (event.getType()) {
                            case GlobalEventConstants.UPDATE: {
                                presenter.updateData();
                                break;
                            }
                        }
                    }
                }));
    }

    @Override
    public void initSearchResults(List<InventoryItem> inventoryItems, String searchText) {
        inventoryItemAdapter.setSearchResult(inventoryItems, searchText);
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
        UIUtils.closeKeyboard(llUnit, getActivity());

    }

    @Override
    public void openWriteOffDialog(InventoryItem inventoryItem, WriteOffProductDialog.WriteOffCallback writeOffCallback) {
        WriteOffProductDialog writeOffProductDialog = new WriteOffProductDialog(getContext(),writeOffCallback,inventoryItem,decimalFormat,databaseManager,null);
        writeOffProductDialog.show();
    }

    @Override
    public void openAddDialog(InventoryItem inventoryItem, SurplusProductDialog.SurplusCallback surplusCallback) {
        SurplusProductDialog surplusProductDialog = new SurplusProductDialog(getActivity(), surplusCallback, inventoryItem, decimalFormat);
        surplusProductDialog.show();
    }

    @Override
    public void openChooseVendorDialog(List<Vendor> vendorList, List<Vendor> vendorsWithProduct) {
        VendorPickerDialog dialog = new VendorPickerDialog(getContext(), vendorList, vendorsWithProduct);
        dialog.show();
        dialog.setListener(vendor -> {
            presenter.setVendorId(vendor.getId());
        });
    }

    @Override
    public void sendDataToConsignment(Long productId, Long vendorId, int consignment_type) {
        ((InventoryActivity) getActivity()).sendDataWithBundle(productId, vendorId, consignment_type);
    }

    @Override
    public void sendInventoryStateEvent(int event) {
        rxBus.send(new InventoryStateEvent(event));
    }

    @Override
    public void openStockQueueForProduct(Long id) {
        ((InventoryActivity) getActivity()).openStockQueueForProduct(id);
    }

    @Override
    public void showVendorListEmptyDialog() {
        UIUtils.showAlert(getContext(), getString(R.string.ok), getString(R.string.warning), getString(R.string.no_vendor_warning_msg), () -> {

        });
    }

    @Override
    public void noVendorsWarning() {
        UIUtils.showAlert(getContext(), getString(R.string.ok), getString(R.string.warning), getString(R.string.no_vendor_error_msg), () -> {

        });
    }


    public void searchText(String searchText) {
        presenter.onSearchTyped(searchText);
    }

    private void deselectAll() {
        ivProductSort.setVisibility(View.GONE);
        ivInventorySort.setVisibility(View.GONE);
        ivLowStockAlertSort.setVisibility(View.GONE);
        ivUnitSort.setVisibility(View.GONE);
        ivVendorSort.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        barcodeStack.unregister();
        super.onDestroy();
        RxBus.removeListners(subscriptions);
    }
}
