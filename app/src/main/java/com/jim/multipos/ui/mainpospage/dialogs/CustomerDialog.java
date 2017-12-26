package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpSearchView;
import com.jim.mpviews.RecyclerViewWithMaxHeight;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.ui.billing_vendor.adapter.BillingInfoAdapter;
import com.jim.multipos.ui.mainpospage.adapter.CustomersListAdapter;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.UIUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment.SortingStates.FILTERED_BY_CONSIGNMENT;
import static com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment.SortingStates.FILTERED_BY_DATE;
import static com.jim.multipos.ui.mainpospage.dialogs.CustomerDialog.CustomerSortingStates.SORTED_BY_ADDRESS;
import static com.jim.multipos.ui.mainpospage.dialogs.CustomerDialog.CustomerSortingStates.SORTED_BY_ADDRESS_INVERT;
import static com.jim.multipos.ui.mainpospage.dialogs.CustomerDialog.CustomerSortingStates.SORTED_BY_ID;
import static com.jim.multipos.ui.mainpospage.dialogs.CustomerDialog.CustomerSortingStates.SORTED_BY_ID_INVERT;
import static com.jim.multipos.ui.mainpospage.dialogs.CustomerDialog.CustomerSortingStates.SORTED_BY_NAME;
import static com.jim.multipos.ui.mainpospage.dialogs.CustomerDialog.CustomerSortingStates.SORTED_BY_NAME_INVERT;
import static com.jim.multipos.ui.mainpospage.dialogs.CustomerDialog.CustomerSortingStates.SORTED_BY_PHONE;
import static com.jim.multipos.ui.mainpospage.dialogs.CustomerDialog.CustomerSortingStates.SORTED_BY_PHONE_INVERT;

/**
 * Created by developer on 04.12.2017.
 */

public class CustomerDialog extends Dialog {

    @BindView(R.id.svCustomerSearch)
    MpSearchView svCustomerSearch;
    @BindView(R.id.rvCustomersList)
    RecyclerView rvCustomersList;
    @BindView(R.id.llClientId)
    LinearLayout llClientId;
    @BindView(R.id.llCustomerName)
    LinearLayout llCustomerName;
    @BindView(R.id.llCustomerAddress)
    LinearLayout llCustomerAddress;
    @BindView(R.id.llCustomerContacts)
    LinearLayout llCustomerContacts;
    @BindView(R.id.ivIdSort)
    ImageView ivIdSort;
    @BindView(R.id.ivCustomerNameSort)
    ImageView ivCustomerNameSort;
    @BindView(R.id.ivAddressSort)
    ImageView ivAddressSort;
    @BindView(R.id.ivContactsSort)
    ImageView ivContactsSort;
    @BindView(R.id.btnAddNewCustomer)
    MpButton btnAddNewCustomer;
    private CustomersListAdapter customersListAdapter;
    private List<Customer> customerList, searchResults;

    public enum CustomerSortingStates {
        SORTED_BY_ID, SORTED_BY_ID_INVERT, SORTED_BY_NAME, SORTED_BY_NAME_INVERT, SORTED_BY_PHONE, SORTED_BY_PHONE_INVERT, SORTED_BY_ADDRESS, SORTED_BY_ADDRESS_INVERT
    }

    private CustomerSortingStates filterMode = SORTED_BY_ID;

    public CustomerDialog(@NonNull Context context, DatabaseManager databaseManager) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.choose_customers_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        UIUtils.closeKeyboard(svCustomerSearch, context);
        customerList = new ArrayList<>();
        customerList = databaseManager.getAllCustomers().blockingSingle();
        rvCustomersList.setLayoutManager(new LinearLayoutManager(context));
        customersListAdapter = new CustomersListAdapter();
        sortList();
        rvCustomersList.setAdapter(customersListAdapter);
        customersListAdapter.setData(customerList);
        customersListAdapter.setCallback(new CustomersListAdapter.OnCustomerListItemCallback() {
            @Override
            public void onItemEdit(Customer customer) {
                AddCustomerDialog addCustomerDialog = new AddCustomerDialog(context, customer, databaseManager, () -> {
                    customerList = databaseManager.getAllCustomers().blockingSingle();
                    sortList();
                    customersListAdapter.setData(customerList);
                });
                addCustomerDialog.show();
            }

            @Override
            public void onItemDelete(Customer customer) {

            }
        });

