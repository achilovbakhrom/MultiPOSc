package com.jim.multipos.ui.vendor_products_view.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewActivity;
import com.jim.multipos.ui.vendor_products_view.adapters.ProductAdapter;
import com.jim.multipos.ui.vendor_products_view.dialogs.MinusInventoryDialog;
import com.jim.multipos.ui.vendor_products_view.dialogs.PlusInventoryDialog;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorDetailsList extends BaseFragment implements ProductAdapter.ProductAdapterListener {
    public static final String INVENTORY_POSITION = "INVENTORY_POSITION";
    @BindView(R.id.rvProduct)
    RecyclerView rvProduct;
    @BindView(R.id.tvProducts)
    TextView tvProducts;
    @BindView(R.id.tvInventory)
    TextView tvInventory;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    private boolean isSortedByProduct;
    private boolean isSortedByInventory;
    private boolean isSortedByUnit;

    @Override
    protected int getLayout() {
        return R.layout.vendors_detail_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        rvProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        ((SimpleItemAnimator) rvProduct.getItemAnimator()).setSupportsChangeAnimations(false);
        rvProduct.setAdapter(new ProductAdapter(getContext(), ((VendorProductsViewActivity) getActivity()).getDecimalFormat(), this, ((VendorProductsViewActivity) getActivity()).getPresenter().getInventoryItems()));

        RxView.clicks(tvProducts).subscribe(o -> {
            if (isSortedByProduct) {
                isSortedByProduct = false;
                ((VendorProductsViewActivity) getActivity()).getPresenter().sortByProductAsc();
                tvProducts.setTypeface(Typeface.create(tvProducts.getTypeface(), Typeface.NORMAL));
                rvProduct.getAdapter().notifyDataSetChanged();
            } else {
                isSortedByProduct = true;
                ((VendorProductsViewActivity) getActivity()).getPresenter().sortByProductDesc();
                tvProducts.setTypeface(Typeface.create(tvProducts.getTypeface(), Typeface.BOLD));
                rvProduct.getAdapter().notifyDataSetChanged();
            }

            isSortedByInventory = false;
            isSortedByUnit = false;
            tvInventory.setTypeface(Typeface.create(tvInventory.getTypeface(), Typeface.NORMAL));
            tvUnit.setTypeface(Typeface.create(tvUnit.getTypeface(), Typeface.NORMAL));
        });

        RxView.clicks(tvInventory).subscribe(o -> {
            if (isSortedByInventory) {
                isSortedByInventory = false;
                ((VendorProductsViewActivity) getActivity()).getPresenter().sortByInventoryDesc();
                tvInventory.setTypeface(Typeface.create(tvInventory.getTypeface(), Typeface.NORMAL));
                rvProduct.getAdapter().notifyDataSetChanged();
            } else {
                isSortedByInventory = true;
                ((VendorProductsViewActivity) getActivity()).getPresenter().sortByInventoryAsc();
                tvInventory.setTypeface(Typeface.create(tvInventory.getTypeface(), Typeface.BOLD));
                rvProduct.getAdapter().notifyDataSetChanged();
            }

            isSortedByProduct = false;
            isSortedByUnit = false;
            tvProducts.setTypeface(Typeface.create(tvProducts.getTypeface(), Typeface.NORMAL));
            tvUnit.setTypeface(Typeface.create(tvUnit.getTypeface(), Typeface.NORMAL));
        });

        RxView.clicks(tvUnit).subscribe(o -> {
            if (isSortedByUnit) {
                isSortedByUnit = false;
                ((VendorProductsViewActivity) getActivity()).getPresenter().sortByUnitDesc();
                tvUnit.setTypeface(Typeface.create(tvUnit.getTypeface(), Typeface.NORMAL));
                rvProduct.getAdapter().notifyDataSetChanged();
            } else {
                isSortedByUnit = true;
                ((VendorProductsViewActivity) getActivity()).getPresenter().sortByUnitAsc();
                tvUnit.setTypeface(Typeface.create(tvUnit.getTypeface(), Typeface.BOLD));
                rvProduct.getAdapter().notifyDataSetChanged();
            }

            isSortedByProduct = false;
            isSortedByInventory = false;
            tvProducts.setTypeface(Typeface.create(tvProducts.getTypeface(), Typeface.NORMAL));
            tvInventory.setTypeface(Typeface.create(tvInventory.getTypeface(), Typeface.NORMAL));
        });
    }

    @Override
    public void showMinusDialog(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(INVENTORY_POSITION, position);
        MinusInventoryDialog dialog = new MinusInventoryDialog();
        dialog.setArguments(bundle);
        dialog.setMinusInventoryDialogListener(inventory -> {
            ((ProductAdapter) rvProduct.getAdapter()).updateItem(inventory);
        });
        dialog.show(getActivity().getSupportFragmentManager(), "MinusInventoryDialog");
    }

    @Override
    public void showPlusDialog(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(INVENTORY_POSITION, position);
        PlusInventoryDialog dialog = new PlusInventoryDialog();
        dialog.setArguments(bundle);
        dialog.setPlusInventoryDialogListener(inventory -> {
            ((ProductAdapter) rvProduct.getAdapter()).updateItem(inventory);
        });
        dialog.show(getActivity().getSupportFragmentManager(), "PlusInventoryDialog");
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }
}
