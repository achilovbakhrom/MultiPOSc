package com.jim.multipos.ui.vendor.add_edit.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor.AddingMode;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditActivity;
import com.jim.multipos.ui.vendor.add_edit.adapter.VendorsListAdapter;
import com.jim.multipos.utils.OnItemClickListener;
import com.jim.multipos.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by bakhrom on 10/21/17.
 */

public class VendorsListFragment extends BaseFragment {

    public static final String SELECTED_POSITION = "SELECTED_POSITION";

    @BindView(R.id.rvVendors)
    RecyclerView vendors;

    private int selectedPosition = -1;

    @Override
    protected int getLayout() {
        return R.layout.vendor_list_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(SELECTED_POSITION, -1);
        }
        fillVendorList();
    }

    private void fillVendorList() {
        if (vendors == null || vendors.getAdapter() == null) {
            List<Vendor> items = ((VendorAddEditActivity) getContext()).getPresenter().getVendors();
            if (items == null) items = new ArrayList<>();
            items.add(0, null);
            VendorsListAdapter adapter = new VendorsListAdapter(items, getContext(),((VendorAddEditActivity)getActivity()).getDatabaseManager());
            ((SimpleItemAnimator) vendors.getItemAnimator()).setSupportsChangeAnimations(false);
            adapter.setOnItemClickListener(new OnItemClickListener<Vendor>() {
                @Override
                public void onItemClicked(int position) {
                    UIUtils.closeKeyboard(vendors, getContext());
                }

                @Override
                public void onItemClicked(Vendor item) {
                    if (item == null) { ((VendorAddEditActivity) getContext()).getPresenter().setMode(AddingMode.ADD, null); }
                    else { ((VendorAddEditActivity) getContext()).getPresenter().setMode(AddingMode.EDIT, item.getId()); }
                }
            });
            vendors.setLayoutManager(new GridLayoutManager(getContext(), 3));
            vendors.setAdapter(adapter);
        }
        else {
            List<Vendor> items = ((VendorAddEditActivity) getContext()).getPresenter().getVendors();
            if (items == null) items = new ArrayList<>();
            items.add(0, null);
            ((VendorsListAdapter)vendors.getAdapter()).setItems(items);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_POSITION, selectedPosition);

    }

    public void updateVendorsList() {
        fillVendorList();
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }




    public void setAddMode() {
        if (vendors != null && vendors.getAdapter() != null)
            ((VendorsListAdapter) vendors.getAdapter()).select(0);
    }

    public void setSelection(int position) {
        if (vendors != null && vendors.getAdapter() != null)
            ((VendorsListAdapter) vendors.getAdapter()).select(position);
    }

    public void discardChanges() {
        setSelection(selectedPosition);
    }

    public void changeSelectedPosition() {
        selectedPosition = ((VendorsListAdapter) vendors.getAdapter()).getSelectedPosition();
    }

    public void selectListItem(Long id) {
        if (this.vendors.getAdapter() != null) {
            ((VendorsListAdapter) this.vendors.getAdapter()).setSelectedPositionWithId(id);
        }
    }
}
