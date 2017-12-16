package com.jim.multipos.ui.billing_vendor.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.billing_vendor.BillingOperationsActivity;
import com.jim.multipos.ui.billing_vendor.adapter.BillingOperartionsAdapter;
import com.jim.multipos.ui.billing_vendor.presenter.BillingOperationPresenter;
import com.jim.multipos.utils.BillingInfoDialog;
import com.jim.multipos.utils.PaymentToVendorDialog;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.jim.multipos.utils.rxevents.MessageWithIdEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

import static com.jim.multipos.data.db.model.consignment.Consignment.INCOME_CONSIGNMENT;
import static com.jim.multipos.data.db.model.consignment.Consignment.RETURN_CONSIGNMENT;
import static com.jim.multipos.data.db.model.inventory.BillingOperations.DEBT_CONSIGNMENT;
import static com.jim.multipos.data.db.model.inventory.BillingOperations.RETURN_TO_VENDOR;
import static com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment.SortModes.DESCRIPTION;
import static com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment.SortModes.DESCRIPTION_INVERT;
import static com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment.SortModes.EXTRA;
import static com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment.SortModes.EXTRA_INVERT;
import static com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment.SortModes.OPERATION;
import static com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment.SortModes.OPERATION_INVERT;
import static com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment.SortModes.PAYMENT;
import static com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment.SortModes.PAYMENT_INVERT;
import static com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment.SortModes.TIME;
import static com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment.SortModes.TIME_INVERT;
import static com.jim.multipos.ui.consignment.view.IncomeConsignmentFragment.CONSIGNMENT_UPDATE;
import static com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment.BILLINGS_UPDATE;

/**
 * Created by developer on 30.11.2017.
 */

public class BillingOperationFragment extends BaseFragment implements BillingOperationView {
    @BindView(R.id.rvBilling)
    RecyclerView rvBilling;

    @Inject
    BillingOperartionsAdapter billingOperartionsAdapter;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    DatabaseManager databaseManager;


    @BindView(R.id.tvVendorName)
    TextView tvVendorName;
    @BindView(R.id.tvVendorCantact)
    TextView tvVendorCantact;
    @BindView(R.id.tvDebtAmmount)
    TextView tvDebtAmmount;
    @BindView(R.id.ivPay)
    ImageView ivPay;

    @BindView(R.id.llTimeDate)
    LinearLayout llTimeDate;
    @BindView(R.id.llOperation)
    LinearLayout llOperation;
    @BindView(R.id.llExtra)
    LinearLayout llExtra;
    @BindView(R.id.llDescription)
    LinearLayout llDescription;
    @BindView(R.id.llPayment)
    LinearLayout llPayment;
    Currency mainCurrency;
    @BindView(R.id.ivTimeDate)
    ImageView ivTimeDate;
    @BindView(R.id.ivOperation)
    ImageView ivOperation;
    @BindView(R.id.ivExtra)
    ImageView ivExtra;
    @BindView(R.id.ivDescription)
    ImageView ivDescription;
    @BindView(R.id.ivPayment)
    ImageView ivPayment;
    @BindView(R.id.tvDebtD)
    TextView tvDebtD;


