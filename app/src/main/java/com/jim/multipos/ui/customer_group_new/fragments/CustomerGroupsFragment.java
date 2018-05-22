package com.jim.multipos.ui.customer_group_new.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group_new.CustomerGroupActivity;
import com.jim.multipos.ui.customer_group_new.adapters.CustomerGroupsAdapter;
import com.jim.multipos.utils.UIUtils;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerGroupsFragment extends BaseFragment {
    @BindView(R.id.rvCustomerGroups)
    RecyclerView rvCustomerGroups;

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    @Override
    protected int getLayout() {
        return R.layout.customer_group_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((SimpleItemAnimator) rvCustomerGroups.getItemAnimator()).setSupportsChangeAnimations(false);
        rvCustomerGroups.setLayoutManager(new GridLayoutManager(getContext(), 6));
        rvCustomerGroups.setAdapter(new CustomerGroupsAdapter(getContext(),((CustomerGroupActivity) getActivity()).getPresenter().getCustomerGroups(),
                new CustomerGroupsAdapter.OnItemClickListener() {
                    @Override
                    public void onAddButtonPressed() {
                        if (((CustomerGroupActivity) getActivity()).getPresenter().hasChanges()) {
                            UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no), getString(R.string.discard_changes),
                                    getString(R.string.warning_discard_changes), new UIUtils.AlertListener() {
                                        @Override
                                        public void onPositiveButtonClicked() {
                                            ((CustomerGroupActivity) getActivity()).getPresenter().clearAddFragment();
                                        }

                                        @Override
                                        public void onNegativeButtonClicked() {

                                        }
                                    });

                        } else
                            ((CustomerGroupActivity) getActivity()).getPresenter().clearAddFragment();
                    }

                    @Override
                    public void onItemPressed(int selectedPosition, int prevPosition) {
                        if (((CustomerGroupActivity) getActivity()).getPresenter().hasChanges()) {
                            UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no), getString(R.string.discard_changes),
                                    getString(R.string.warning_discard_changes), new UIUtils.AlertListener() {
                                        @Override
                                        public void onPositiveButtonClicked() {
                                            ((CustomerGroupActivity) getActivity()).getPresenter().showSelectedCustomerGroup(selectedPosition - 1);
                                        }

                                        @Override
                                        public void onNegativeButtonClicked() {
                                            ((CustomerGroupsAdapter) rvCustomerGroups.getAdapter()).setPosition(prevPosition);
                                        }
                                    });

                        } else
                            ((CustomerGroupActivity) getActivity()).getPresenter().showSelectedCustomerGroup(selectedPosition - 1);
                    }
                }, 0));
    }

    public void addItem(CustomerGroup customerGroup) {
        ((CustomerGroupsAdapter) rvCustomerGroups.getAdapter()).addItem(customerGroup);
    }

    public void removeItem(CustomerGroup customerGroup) {
        ((CustomerGroupsAdapter) rvCustomerGroups.getAdapter()).removeItem(customerGroup);
    }

    public void updateItem(CustomerGroup customerGroup) {
        ((CustomerGroupsAdapter) rvCustomerGroups.getAdapter()).updateItem(customerGroup);
    }

    public void setDefaultValueRV() {
        ((CustomerGroupsAdapter) rvCustomerGroups.getAdapter()).setToDefault();
    }
}
