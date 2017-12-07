package com.jim.multipos.ui.vendor_item_managment.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.ui.billing_vendor.BillingOperationsActivity;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment;
import com.jim.multipos.ui.inventory.fragments.InventoryFragment;
import com.jim.multipos.ui.inventory.presenter.InventoryPresenter;
import com.jim.multipos.ui.vendor.add_edit.adapter.VendorsListAdapter;
import com.jim.multipos.ui.vendor_item_managment.VendorItemsActivity;
import com.jim.multipos.ui.vendor_item_managment.adapters.VendorItemAdapter;
import com.jim.multipos.ui.vendor_item_managment.model.VendorWithDebt;
import com.jim.multipos.ui.vendor_item_managment.presenter.VendorItemPresenter;
import com.jim.multipos.utils.UIUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.DEBT;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.DEBT_INVENTORY;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.PRODUCTS;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.PRODUCTS_INVENTORY;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.VENDOR;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.VENDOR_INVENTORY;


/**
 * Created by developer on 20.11.2017.
 */

public class VendorItemFragment extends BaseFragment implements VendorItemView{
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
    VendorItemAdapter vendorItemAdapter;

    public enum SortModes{
        VENDOR,VENDOR_INVENTORY,PRODUCTS,PRODUCTS_INVENTORY,DEBT,DEBT_INVENTORY
    }
    @Override
    public void initRecyclerView(List<VendorWithDebt> vendorWithDebts) {
        vendorItemAdapter = new VendorItemAdapter(vendorWithDebts, new VendorItemAdapter.OnVendorAdapterCallback() {
            @Override
            public void onIncomeProduct(VendorWithDebt vendorWithDebt) {
                presenter.onIncomeProduct(vendorWithDebt);
            }

            @Override
            public void onWriteOff(VendorWithDebt vendorWithDebt) {
                presenter.onWriteOff(vendorWithDebt);
            }

            @Override
            public void onConsigmentStory(VendorWithDebt vendorWithDebt) {
                presenter.onConsigmentStory(vendorWithDebt);
            }

            @Override
            public void onPay(VendorWithDebt vendorWithDebt) {
                presenter.onPay(vendorWithDebt);
            }

            @Override
            public void onPayStory(VendorWithDebt vendorWithDebt,Double debt) {
                presenter.onPayStory(vendorWithDebt,debt);
            }

            @Override
            public void onMore(VendorWithDebt vendorWithDebt) {
                presenter.onMore(vendorWithDebt);
            }
        },getContext(),decimalFormat);
        rvVendorItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVendorItems.setAdapter(vendorItemAdapter);
        ivVendorSort.setVisibility(View.VISIBLE);

        llVendor.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != VENDOR){
                filterMode = VENDOR;
                presenter.filterBy(VENDOR);
                ivVendorSort.setVisibility(View.VISIBLE);
                ivVendorSort.setImageResource(R.drawable.sorting);
            }
            else {
                filterMode = VENDOR_INVENTORY;
                ivVendorSort.setVisibility(View.VISIBLE);
                ivVendorSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });

        llProduct.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != PRODUCTS){
                filterMode = PRODUCTS;
                presenter.filterBy(PRODUCTS);
                ivProductSort.setVisibility(View.VISIBLE);
                ivProductSort.setImageResource(R.drawable.sorting);
            }
            else {
                filterMode = PRODUCTS_INVENTORY;
                ivProductSort.setVisibility(View.VISIBLE);
                ivProductSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });

        llDebt.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != DEBT){
                filterMode = DEBT;
                presenter.filterBy(DEBT);
                ivDebtSort.setVisibility(View.VISIBLE);
                ivDebtSort.setImageResource(R.drawable.sorting);
            }
            else {
                filterMode = DEBT_INVENTORY;
                ivDebtSort.setVisibility(View.VISIBLE);
                ivDebtSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });

    }

    @Override
    public void initSearchResults(List<VendorWithDebt> vendorWithDebts, String searchText) {
        vendorItemAdapter.setSearchResult(vendorWithDebts,searchText);
        vendorItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void initDefault(List<VendorWithDebt> vendorWithDebts) {
        vendorItemAdapter.setData(vendorWithDebts);
        vendorItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyList() {
        vendorItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeKeyboard() {
        UIUtils.closeKeyboard(llDebt,getActivity());

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
    public void openVendorBillingStory(Long vendorId,Double totaldebt) {
        Intent intent = new Intent(getActivity(), BillingOperationsActivity.class);
        intent.putExtra(BillingOperationsActivity.VENDOR_EXTRA_ID,vendorId);
        intent.putExtra(BillingOperationsActivity.VENDOR_DEBT,totaldebt);
        getActivity().startActivity(intent);
    }


    @Override
    protected int getLayout() {
        return R.layout.vendor_item_fragment;
    }
    public void searchText(String searchText){
        presenter.onSearchTyped(searchText);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.onCreateView(savedInstanceState);
    }
    private void deselectAll(){
        ivDebtSort.setVisibility(View.GONE);
        ivProductSort.setVisibility(View.GONE);
        ivVendorSort.setVisibility(View.GONE);
    }
}
