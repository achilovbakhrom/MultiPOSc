package com.jim.multipos.ui.customers_edit;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;

import com.jim.multipos.R;
import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomers;
import com.jim.multipos.data.operations.CustomerGroupOperations;
import com.jim.multipos.data.operations.CustomerOperations;
import com.jim.multipos.data.operations.JoinCustomerGroupWithCustomerOperations;
import com.jim.multipos.ui.customers_edit.connector.CustomersEditConnector;
import com.jim.multipos.ui.customers_edit.entity.CustomersWithCustomerGroups;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CustomerEvent;
import com.jim.multipos.utils.rxevents.GlobalEventsConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 02.09.17.
 */

public class CustomersEditPresenterImpl extends CustomersEditConnector implements CustomersEditPresenter {
    private Context context;
    private List<Customer> customers;
    private List<CustomerGroup> customerGroups;
    private CustomersEditView view;
    private CustomerOperations operations;
    private CustomerGroupOperations customerGroupOperations;
    private JoinCustomerGroupWithCustomerOperations joinCustomerGroupWithCustomerOperations;
    private long maxValue = -1;
    private RxBus rxBus;
    private List<CustomerGroup> selectedCustomerGroups;
    private List<CustomersWithCustomerGroups> customersWithCustomerGroups;
    private int rvSelectedItemPosition;
    private List<CustomersWithCustomerGroups> currentRVItems;
    private List<CustomerGroup> spinnerCustomerGroups;

    public CustomersEditPresenterImpl(RxBus rxBus, RxBusLocal rxBusLocal, Context context, CustomersEditView view, CustomerOperations operations, CustomerGroupOperations customerGroupOperations, JoinCustomerGroupWithCustomerOperations joinCustomerGroupWithCustomerOperations) {
        this.rxBus = rxBus;
        this.context = context;
        this.view = view;
        this.operations = operations;
        this.customerGroupOperations = customerGroupOperations;
        this.joinCustomerGroupWithCustomerOperations = joinCustomerGroupWithCustomerOperations;
        selectedCustomerGroups = new ArrayList<>();
        customersWithCustomerGroups = new ArrayList<>();
        rvSelectedItemPosition = 0;
        currentRVItems = new ArrayList<>();
        spinnerCustomerGroups = new ArrayList<>();

        initConnectors(rxBus, rxBusLocal);
    }

    @Override
    public void showCustomerGroups() {
        getCustomerGroups();
        view.updateCustomerGroupDialog();
    }

    @Override
    public void getQrCode() {
        view.showQrCode(getQr());
    }

    @Override
    public void refreshQrCode(int position) {
        customers.get(position).setQrCode(getQr());

        view.updateRecyclerView();
    }

    @Override
    public void getClientId() {
        view.showClientId(getId());
    }

    private String getId() {
        if (maxValue == -1) {
            if (!customers.isEmpty()) {
                maxValue = customers.get(0).getClientId();

                for (Customer c : customers) {
                    if (c.getClientId() > maxValue)
                        maxValue = c.getClientId();
                }

                maxValue++;
            } else {
                maxValue = 1;
            }
        } else {
            maxValue++;
        }

        return String.valueOf(maxValue);
    }

    @Override
    public void getCustomers() {
        operations.getAllCustomers().subscribe(customers -> {
            this.customers = customers;

            sortCustomers();

            for (Customer customer : customers) {
                operations.getCustomerGroups(customer).subscribe(customerGroups -> {
                    CustomersWithCustomerGroups cc = new CustomersWithCustomerGroups();
                    cc.setCustomer(customer);
                    cc.setCustomerGroups(customerGroups);

                    customersWithCustomerGroups.add(cc);
                });
            }

            filterCustomerGroups(-1);
        });
    }

    @Override
    public void getCustomerGroups() {
        customerGroupOperations.getAllCustomerGroups().subscribe(customerGroups -> {
            this.customerGroups = customerGroups;

            spinnerCustomerGroups.clear();
            spinnerCustomerGroups.addAll(customerGroups);

            CustomerGroup customerGroup = new CustomerGroup();
            customerGroup.setName("All");
            customerGroup.setId("-1");
            spinnerCustomerGroups.add(0, customerGroup);

            view.showCustomerGroups(spinnerCustomerGroups);
        });
    }

    @Override
    public void addCustomer(String clientId, String fullName, String phone, String address, String qrCode) {
        boolean hasError = checkData(fullName, phone, address);

        if (!hasError) {
            Customer customer = new Customer();
            customer.setClientId(Long.parseLong(clientId));
            customer.setName(fullName);
            customer.setPhoneNumber(phone);
            customer.setAddress(address);
            customer.setQrCode(qrCode);

            operations.addCustomer(customer).subscribe(aLong -> {
                List<JoinCustomerGroupsWithCustomers> joinCustomerGroupsWithCustomersList = new ArrayList<>();
                rxBus.send(new CustomerEvent(customer, GlobalEventsConstants.ADD));
                customers.add(0, customer);

                for (CustomerGroup customerGroup : selectedCustomerGroups) {
                    JoinCustomerGroupsWithCustomers joinCustomerGroupsWithCustomers = new JoinCustomerGroupsWithCustomers();
                    joinCustomerGroupsWithCustomers.setCustomerId(customer.getId());
                    joinCustomerGroupsWithCustomers.setCustomerGroupId(customerGroup.getId());

                    joinCustomerGroupsWithCustomersList.add(joinCustomerGroupsWithCustomers);

                    joinCustomerGroupWithCustomerOperations.addJoinCustomerGroupWithCustomers(joinCustomerGroupsWithCustomersList).subscribe();
                }

                customersWithCustomerGroups.add(0, new CustomersWithCustomerGroups(customer, new ArrayList<>(selectedCustomerGroups)));
                currentRVItems.add(0, customersWithCustomerGroups.get(0));

                view.clearViews();
                view.updateRecyclerView();
                view.showClientId(getId());
                view.showQrCode(getQr());
                view.requestFocus();
            });
        }
    }