        btnAddNewCustomer.setOnClickListener(view -> {
            AddCustomerDialog addCustomerDialog = new AddCustomerDialog(context, null, databaseManager, () -> {
                customerList = databaseManager.getAllCustomers().blockingSingle();
                sortList();
                customersListAdapter.setData(customerList);
            });
            addCustomerDialog.show();
        });

        llClientId.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_ID) {
                filterMode = SORTED_BY_ID;
                sortList();
                ivIdSort.setVisibility(View.VISIBLE);
                ivIdSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_ID_INVERT;
                ivIdSort.setVisibility(View.VISIBLE);
                ivIdSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });
        llCustomerName.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_NAME) {
                filterMode = SORTED_BY_NAME;
                sortList();
                ivCustomerNameSort.setVisibility(View.VISIBLE);
                ivCustomerNameSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_NAME_INVERT;
                ivCustomerNameSort.setVisibility(View.VISIBLE);
                ivCustomerNameSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });
        llCustomerAddress.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_ADDRESS) {
                filterMode = SORTED_BY_ADDRESS;
                sortList();
                ivAddressSort.setVisibility(View.VISIBLE);
                ivAddressSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_ADDRESS_INVERT;
                ivAddressSort.setVisibility(View.VISIBLE);
                ivAddressSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });
        llCustomerContacts.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_PHONE) {
                filterMode = SORTED_BY_PHONE;
                sortList();
                ivContactsSort.setVisibility(View.VISIBLE);
                ivContactsSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_PHONE_INVERT;
                ivContactsSort.setVisibility(View.VISIBLE);
                ivContactsSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });

        svCustomerSearch.getSearchView().addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                String searchText = svCustomerSearch.getSearchView().getText().toString();
                if (searchText.isEmpty()) {
                    searchResults = null;
                    sortList();
                    customersListAdapter.setData(customerList);
                } else {
                    searchResults = new ArrayList<>();
                    for (int i = 0; i < customerList.size(); i++) {
                        if (customerList.get(i).getName().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(customerList.get(i));
                            continue;
                        }
                        if (customerList.get(i).getClientId().toString().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(customerList.get(i));
                            continue;
                        }
                        if (customerList.get(i).getPhoneNumber().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(customerList.get(i));
                            continue;
                        }
                        if (customerList.get(i).getAddress().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(customerList.get(i));
                        }
                    }
                    sortList();
                    customersListAdapter.setSearchResult(searchResults, searchText);
                }
            }
        });
    }

    @OnClick(R.id.btnBack)
    public void onBackPressed() {
        dismiss();
    }

    private void deselectAll() {
        ivContactsSort.setVisibility(View.GONE);
        ivAddressSort.setVisibility(View.GONE);
        ivCustomerNameSort.setVisibility(View.GONE);
        ivIdSort.setVisibility(View.GONE);
    }

    private void sortList() {
        List<Customer> customerListTemp;
        if (searchResults != null)
            customerListTemp = searchResults;
        else customerListTemp = customerList;
        switch (filterMode) {
            case SORTED_BY_ADDRESS:
                Collections.sort(customerListTemp, (customer, t1) -> customer.getAddress().toUpperCase().compareTo(t1.getAddress().toUpperCase()));
                break;
            case SORTED_BY_ID:
                Collections.sort(customerListTemp, (customer, t1) -> customer.getClientId().compareTo(t1.getClientId()));
                break;
            case SORTED_BY_NAME:
                Collections.sort(customerListTemp, (customer, t1) -> customer.getName().toUpperCase().compareTo(t1.getName().toUpperCase()));
                break;
            case SORTED_BY_PHONE:
                Collections.sort(customerListTemp, (customer, t1) -> customer.getPhoneNumber().compareTo(t1.getPhoneNumber()));
                break;
            case SORTED_BY_ADDRESS_INVERT:
                Collections.sort(customerListTemp, (customer, t1) -> customer.getAddress().toUpperCase().compareTo(t1.getAddress().toUpperCase()) * -1);
                break;
            case SORTED_BY_ID_INVERT:
                Collections.sort(customerListTemp, (customer, t1) -> customer.getClientId().compareTo(t1.getClientId()) * -1);
                break;
            case SORTED_BY_NAME_INVERT:
                Collections.sort(customerListTemp, (customer, t1) -> customer.getName().toUpperCase().compareTo(t1.getName().toUpperCase()) * -1);
                break;
            case SORTED_BY_PHONE_INVERT:
                Collections.sort(customerListTemp, (customer, t1) -> customer.getPhoneNumber().compareTo(t1.getPhoneNumber()) * -1);
                break;
        }
        customersListAdapter.notifyDataSetChanged();
    }
}
