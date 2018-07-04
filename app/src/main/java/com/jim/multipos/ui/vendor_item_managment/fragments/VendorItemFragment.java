package com.jim.multipos.ui.vendor_item_managment.fragments;

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
import com.jim.multipos.ui.vendor_item_managment.VendorItemsActivity;
import com.jim.multipos.ui.vendor_item_managment.adapters.VendorItemAdapter;
import com.jim.multipos.ui.vendor_item_managment.model.VendorManagmentItem;
import com.jim.multipos.ui.vendor_item_managment.presenter.VendorItemPresenter;
import com.jim.multipos.utils.PaymentToVendorDialog;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.rxevents.inventory_events.BillingOperationEvent;
import com.jim.multipos.utils.rxevents.inventory_events.VendorEvent;
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

import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.DEBT;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.DEBT_INVENTORY;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.PRODUCTS;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.PRODUCTS_INVENTORY;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.VENDOR;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.VENDOR_INVENTORY;


/**
 * Created by developer on 20.11.2017.
 */

public class VendorItemFragment extends BaseFragment implements VendorItemView {
    @BindView(R.id.llVendor)
    LinearLayout llVendor;
    @BindView(R.id.llProduct)
    LinearLayout llProduct;
    @BindView(R.id.llDebt)
    LinearLayout llDebt;

    @BindView(R.id.ivVendorSort)
    ImageView ivVendorSort;
    @BindView(R.id.ivProductSort)
    ImageView ivProductSort;
    @BindView(R.id.ivDebtSort)
    ImageView ivDebtSort;


    @BindView(R.id.rvVendorItems)
    RecyclerView rvVendorItems;
    SortModes filterMode = VENDOR;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    VendorItemPresenter presenter;
    @Inject
    RxPermissions rxPermissions;
    @Inject
    RxBus rxBus;
    VendorItemAdapter vendorItemAdapter;
    public static final String BILLINGS_UPDATE = "BILLINGS_UPDATE";
    private ArrayList<Disposable> subscriptions;


    public enum SortModes {
        VENDOR, VENDOR_INVENTORY, PRODUCTS, PRODUCTS_INVENTORY, DEBT, DEBT_INVENTORY
    }

    @Override
    public void initRecyclerView(List<VendorManagmentItem> vendorManagmentItems) {
        vendorItemAdapter = new VendorItemAdapter(vendorManagmentItems, new VendorItemAdapter.OnVendorAdapterCallback() {
            @Override
            public void onIncomeProduct(VendorManagmentItem vendorManagmentItem) {
                presenter.onIncomeProduct(vendorManagmentItem);
            }

            @Override
            public void onWriteOff(VendorManagmentItem vendorManagmentItem) {
                presenter.onWriteOff(vendorManagmentItem);
            }

            @Override
            public void onConsigmentStory(VendorManagmentItem vendorManagmentItem) {
                presenter.onConsigmentStory(vendorManagmentItem);
            }

            @Override
            public void onPay(VendorManagmentItem vendorManagmentItem) {
                presenter.onPay(vendorManagmentItem);
            }

            @Override
            public void onPayStory(VendorManagmentItem vendorManagmentItem, Double debt) {
                presenter.onPayStory(vendorManagmentItem, debt);
            }

            @Override
            public void onMore(VendorManagmentItem vendorManagmentItem) {
                presenter.onMore(vendorManagmentItem);
            }

            @Override
            public void onStockQueueForVendor(VendorManagmentItem vendorManagmentItem) {
                presenter.onStockQueueForVendor(vendorManagmentItem);
            }
        }, getContext(), decimalFormat);
        rvVendorItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVendorItems.setAdapter(vendorItemAdapter);
        ivVendorSort.setVisibility(View.VISIBLE);

        llVendor.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != VENDOR) {
                filterMode = VENDOR;
                presenter.filterBy(VENDOR);
                ivVendorSort.setVisibility(View.VISIBLE);
                ivVendorSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = VENDOR_INVENTORY;
                ivVendorSort.setVisibility(View.VISIBLE);
                ivVendorSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });

        llProduct.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != PRODUCTS) {
                filterMode = PRODUCTS;
                presenter.filterBy(PRODUCTS);
                ivProductSort.setVisibility(View.VISIBLE);
                ivProductSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = PRODUCTS_INVENTORY;
                ivProductSort.setVisibility(View.VISIBLE);
                ivProductSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });

        llDebt.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != DEBT) {
                filterMode = DEBT;
                presenter.filterBy(DEBT);
                ivDebtSort.setVisibility(View.VISIBLE);
                ivDebtSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = DEBT_INVENTORY;
                ivDebtSort.setVisibility(View.VISIBLE);
                ivDebtSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });

    }

    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof BillingOperationEvent) {
                        BillingOperationEvent event = (BillingOperationEvent) o;
                        if (event.getType() == GlobalEventConstants.BILLING_IS_DONE) {
                            presenter.updateData();
                        }
                    }
                    if (o instanceof ConsignmentEvent) {
                        ConsignmentEvent event = (ConsignmentEvent) o;
                        if (event.getType() == GlobalEventConstants.UPDATE) {
                            presenter.updateData();
                        }
                    }

                    if (o instanceof VendorEvent) {
                        VendorEvent event = (VendorEvent) o;
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
    public void onDestroy() {
        super.onDestroy();
        RxBus.removeListners(subscriptions);
    }

    @Override
    public void initSearchResults(List<VendorManagmentItem> vendorManagmentItems, String searchText) {
        vendorItemAdapter.setSearchResult(vendorManagmentItems, searchText);
        vendorItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void initDefault(List<VendorManagmentItem> vendorManagmentItems) {
        vendorItemAdapter.setData(vendorManagmentItems);
        vendorItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyList() {
        vendorItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeKeyboard() {
        UIUtils.closeKeyboard(llDebt, getActivity());

    }

    @Override
    public void sendDataToConsignment(Long vendorId, int consignment_type) {
        ((VendorItemsActivity) getActivity()).sendDataToConsignment(vendorId, consignment_type);
    }

    @Override
    public void openVendorDetails(Long vendorId) {
        ((VendorItemsActivity) getActivity()).openVendorDetails(vendorId);
    }

    @Override
    public void openVendorConsignmentsStory(Long vendorId) {
        ((VendorItemsActivity) getActivity()).openVendorConsignmentStory(vendorId);
    }

    @Override
    public void openPaymentDialog(DatabaseManager databaseManager, Vendor vendor) {
        PaymentToVendorDialog paymentToVendorDialog = new PaymentToVendorDialog(getContext(), vendor, new PaymentToVendorDialog.PaymentToVendorCallback() {
            @Override
            public void onChanged() {
                presenter.updateData();
            }

            @Override
            public void onCancel() {
            }
        }, databaseManager, null);
        paymentToVendorDialog.show();
    }

    @Override
    public void openStockQueueForVendor(Long id) {
        ((VendorItemsActivity) getActivity()).openProductQueueStory(id);
    }

    @Override
    public void openVendorBillingStory(Long vendorId, Double totaldebt) {
        ((VendorItemsActivity) getActivity()).openVendorBillingStory(vendorId, totaldebt);
    }


    @Override
    protected int getLayout() {
        return R.layout.vendor_item_fragment;
    }

    public void searchText(String searchText) {
        presenter.onSearchTyped(searchText);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.onCreateView(savedInstanceState);
    }

    private void deselectAll() {
        ivDebtSort.setVisibility(View.GONE);
        ivProductSort.setVisibility(View.GONE);
        ivVendorSort.setVisibility(View.GONE);
    }
}
