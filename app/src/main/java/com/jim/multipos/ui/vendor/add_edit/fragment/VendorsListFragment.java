package com.jim.multipos.ui.vendor.add_edit.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.ui.vendor.AddingMode;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditActivity;
import com.jim.multipos.ui.vendor.add_edit.adapter.VendorsListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by bakhrom on 10/21/17.
 */

public class VendorsListFragment extends BaseFragment implements ClickableBaseAdapter.OnItemClickListener<Vendor> {

    @BindView(R.id.rvVendors)
    RecyclerView vendors;

    @Override
    protected int getLayout() {
        return R.layout.vendor_list_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fillVendorList();
    }

    private void fillVendorList() {
        if (vendors == null || vendors.getAdapter() == null) {
            List<Vendor> items = ((VendorAddEditActivity) getContext()).getPresenter().getVendors();
            if (items == null) items = new ArrayList<>();
            items.add(0, null);
            VendorsListAdapter adapter = new VendorsListAdapter(items);
            adapter.setOnItemClickListener(this);

            vendors.setLayoutManager(new GridLayoutManager(getContext(), 6));
            vendors.setAdapter(adapter);
        }
        else {
            List<Vendor> items = ((VendorAddEditActivity) getContext()).getPresenter().getVendors();
            if (items == null) items = new ArrayList<>();
            items.add(0, null);
            ((VendorsListAdapter)vendors.getAdapter()).setItems(items);
        }
    }

    public void updateVendorsList() {
        fillVendorList();
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    @Override
    public void onItemClicked(int position) {
        //TODO nothing
    }

    @Override
    public void onItemClicked(Vendor item) {
        if (item == null) {
            ((VendorAddEditActivity) getContext()).getPresenter().setMode(AddingMode.ADD, null);
        }
        else {
            ((VendorAddEditActivity) getContext()).getPresenter().setMode(AddingMode.EDIT, item.getId());
        }
    }

    public void setAddMode() {
        if (vendors != null && vendors.getAdapter() != null)
            ((VendorsListAdapter) vendors.getAdapter()).select(0);
    }
}