    @Override
    public void removeCustomer(int position) {
        Customer customer = customers.get(position);

        operations.removeCustomer(customer).subscribe(aBoolean -> {
            rxBus.send(new CustomerEvent(customer, GlobalEventsConstants.DELETE));
            customersWithCustomerGroups.remove(position);
            customers.remove(position);
            currentRVItems.remove(position);
        });

        view.updateRecyclerView();
    }

    private boolean checkData(String fullName, String phone, String address) {
        boolean hasError = false;

        if (fullName.isEmpty()) {
            hasError = true;
            view.showFullNameError(getString(R.string.enter_full_name));
        }

        if (phone.isEmpty()) {
            hasError = true;
            view.showPhoneError(getString(R.string.enter_phone));
        }

        if (address.isEmpty()) {
            hasError = true;
            view.showAddressError(getString(R.string.enter_address));
        }

        return hasError;
    }

    @Override
    public void saveCustomer(int position, String fullName, String phone, String address, String qrCode) {
        Customer customer = currentRVItems.get(position).getCustomer();

        Customer cTemp = new Customer();
        cTemp.setId(customer.getId());
        cTemp.setClientId(customer.getClientId());
        cTemp.setName(fullName);
        cTemp.setPhoneNumber(phone);
        cTemp.setAddress(address);
        cTemp.setQrCode(qrCode);

        operations.addCustomer(cTemp).subscribe(aLong -> {
            customer.setName(fullName);
            customer.setPhoneNumber(phone);
            customer.setAddress(address);
            customer.setQrCode(qrCode);
            rxBus.send(new CustomerEvent(customer, GlobalEventsConstants.UPDATE));
        });
    }

    @Override
    public void getDialogData() {
        view.showCustomerGroupDialog(customerGroups, selectedCustomerGroups);
    }

    @Override
    public void save() {
        view.save();
    }

    @Override
    public void cancel() {
        view.cancel();
    }

    private String getString(int resId) {
        return context.getString(resId);
    }

    private String getQr() {
        return String.valueOf(System.currentTimeMillis());
    }

    @Override
    public void customerGroupsSelected(List<CustomerGroup> selectedItems) {
        this.selectedCustomerGroups = selectedItems;

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < selectedItems.size(); i++) {
            str.append(selectedItems.get(i).getName());

            if (i < selectedItems.size() - 1) {
                str.append(", ");
            }
        }


        view.showSelectedCustomerGroups(str.toString());
    }

    @Override
    public void getCustomerCustomerGroups(int position) {
        if (position == -1) {
            position = rvSelectedItemPosition;
        }

        rvSelectedItemPosition = position;

        List<CustomerGroup> cg = customersWithCustomerGroups.get(position).getCustomerGroups();

        view.showRVItemCustomerGroupDialog(position, customerGroups, cg);
    }

    @Override
    public void updateCustomerCustomerGroup(int position, List<CustomerGroup> selectedItems) {
        CustomersWithCustomerGroups customersWithCustomerGroups = this.customersWithCustomerGroups.get(position);
        List<CustomerGroup> customerGroups = customersWithCustomerGroups.getCustomerGroups();
        List<JoinCustomerGroupsWithCustomers> joinCustomerGroupsWithCustomersList = new ArrayList<>();
        List<CustomerGroup> tempCustomerGroups = new ArrayList<>();

        joinCustomerGroupWithCustomerOperations.removeJoinCustomerGroupWithCustomer(customers.get(position).getId()).subscribe(aBoolean -> {
            for (CustomerGroup cg : selectedItems) {
                for (CustomerGroup cg1 : this.customerGroups) {
                    if (cg1.getId().equals(cg.getId()))
                        tempCustomerGroups.add(cg);
                }
            }

            for (CustomerGroup cg : tempCustomerGroups) {
                JoinCustomerGroupsWithCustomers joinCustomerGroupsWithCustomers = new JoinCustomerGroupsWithCustomers();
                joinCustomerGroupsWithCustomers.setCustomerGroupId(cg.getId());
                joinCustomerGroupsWithCustomers.setCustomerId(customers.get(position).getId());

                joinCustomerGroupsWithCustomersList.add(joinCustomerGroupsWithCustomers);

                joinCustomerGroupWithCustomerOperations.addJoinCustomerGroupWithCustomer(joinCustomerGroupsWithCustomers).subscribe();
            }

            customerGroups.clear();
            customerGroups.addAll(tempCustomerGroups);
            view.updateRecyclerView();
        });
    }

    private void sortCustomers() {
        Collections.sort(customers, (o1, o2) -> {
            int result = (int) (o1.getClientId() - o2.getClientId());

            if (result > 0) {
                result = -1;
            } else if (result < 0) {
                result = 1;
            }

            return result;
        });
    }

    @Override
    public void filterCustomerGroups(int position) {
        if (position == -1) {
            position = 0;
        }

        currentRVItems.clear();

        CustomerGroup customerGroup = spinnerCustomerGroups.get(position);

        if (0 == position && customerGroup.getId().equals("-1")) {
            currentRVItems.addAll(customersWithCustomerGroups);
        } else {
            for (CustomersWithCustomerGroups c : customersWithCustomerGroups) {
                for (CustomerGroup cg : c.getCustomerGroups()) {
                    if (cg.getName().equals(customerGroup.getName())) {
                        currentRVItems.add(c);
                        break;
                    }
                }
            }
        }

        view.showCustomersWithCustomerGroups(currentRVItems);
    }
}
