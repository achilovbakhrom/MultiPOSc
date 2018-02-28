package com.jim.multipos.ui.consignment.view;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.consignment.adapter.IncomeItemsListAdapter;
import com.jim.multipos.ui.consignment.adapter.VendorItemsListAdapter;
import com.jim.multipos.ui.consignment.presenter.IncomeConsignmentPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.rxevents.inventory_events.BillingOperationEvent;
import com.jim.multipos.utils.rxevents.inventory_events.ConsignmentWithVendorEvent;
import com.jim.multipos.utils.rxevents.inventory_events.InventoryStateEvent;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.CONSIGNMENT_ID;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.PRODUCT_ID;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.VENDOR_ID;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class IncomeConsignmentFragment extends BaseFragment implements IncomeConsignmentView {

    @Inject
    IncomeConsignmentPresenter presenter;
    @Inject
    IncomeItemsListAdapter itemsListAdapter;
    @Inject
    VendorItemsListAdapter vendorItemsListAdapter;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    RxBus rxBus;
    @BindView(R.id.etConsignmentDescription)
    EditText etConsignmentDescription;
    @BindView(R.id.tvVendorName)
    TextView tvVendorName;
    @BindView(R.id.rvConsignmentItems)
    RecyclerView rvConsignmentItems;
    @NotEmpty(messageId = R.string.consignment_number_is_empty)
    @BindView(R.id.etConsignmentNumber)
    EditText etConsignmentNumber;
    @NotEmpty(messageId = R.string.cannot_be_empty)
    @BindView(R.id.tvTotalShouldPay)
    TextView tvTotalShouldPay;
    @BindView(R.id.etTotalPaid)
    MpEditText etTotalPaid;
    @BindView(R.id.spAccounts)
    MPosSpinner spAccounts;
    @BindView(R.id.chbFromAccount)
    MpCheckbox chbFromAccount;
    @BindView(R.id.llAccounts)
    LinearLayout llAccounts;
    private Dialog dialog;
    private double sum = 0;
    public static final String CONSIGNMENT_UPDATE = "CONSIGNMENT_UPDATE";
    public static final String INVENTORY_STATE_UPDATE = "INVENTORY_STATE_UPDATE";


    @Override
    protected int getLayout() {
        return R.layout.income_fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            Long productId = (Long) getArguments().get(PRODUCT_ID);
            Long vendorId = (Long) getArguments().get(VENDOR_ID);
            Long consignmentId = (Long) getArguments().get(CONSIGNMENT_ID);
            presenter.setData(productId, vendorId, consignmentId);
        }
        rvConsignmentItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvConsignmentItems.setAdapter(itemsListAdapter);
        ((SimpleItemAnimator) rvConsignmentItems.getItemAnimator()).setSupportsChangeAnimations(false);
        chbFromAccount.setTextSize(16);
        dialog = new Dialog(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.vendor_product_list_dialog, null, false);
        RecyclerView rvProductList = dialogView.findViewById(R.id.rvProductList);
        rvProductList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProductList.setAdapter(vendorItemsListAdapter);
        dialog.setContentView(dialogView);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        vendorItemsListAdapter.setListener(product -> {
            presenter.setConsignmentItem(product);
            dialog.dismiss();
        });

        itemsListAdapter.setListeners(new IncomeItemsListAdapter.OnConsignmentCallback() {
            @Override
            public void onDelete(ConsignmentProduct consignmentProduct) {
                WarningDialog warningDialog = new WarningDialog(getContext());
                warningDialog.setWarningMessage(getContext().getString(R.string.do_you_want_delete));
                warningDialog.setOnYesClickListener(view1 -> {
                    presenter.deleteFromList(consignmentProduct);
                    itemsListAdapter.notifyDataSetChanged();
                    warningDialog.dismiss();
                });
                warningDialog.setOnNoClickListener(view -> warningDialog.dismiss());
                warningDialog.show();
            }

            @Override
            public void onSumChanged() {
                presenter.calculateConsignmentSum();
            }
        });

        chbFromAccount.setCheckedChangeListener(isChecked -> {
            if (isChecked) {
                llAccounts.setVisibility(View.VISIBLE);
            } else llAccounts.setVisibility(View.GONE);
        });
        etTotalPaid.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    double paid_sum = 0;
                    try {
                        paid_sum = Double.parseDouble(etTotalPaid.getText().toString());
                    } catch (Exception e) {
                        etTotalPaid.setError(getContext().getString(R.string.invalid));
                        return;
                    }
                    if (paid_sum > sum) {
                        etTotalPaid.setError(getString(R.string.total_paid_sum_warning));
                    }
                }
            }
        });
    }

    @OnClick(R.id.btnBack)
    public void onBack() {
        presenter.checkChanges(etConsignmentNumber.getText().toString(), etConsignmentDescription.getText().toString(), etTotalPaid.getText().toString(), chbFromAccount.isChecked(), spAccounts.getSelectedPosition());
    }

    @OnClick(R.id.btnAddProductToConsignment)
    public void onAddProductsToConsignment() {
        dialog.show();
        presenter.loadVendorProducts();
    }

    @OnClick(R.id.btnAddConsignment)
    public void onAddConsignment() {
        if (isValid()) {
            presenter.saveConsignment(etConsignmentNumber.getText().toString(), etConsignmentDescription.getText().toString(), tvTotalShouldPay.getText().toString(), etTotalPaid.getText().toString(), chbFromAccount.isChecked(), spAccounts.getSelectedPosition());
        }
    }

    @Override
    public void fillDialogItems(List<Product> productList) {
        vendorItemsListAdapter.setData(productList);
        vendorItemsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void fillConsignmentProductList(List<ConsignmentProduct> consignmentProductList, int type) {
        itemsListAdapter.setData(consignmentProductList, type);
    }

    @Override
    public void setVendorName(String name) {
        tvVendorName.setText(name);
    }

    @Override
    public void fillAccountsList(List<String> accountList) {
        spAccounts.setAdapter(accountList);
    }

    @Override
    public void setConsignmentSumValue(double sum) {
        this.sum = sum;
        tvTotalShouldPay.setText(decimalFormat.format(sum));
    }

    @Override
    public void setError(String string) {
        WarningDialog warningDialog = new WarningDialog(getContext());
        warningDialog.onlyText(true);
        warningDialog.setWarningMessage(string);
        warningDialog.setOnYesClickListener(view1 -> warningDialog.dismiss());
        warningDialog.show();
    }

    @Override
    public void openDiscardDialog() {
        WarningDialog warningDialog = new WarningDialog(getContext());
        warningDialog.setWarningMessage(getString(R.string.warning_discard_changes));
        warningDialog.setDialogTitle(getString(R.string.discard_changes));
        warningDialog.setOnYesClickListener(view1 -> {
            warningDialog.dismiss();
            getActivity().finish();
        });
        warningDialog.setOnNoClickListener(view -> warningDialog.dismiss());
        warningDialog.show();
    }

    @Override
    public void openSaveChangesDialog() {
        WarningDialog warningDialog = new WarningDialog(getContext());
        warningDialog.setWarningMessage(getString(R.string.do_you_want_to_save_the_change));
        warningDialog.setDialogTitle(getString(R.string.warning));
        warningDialog.setOnYesClickListener(view1 -> {
            presenter.saveChanges();
            warningDialog.dismiss();
        });
        warningDialog.setOnNoClickListener(view -> warningDialog.dismiss());
        warningDialog.show();
    }

    @Override
    public void closeFragment(Vendor vendor) {
        rxBus.send(new ConsignmentWithVendorEvent(vendor, GlobalEventConstants.UPDATE));
        rxBus.send(new BillingOperationEvent(GlobalEventConstants.BILLING_IS_DONE));
        rxBus.send(new InventoryStateEvent(GlobalEventConstants.UPDATE));
        getActivity().finish();
    }

    @Override
    public void fillConsignmentData(String consignmentNumber, String description, Boolean isFromAccount, double amount) {
        etConsignmentNumber.setText(consignmentNumber);
        etConsignmentDescription.setText(description);
        chbFromAccount.setChecked(isFromAccount);
        if (isFromAccount) {
            llAccounts.setVisibility(View.VISIBLE);
        } else llAccounts.setVisibility(View.GONE);
        etTotalPaid.setText(String.valueOf(amount));
    }

    @Override
    public void setAccountSpinnerSelection(int selectedAccount) {
        spAccounts.setSelectedPosition(selectedAccount);
    }
}
