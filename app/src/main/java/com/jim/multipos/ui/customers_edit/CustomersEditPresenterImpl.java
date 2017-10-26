package com.jim.multipos.ui.customers_edit;

import android.util.Log;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomers;
import com.jim.multipos.ui.customers_edit.entity.CustomersWithCustomerGroups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by user on 02.09.17.
 */

public class CustomersEditPresenterImpl extends BasePresenterImpl<CustomersEditView> implements CustomersEditPresenter {
    /*@Inject
    RxBus rxBus;*/
    private List<Customer> customers;
    private List<CustomerGroup> customerGroups;
    private CustomersEditView view;
    private long maxValue = -1;
    private List<CustomerGroup> selectedCustomerGroups;
    private List<CustomersWithCustomerGroups> customersWithCustomerGroups;
    private int rvSelectedItemPosition;
    private List<CustomersWithCustomerGroups> currentRVItems;
    private List<CustomerGroup> spinnerCustomerGroups;
    private DatabaseManager databaseManager;
    private String enterFullName;
    private String enterPhone;
    private String enterAddress;
    private List<CustomerGroup> customerTypeSelectedCustomerGroups;

    @Inject
    public CustomersEditPresenterImpl(CustomersEditView view, DatabaseManager databaseManager,
                                      @Named(value = "enter_full_name") String enterFullName,
                                      @Named(value = "enter_phone") String enterPhone,
                                      @Named(value = "enter_address") String enterAddress) {
        super(view);
        this.view = view;
        this.databaseManager = databaseManager;
        this.enterFullName = enterFullName;
        this.enterPhone = enterPhone;
        this.enterAddress = enterAddress;
        selectedCustomerGroups = new ArrayList<>();
        customerTypeSelectedCustomerGroups = new ArrayList<>();
        customersWithCustomerGroups = new ArrayList<>();
        rvSelectedItemPosition = 0;
        currentRVItems = new ArrayList<>();
        spinnerCustomerGroups = new ArrayList<>();
    }

    @Override
    public void showCustomerGroups() {
        getCustomerGroups();
        view.updateCustomerGroupDialog();
    }

    /*@Override
    public void getQrCode() {
        view.showQrCode(getQr());
    }*/

    @Override
    public void refreshQrCode(int position) {
        customers.get(position).setQrCode(getQr());

        view.updateRecyclerView();
    }

    @Override
    public void refreshQrCode() {
        view.updateCustomerTypeQrCode(getQr());
    }
/*@Override
    public void getClientId() {
        view.showClientId(getId());
    }*/

    @Override
    public void showClientId() {
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
        databaseManager.getCustomerOperations().getAllCustomers().subscribe(customers -> {
            this.customers = customers;

            sortCustomers(customers);

            for (Customer customer : customers) {
                databaseManager.getCustomerOperations().getCustomerGroups(customer).subscribe(customerGroups -> {
                    CustomersWithCustomerGroups cc = new CustomersWithCustomerGroups();
                    cc.setCustomer(customer);
                    cc.setCustomerGroups(customerGroups);

                    customersWithCustomerGroups.add(cc);
                });
            }

            //filterCustomerGroups(-1);

            view.showCustomersWithCustomerGroups(customerTypeSelectedCustomerGroups, customersWithCustomerGroups, getId(), getQr());
        });
    }

    @Override
    public void getCustomerGroups() {
        databaseManager.getCustomerGroupOperations().getAllCustomerGroups().subscribe(customerGroups -> {
            this.customerGroups = customerGroups;

            spinnerCustomerGroups.clear();
            spinnerCustomerGroups.addAll(customerGroups);

            CustomerGroup customerGroup = new CustomerGroup();
            customerGroup.setName("All");
            customerGroup.setId(-1L);
            spinnerCustomerGroups.add(0, customerGroup);

            view.showCustomerGroups(spinnerCustomerGroups);
        });
    }

