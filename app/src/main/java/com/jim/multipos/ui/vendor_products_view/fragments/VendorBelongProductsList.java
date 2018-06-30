package com.jim.multipos.ui.vendor_products_view.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewActivity;
import com.jim.multipos.ui.vendor_products_view.adapters.ProductAdapter;
import com.jim.multipos.ui.vendor_products_view.model.ProductState;
import com.jim.multipos.ui.vendor_products_view.presenter.VendorBelongProductsListPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.inventory_events.ConsignmentWithVendorEvent;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorBelongProductsList extends BaseFragment implements VendorBelongProductsListView {
    @Inject
    VendorBelongProductsListPresenter presenter;
    ProductAdapter productAdapter;
    @BindView(R.id.rvProduct)
    RecyclerView rvProduct;
    @BindView(R.id.tvProducts)
    TextView tvProducts;
    @BindView(R.id.tvInventory)
    TextView tvInventory;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.ivProductSort)
    ImageView ivProductSort;
    @BindView(R.id.ivInventorySort)
    ImageView ivInventorySort;
    @BindView(R.id.ivUnitSort)
    ImageView ivUnitSort;

    @Inject
    DecimalFormat decimalFormat;

    @Override
    protected int getLayout() {
        return R.layout.vendors_detail_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.onCreateView(getArguments());
    }


    @Override
    public void initRecyclerView(List<ProductState> productStates) {
        rvProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        productAdapter = new ProductAdapter(getContext(), decimalFormat, new ProductAdapter.ProductAdapterListener() {
            @Override
            public void onInvoiceClick(ProductState state) {
                presenter.onInvoiceClick(state);
            }

            @Override
            public void onOutvoiceClick(ProductState state) {
                presenter.onOutvoiceClick(state);
            }
        }, productStates);
        rvProduct.setAdapter(productAdapter);

    }

    @Override
    public void openVendorInvoiceWithProduct(Vendor vendor, Product product) {

    }

    @Override
    public void openVendorOutvoiceWithProduct(Vendor vendor, Product product) {

    }
}
