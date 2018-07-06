package com.jim.multipos.ui.vendors.vendor_list;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendors.adapter.VendorListAdapter;
import com.jim.multipos.ui.vendors.connection.VendorConnection;
import com.jim.multipos.utils.UIUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class VendorListFragment extends BaseFragment implements VendorListFragmentView {

    @BindView(R.id.rvVendors)
    RecyclerView rvVendors;
    @Inject
    VendorListFragmentPresenter presenter;
    @Inject
    VendorConnection connection;
    private VendorListAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.vendor_list_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setVendorListFragmentView(this);
        rvVendors.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new VendorListAdapter(getContext());
        rvVendors.setAdapter(adapter);
        ((SimpleItemAnimator) rvVendors.getItemAnimator()).setSupportsChangeAnimations(false);
        connection.setSelectedVendor(null, 0);
        adapter.setListener((vendor, position, previousPosition) -> {
            if (position != previousPosition) {
                if (connection.hasChanges()) {
                    UIUtils.showAlert(getContext(), getString(R.string.yes),
                            getString(R.string.no), getString(R.string.discard_changes),
                            getString(R.string.warning_discard_changes), new UIUtils.AlertListener() {
                                @Override
                                public void onPositiveButtonClicked() {
                                    connection.setSelectedVendor(vendor, position);
                                }

                                @Override
                                public void onNegativeButtonClicked() {
                                    adapter.setSelectedPosition(previousPosition);
                                }
                            });
                } else {
                    connection.setSelectedVendor(vendor, position);
                }
            }

        });
        presenter.onCreateView(savedInstanceState);
    }

    @Override
    public void setVendorsList(List<Vendor> vendorList) {
        adapter.setItems(vendorList);
    }

    @Override
    public void refreshVendorsList() {
        presenter.refreshList();
    }

    @Override
    public void notifyItemUpdated() {
        adapter.notifyItemChanged(adapter.getSelectedPosition());
    }

    @Override
    public void setSelectedPosition(int position) {
        adapter.setSelectedPosition(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        connection.setVendorListFragmentView(null);
    }
}