    @Override
    public void addCustomer(String clientId, String fullName, String phone, String address, String qrCode, List<CustomerGroup> selectedGroups) {
        //boolean hasError = checkData(fullName, phone, address);

        //if (!hasError) {
        Customer customer = new Customer();
        customer.setClientId(Long.parseLong(clientId));
        customer.setName(fullName);
        customer.setPhoneNumber(phone);
        customer.setAddress(address);
        customer.setQrCode(qrCode);

        databaseManager.getCustomerOperations().addCustomer(customer).subscribe(aLong -> {
            List<JoinCustomerGroupsWithCustomers> joinCustomerGroupsWithCustomersList = new ArrayList<>();
            //rxBus.send(new CustomerEvent(customer, GlobalEventsConstants.ADD));
            customers.add(0, customer);

            for (CustomerGroup customerGroup : selectedGroups) {
                JoinCustomerGroupsWithCustomers joinCustomerGroupsWithCustomers = new JoinCustomerGroupsWithCustomers();
                joinCustomerGroupsWithCustomers.setCustomerId(customer.getId());
                joinCustomerGroupsWithCustomers.setCustomerGroupId(customerGroup.getId());

                joinCustomerGroupsWithCustomersList.add(joinCustomerGroupsWithCustomers);

                databaseManager.getJoinCustomerGroupWithCustomerOperations().addJoinCustomerGroupWithCustomers(joinCustomerGroupsWithCustomersList).subscribe();
            }

            customersWithCustomerGroups.add(0, new CustomersWithCustomerGroups(customer, new ArrayList<>(selectedGroups)));
            currentRVItems.add(0, customersWithCustomerGroups.get(0));


            view.updateRecyclerView();
                /*view.clearViews();
                view.updateRecyclerView();
                view.showClientId(getId());
                view.showQrCode(getQr());
                view.requestFocus();*/
        });
        //}
    }

    @Override
    public void removeCustomer(int position) {
        Customer customer = customers.get(position);

        databaseManager.getCustomerOperations().removeCustomer(customer).subscribe(aBoolean -> {
            //rxBus.send(new CustomerEvent(customer, GlobalEventsConstants.DELETE));
            customersWithCustomerGroups.remove(position);
            customers.remove(position);
            currentRVItems.remove(position);
        });

        view.updateRecyclerView();
    }

    /*private boolean checkData(String fullName, String phone, String address) {
        boolean hasError = false;

        if (fullName.isEmpty()) {
            hasError = true;
            view.showFullNameError(enterFullName);
        }

        if (phone.isEmpty()) {
            hasError = true;
            view.showPhoneError(enterPhone);
        }

        if (address.isEmpty()) {
            hasError = true;
            view.showAddressError(enterAddress);
        }

        return hasError;
    }*/

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

