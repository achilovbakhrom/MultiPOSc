package com.jim.multipos.ui.customer_debt.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.mpviews.MpSearchView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.ui.customer_debt.adapter.CustomerListAdapter;
import com.jim.multipos.ui.customer_debt.connection.CustomerDebtConnection;
import com.jim.multipos.ui.customer_debt.presenter.CustomerListPresenter;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 29.12.2017.
 */

public class CustomerListFragment extends BaseFragment implements CustomerListView {

    @Inject
    CustomerListPresenter presenter;
    @BindView(R.id.rvCustomerList)
    RecyclerView rvCustomerList;
    @BindView(R.id.svCustomerSearch)
    MpSearchView svCustomerSearch;
    @Inject
    CustomerListAdapter customerListAdapter;
    @Inject
    CustomerDebtConnection customerDebtConnection;

    @Override
    protected int getLayout() {
        return R.layout.customer_list_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        customerDebtConnection.setCustomerListView(this);
        presenter.initData();
        rvCustomerList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCustomerList.setAdapter(customerListAdapter);
        customerListAdapter.setListener((item, position) -> customerDebtConnection.sendCustomerWithDebt(item));
    }

    @Override
    public void fillCustomerListRecyclerView(List<Customer> customerList) {
        customerListAdapter.setItems(customerList);
        svCustomerSearch.getSearchView().addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.setSearchText(svCustomerSearch.getSearchView().getText().toString());
            }
        });
    }

    @Override
    public void initSearchResults(List<Customer> searchResults, String searchText) {
        customerListAdapter.setSearchResult(searchResults, searchText);
    }

    @Override
    public void updateList() {
        presenter.initData();
    }

    @Override
    public void setDebtListVisibility(int visibility) {
       customerDebtConnection.setCustomerDebtListVisibility(visibility);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        customerDebtConnection.setCustomerListView(null);
    }
}
