package com.jim.multipos.ui.vendor_item_managment.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.inventory.fragments.InventoryFragment;
import com.jim.multipos.ui.inventory.presenter.InventoryPresenter;
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
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.DEFAULT;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.PRODUCTS;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.SortModes.VENDOR;


/**
 * Created by developer on 20.11.2017.
 */

public class VendorItemFragment extends BaseFragment implements VendorItemView{
    @BindView(R.id.tvVendor)
    TextView tvVendor;
    @BindView(R.id.tvProduct)
    TextView tvProduct;
    @BindView(R.id.tvDebt)
    TextView tvDebt;
    @BindView(R.id.rvVendorItems)
    RecyclerView rvVendorItems;
    SortModes filterMode ;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    VendorItemPresenter presenter;
    @Inject
    RxPermissions rxPermissions;
    VendorItemAdapter vendorItemAdapter;

    public enum SortModes{
        DEFAULT,VENDOR,PRODUCTS,DEBT
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
            public void onPayStory(VendorWithDebt vendorWithDebt) {
                presenter.onPayStory(vendorWithDebt);
            }

            @Override
            public void onMore(VendorWithDebt vendorWithDebt) {
                presenter.onMore(vendorWithDebt);
            }
        },getContext(),decimalFormat);
        rvVendorItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVendorItems.setAdapter(vendorItemAdapter);

        tvVendor.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != VENDOR){
                filterMode = VENDOR;
                presenter.filterBy(VENDOR);
                tvVendor.setTypeface(Typeface.create(tvVendor.getTypeface(), Typeface.BOLD));
            }
            else {
                filterMode = DEFAULT;
                presenter.filterCancel();
            }
        });

        tvProduct.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != PRODUCTS){
                filterMode = PRODUCTS;
                presenter.filterBy(PRODUCTS);
                tvProduct.setTypeface(Typeface.create(tvProduct.getTypeface(), Typeface.BOLD));
            }
            else {
                filterMode = DEFAULT;
                presenter.filterCancel();
            }
        });

        tvDebt.setOnClickListener(view -> {
            deselectAll();
            if(filterMode != DEBT){
                filterMode = DEBT;
                presenter.filterBy(DEBT);
                tvDebt.setTypeface(Typeface.create(tvDebt.getTypeface(), Typeface.BOLD));
            }
            else {
                filterMode = DEFAULT;
                presenter.filterCancel();
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
        UIUtils.closeKeyboard(tvVendor,getActivity());

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
        tvProduct.setTypeface(Typeface.create(tvProduct.getTypeface(), Typeface.NORMAL));
        tvDebt.setTypeface(Typeface.create(tvDebt.getTypeface(), Typeface.NORMAL));
        tvVendor.setTypeface(Typeface.create(tvVendor.getTypeface(), Typeface.NORMAL));
    }
}