        databaseManager.getCustomerOperations().addCustomer(cTemp).subscribe(aLong -> {
            customer.setName(fullName);
            customer.setPhoneNumber(phone);
            customer.setAddress(address);
            customer.setQrCode(qrCode);
            //rxBus.send(new CustomerEvent(customer, GlobalEventsConstants.UPDATE));
        });
    }

    @Override
    public void showCustomerGroupDialog() {
        view.showCustomerGroupDialog(customerGroups, selectedCustomerGroups);
    }

    @Override
    public void save() {
        view.save();
    }

    @Override
    public void back() {
        view.cancel();
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

        //view.showSelectedCustomerGroups(str.toString());
    }

    @Override
    public void getCustomerCustomerGroups(int position) {
        List<CustomerGroup> cg = null;

        if (position == -1) {
            position = rvSelectedItemPosition;
        }

        rvSelectedItemPosition = position;

        if (position == 0) {
            cg = customerTypeSelectedCustomerGroups;
        } else {
            cg = customersWithCustomerGroups.get(position - 1).getCustomerGroups();
        }

        view.showRVItemCustomerGroupDialog(position, customerGroups, cg);
    }

    @Override
    public void getCustomerCustomerGroups() {
        //TODO
        view.showRVItemCustomerGroupDialog(customerGroups, customerTypeSelectedCustomerGroups);
    }

    @Override
    public void updateCustomerCustomerGroup(int position, List<CustomerGroup> selectedItems) {
        //TODO IT AFTER ENTITY FIX
        if (position != 0) {
            CustomersWithCustomerGroups customersWithCustomerGroups = this.customersWithCustomerGroups.get(position - 1);
            List<CustomerGroup> customerGroups = customersWithCustomerGroups.getCustomerGroups();
            List<JoinCustomerGroupsWithCustomers> joinCustomerGroupsWithCustomersList = new ArrayList<>();
            List<CustomerGroup> tempCustomerGroups = new ArrayList<>();

            databaseManager.getJoinCustomerGroupWithCustomerOperations().removeJoinCustomerGroupWithCustomer(customers.get(position - 1).getId()).subscribe(aBoolean -> {
                for (CustomerGroup cg : selectedItems) {
                    for (CustomerGroup cg1 : this.customerGroups) {
                        if (cg1.getId() == cg.getId())
                            tempCustomerGroups.add(cg);
                    }
                }

                for (CustomerGroup cg : tempCustomerGroups) {
                    JoinCustomerGroupsWithCustomers joinCustomerGroupsWithCustomers = new JoinCustomerGroupsWithCustomers();
                    joinCustomerGroupsWithCustomers.setCustomerGroupId(cg.getId());
                    joinCustomerGroupsWithCustomers.setCustomerId(customers.get(position - 1).getId());

                    joinCustomerGroupsWithCustomersList.add(joinCustomerGroupsWithCustomers);

                    databaseManager.getJoinCustomerGroupWithCustomerOperations().addJoinCustomerGroupWithCustomer(joinCustomerGroupsWithCustomers).subscribe();
                }

                customerGroups.clear();
                customerGroups.addAll(tempCustomerGroups);
                //view.updateRecyclerView();

                databaseManager.getCustomerOperations().getAllCustomers().subscribe(customers -> {
                    this.customers.clear();
                    this.customersWithCustomerGroups.clear();
                    this.customers.addAll(customers);

                    sortCustomers(customers);

                    for (Customer customer2 : customers) {
                        databaseManager.getCustomerOperations().getCustomerGroups(customer2).subscribe(customerGroups2 -> {
                            CustomersWithCustomerGroups cc = new CustomersWithCustomerGroups();
                            cc.setCustomer(customer2);
                            cc.setCustomerGroups(customerGroups2);

                            this.customersWithCustomerGroups.add(cc);
                        });
                    }

                    //filterCustomerGroups(-1);
                    view.updateRecyclerView(this.customersWithCustomerGroups);
                });
            });

            //filterCustomerGroups(-1);
        } else {
            view.updateRecyclerView();
        }
    }

    @Override
    public void updateCustomerCustomerGroup(List<CustomerGroup> selectedItems) {
        /*this.customerTypeSelectedCustomerGroups.clear();
        this.customerTypeSelectedCustomerGroups.addAll(selectedItems);

        Log.d("myLogs", "Count of selected items: " + customerTypeSelectedCustomerGroups.size());*/
        view.updateRecyclerView();
    }

    private void sortCustomers(List<Customer> customers) {
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

        if (0 == position && customerGroup.getId() == -1) {
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

        //view.showCustomersWithCustomerGroups(customerTypeSelectedCustomerGroups, currentRVItems, getId(), getQr());

        view.updateRecyclerView(currentRVItems);
    }

    @Override
    public void sortByName() {
        Collections.sort(customersWithCustomerGroups, (o1, o2) -> o1.getCustomer().getName().compareTo(o2.getCustomer().getName()));

        view.updateRecyclerView(customersWithCustomerGroups);
    }

    @Override
    public void sortByPhone() {
        Collections.sort(customersWithCustomerGroups, (o1, o2) -> o1.getCustomer().getPhoneNumber().compareTo(o2.getCustomer().getPhoneNumber()));

        view.updateRecyclerView(customersWithCustomerGroups);
    }

    @Override
    public void sortByAddress() {
        Collections.sort(customersWithCustomerGroups, (o1, o2) -> o1.getCustomer().getAddress().compareTo(o2.getCustomer().getAddress()));

        view.updateRecyclerView(customersWithCustomerGroups);
    }

    @Override
    public void sortByIdDesc() {
        Collections.sort(customersWithCustomerGroups, (o1, o2) ->
                o2.getCustomer().getClientId().compareTo(o1.getCustomer().getClientId()));

        view.updateRecyclerView(customersWithCustomerGroups);
    }

    @Override
    public void sortByClientId() {
        Collections.sort(customersWithCustomerGroups, (o1, o2) -> o1.getCustomer().getClientId().compareTo(o2.getCustomer().getClientId()));

        view.updateRecyclerView(customersWithCustomerGroups);
    }

    @Override
    public void sortByQrCode() {
        Collections.sort(customersWithCustomerGroups, (o1, o2) -> o1.getCustomer().getQrCode().compareTo(o2.getCustomer().getQrCode()));

        view.updateRecyclerView(customersWithCustomerGroups);
    }

    @Override
    public boolean isCustomerExists(String name) {
        return databaseManager.getCustomerOperations().isCustomerExists(name).blockingSingle();
    }
}
