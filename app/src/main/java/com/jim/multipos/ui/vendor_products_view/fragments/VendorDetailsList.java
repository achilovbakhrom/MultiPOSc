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
import com.jim.multipos.data.db.model.inventory.InventoryState;
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
    @BindView(R.id.ivProductSort)
    ImageView ivProductSort;
    @BindView(R.id.ivInventorySort)
    ImageView ivInventorySort;
    @BindView(R.id.ivUnitSort)
    ImageView ivUnitSort;
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
        rvProduct.setAdapter(new ProductAdapter(getContext(), ((VendorProductsViewActivity) getActivity()).getDecimalFormat(), this, ((VendorProductsViewActivity) getActivity()).getPresenter().getInventoryStates()));

        showProductArrowDown();
        isSortedByProduct = true;

        RxView.clicks(tvProducts).subscribe(o -> {
            if (isSortedByProduct) {
                isSortedByProduct = false;
                ((VendorProductsViewActivity) getActivity()).getPresenter().sortByProductDesc();
                showProductArrowUp();
                rvProduct.getAdapter().notifyDataSetChanged();
            } else {
                isSortedByProduct = true;
                ((VendorProductsViewActivity) getActivity()).getPresenter().sortByProductAsc();
                showProductArrowDown();
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
                ((VendorProductsViewActivity) getActivity()).getPresenter().sortByInventoryAsc();
                showInventoryArrowUp();
                rvProduct.getAdapter().notifyDataSetChanged();
            } else {
                isSortedByInventory = true;
                ((VendorProductsViewActivity) getActivity()).getPresenter().sortByInventoryDesc();
                showInventoryArrowDown();
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
                ((VendorProductsViewActivity) getActivity()).getPresenter().sortByUnitAsc();
                showUnitArrowUp();
                rvProduct.getAdapter().notifyDataSetChanged();
            } else {
                isSortedByUnit = true;
                ((VendorProductsViewActivity) getActivity()).getPresenter().sortByUnitDesc();
                showUnitArrowDown();
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
        dialog.setMinusInventoryDialogListener((inventory, shortage) ->  {
            ((ProductAdapter) rvProduct.getAdapter()).updateItem(inventory);
            ((VendorProductsViewActivity) getActivity()).getPresenter().insertNewWarehouseOperation(inventory, shortage);
        });
        dialog.show(getActivity().getSupportFragmentManager(), "MinusInventoryDialog");
    }

    @Override
    public void showPlusDialog(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(INVENTORY_POSITION, position);
        PlusInventoryDialog dialog = new PlusInventoryDialog();
        dialog.setArguments(bundle);
        dialog.setPlusInventoryDialogListener((inventory, shortage) ->  {
            ((ProductAdapter) rvProduct.getAdapter()).updateItem(inventory);
            ((VendorProductsViewActivity) getActivity()).getPresenter().insertNewWarehouseOperation(inventory, shortage);
        });
        dialog.show(getActivity().getSupportFragmentManager(), "PlusInventoryDialog");
    }

    @Override
    public void getInventoryItem(InventoryState state, int consignmentType) {
        ((VendorProductsViewActivity) getActivity()).getPresenter().openIncomeConsignmentToProduct(state, consignmentType);
    }

    @Override
    public ProductClass getProductClass(Long id) {
        return ((VendorProductsViewActivity) getActivity()).getPresenter().getProductClassById(id);
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    private void showProductArrowUp() {
        ivProductSort.setImageResource(R.drawable.sorting_invert);
        ivProductSort.setVisibility(View.VISIBLE);
        ivInventorySort.setVisibility(View.INVISIBLE);
        ivUnitSort.setVisibility(View.INVISIBLE);
        tvProducts.setTypeface(Typeface.create(tvProducts.getTypeface(), Typeface.BOLD));
    }

    private void showProductArrowDown() {
        ivProductSort.setImageResource(R.drawable.sorting);
        ivProductSort.setVisibility(View.VISIBLE);
        ivInventorySort.setVisibility(View.INVISIBLE);
        ivUnitSort.setVisibility(View.INVISIBLE);
        tvProducts.setTypeface(Typeface.create(tvProducts.getTypeface(), Typeface.BOLD));
    }

    private void showInventoryArrowUp() {
        ivInventorySort.setImageResource(R.drawable.sorting_invert);
        ivInventorySort.setVisibility(View.VISIBLE);
        ivProductSort.setVisibility(View.INVISIBLE);
        ivUnitSort.setVisibility(View.INVISIBLE);
        tvInventory.setTypeface(Typeface.create(tvInventory.getTypeface(), Typeface.BOLD));
    }

    private void showInventoryArrowDown() {
        ivInventorySort.setImageResource(R.drawable.sorting);
        ivInventorySort.setVisibility(View.VISIBLE);
        ivProductSort.setVisibility(View.INVISIBLE);
        ivUnitSort.setVisibility(View.INVISIBLE);
        tvInventory.setTypeface(Typeface.create(tvInventory.getTypeface(), Typeface.BOLD));
    }

    private void showUnitArrowUp() {
        ivUnitSort.setImageResource(R.drawable.sorting_invert);
        ivUnitSort.setVisibility(View.VISIBLE);
        ivProductSort.setVisibility(View.INVISIBLE);
        ivInventorySort.setVisibility(View.INVISIBLE);
        tvUnit.setTypeface(Typeface.create(tvUnit.getTypeface(), Typeface.BOLD));
    }

    private void showUnitArrowDown() {
        ivUnitSort.setImageResource(R.drawable.sorting);
        ivUnitSort.setVisibility(View.VISIBLE);
        ivProductSort.setVisibility(View.INVISIBLE);
        ivInventorySort.setVisibility(View.INVISIBLE);
        tvUnit.setTypeface(Typeface.create(tvUnit.getTypeface(), Typeface.BOLD));
    }
}