    SortModes filterMode = TIME;
    @Inject
    BillingOperationPresenter presenter;
    @Inject
    RxBus rxBus;
    private ArrayList<Disposable> subscriptions;

    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof MessageWithIdEvent) {
                        MessageWithIdEvent event = (MessageWithIdEvent) o;
                        switch (event.getMessage()) {
                            case CONSIGNMENT_UPDATE: {
                                presenter.updateBillings();
                                break;
                            }
                        }
                    }
                }));
    }

    @Override
    public void setVendorData(Vendor vendor) {
        StringBuilder builder = new StringBuilder();

        for (Contact contact : vendor.getContacts()) {
            if (!builder.toString().isEmpty()) builder.append("  |  ");

            if (contact.getType() == Contact.E_MAIL) {
                builder.append(getString(R.string.email_two_dots));
                builder.append(" ");
                builder.append(contact.getName());
            } else if (contact.getType() == Contact.PHONE) {
                builder.append(getString(R.string.phone_two_dots));
                builder.append(" ");
                builder.append(contact.getName());
            }
        }
        if (!builder.toString().isEmpty())
            tvVendorCantact.setText(builder.toString());
        else if (!vendor.getContactName().isEmpty()) {
            builder.append(getString(R.string.contact_name));
            builder.append(": ");
            builder.append(vendor.getContactName());
            tvVendorCantact.setText(builder.toString());
        } else {
            tvVendorCantact.setVisibility(View.GONE);
        }

        tvVendorName.setText(vendor.getName());
        ivPay.setOnClickListener(view -> {
            PaymentToVendorDialog paymentToVendorDialog = new PaymentToVendorDialog(getContext(), vendor, new PaymentToVendorDialog.PaymentToVendorCallback() {
                @Override
                public void onChanged() {
                    presenter.updateBillings();
                }

                @Override
                public void onCancel() {

                }
            }, databaseManager,null);
            paymentToVendorDialog.show();
        });

    }

    @Override
    public void setTransactions(List<BillingOperations> transactions) {
        rvBilling.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBilling.setAdapter(billingOperartionsAdapter);
        billingOperartionsAdapter.setData(transactions, billingOperations -> {
            BillingInfoDialog infoDialog = new BillingInfoDialog(getContext(), billingOperations, databaseManager, (operations) -> {
                if (operations.getOperationType().equals(DEBT_CONSIGNMENT)) {
                    ((BillingOperationsActivity) getActivity()).openConsignment(operations.getConsignmentId(), INCOME_CONSIGNMENT);
                } else if (operations.getOperationType().equals(RETURN_TO_VENDOR)) {
                    ((BillingOperationsActivity) getActivity()).openConsignment(operations.getConsignmentId(), RETURN_CONSIGNMENT);
                } else {
                    PaymentToVendorDialog paymentToVendorDialog = new PaymentToVendorDialog(getContext(), operations.getVendor(), new PaymentToVendorDialog.PaymentToVendorCallback() {
                        @Override
                        public void onChanged() {
                            presenter.updateBillings();
                        }

                        @Override
                        public void onCancel() {

                        }
                    }, databaseManager, operations);
                    paymentToVendorDialog.show();
                }
            });
            infoDialog.show();
        });
    }

    @Override
    public void notifyListChange(List<BillingOperations> billingOperations) {
        billingOperartionsAdapter.setData(billingOperations);
        billingOperartionsAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyList() {
        billingOperartionsAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateDebt(Double debt) {
        totalDebt = debt;
        rxBus.send(new MessageEvent(BILLINGS_UPDATE));

        if(debt<=0){
            tvDebtD.setText(R.string.debt);
            tvDebtAmmount.setTextColor(Color.parseColor("#df4f4f"));
        }else {
            tvDebtD.setText(R.string.overpaid);
            tvDebtAmmount.setTextColor(Color.parseColor("#36a614"));

        }
        tvDebtAmmount.setText(decimalFormat.format(totalDebt)+" "+mainCurrency.getAbbr());


    }

    @Override
    public void notifyChange() {
        billingOperartionsAdapter.notifyDataSetChanged();
    }

    public enum SortModes{
        TIME,TIME_INVERT,OPERATION,OPERATION_INVERT,EXTRA,EXTRA_INVERT, DESCRIPTION, DESCRIPTION_INVERT, PAYMENT, PAYMENT_INVERT
   }

    @Override
    protected int getLayout() {
        return R.layout.billing_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.onCreateView(savedInstanceState);
        mainCurrency = databaseManager.getMainCurrency();
        presenter.findVendor(vendorId);
        llTimeDate.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != TIME) {
                filterMode = TIME;
                presenter.filterBy(TIME);
                ivTimeDate.setVisibility(View.VISIBLE);
                ivTimeDate.setImageResource(R.drawable.sorting);
            } else {
                filterMode = TIME_INVERT;
                ivTimeDate.setVisibility(View.VISIBLE);
                ivTimeDate.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llOperation.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != OPERATION) {
                filterMode = OPERATION;
                presenter.filterBy(OPERATION);
                ivOperation.setVisibility(View.VISIBLE);
                ivOperation.setImageResource(R.drawable.sorting);
            } else {
                filterMode = OPERATION_INVERT;
                ivOperation.setVisibility(View.VISIBLE);
                ivOperation.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llExtra.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != EXTRA) {
                filterMode = EXTRA;
                presenter.filterBy(EXTRA);
                ivExtra.setVisibility(View.VISIBLE);
                ivExtra.setImageResource(R.drawable.sorting);
            } else {
                filterMode = EXTRA_INVERT;
                ivExtra.setVisibility(View.VISIBLE);
                ivExtra.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llDescription.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != DESCRIPTION) {
                filterMode = DESCRIPTION;
                presenter.filterBy(DESCRIPTION);
                ivDescription.setVisibility(View.VISIBLE);
                ivDescription.setImageResource(R.drawable.sorting);
            } else {
                filterMode = DESCRIPTION_INVERT;
                ivDescription.setVisibility(View.VISIBLE);
                ivDescription.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llPayment.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != PAYMENT) {
                filterMode = PAYMENT;
                presenter.filterBy(PAYMENT);
                ivPayment.setVisibility(View.VISIBLE);
                ivPayment.setImageResource(R.drawable.sorting);
            } else {
                filterMode = PAYMENT_INVERT;
                ivPayment.setVisibility(View.VISIBLE);
                ivPayment.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        updateDebt(totalDebt);

    }

    private void deselectAll() {
        ivTimeDate.setVisibility(View.GONE);
        ivOperation.setVisibility(View.GONE);
        ivExtra.setVisibility(View.GONE);
        ivDescription.setVisibility(View.GONE);
        ivPayment.setVisibility(View.GONE);
    }

    Long vendorId;
    Double totalDebt;

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
    public void setTotalDebt(Double totalDebt) {this.totalDebt = totalDebt;}

    public void dateIntervalPicked(Calendar fromDate, Calendar toDate) {
        presenter.dateIntervalPicked(fromDate,toDate);
    }

    public void datePicked(Calendar pickedDate) {
        presenter.datePicked(pickedDate);
    }
    public void clearInterval(){
        presenter.clearIntervals();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.removeListners(subscriptions);
    }
}
