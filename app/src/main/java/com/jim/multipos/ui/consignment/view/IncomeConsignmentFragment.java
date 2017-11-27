package com.jim.multipos.ui.consignment.view;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.jim.multipos.ui.consignment.adapter.IncomeItemsListAdapter;
import com.jim.multipos.ui.consignment.adapter.VendorItemsListAdapter;
import com.jim.multipos.ui.consignment.presenter.IncomeConsignmentPresenter;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.WarningDialog;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

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
    @BindView(R.id.etConsignmentDescription)
    EditText etConsignmentDescription;
    @BindView(R.id.tvVendorName)
    TextView tvVendorName;
    @BindView(R.id.rvConsignmentItems)
    RecyclerView rvConsignmentItems;
    @NotEmpty(messageId = R.string.consignment_number_is_empty)
    @BindView(R.id.etConsignmentNumber)
    EditText etConsignmentNumber;
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
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String VENDOR_ID = "VENDOR_ID";
    @Override
    protected int getLayout() {
        return R.layout.income_fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void init(Bundle savedInstanceState) {
        if (getArguments() != null){
            Long productId = (Long) getArguments().get(PRODUCT_ID);
            Long vendorId = (Long) getArguments().get(VENDOR_ID);
            presenter.setData(productId, vendorId);
        }
        rvConsignmentItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvConsignmentItems.setAdapter(itemsListAdapter);
        ((SimpleItemAnimator) rvConsignmentItems.getItemAnimator()).setSupportsChangeAnimations(false);
        chbFromAccount.setTextSize(16);
        dialog = new Dialog(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.vendor_product_list_dialog, null, false);
        RecyclerView rvProductList = (RecyclerView) dialogView.findViewById(R.id.rvProductList);
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
        getActivity().finish();
    }

    @OnClick(R.id.btnAddProductToConsignment)
    public void onAddProductsToConsignment() {
        dialog.show();
        presenter.loadVendorProducts();
    }

    @OnClick(R.id.btnAddConsignment)
    public void onAddConsignment() {
        if (isValid())
            presenter.saveConsignment(etConsignmentNumber.getText().toString(), etConsignmentDescription.getText().toString(), tvTotalShouldPay.getText().toString(), chbFromAccount.isChecked(), spAccounts.getSelectedPosition());
    }

    @Override
    public void fillDialogItems(List<Product> productList) {
        vendorItemsListAdapter.setData(productList);
        vendorItemsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void fillConsignmentProductList(List<ConsignmentProduct> consignmentProductList) {
        itemsListAdapter.setData(consignmentProductList);
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
    public void setError() {
        WarningDialog warningDialog = new WarningDialog(getContext());
        warningDialog.onlyText(true);
        warningDialog.setWarningMessage(getString(R.string.add_product_to_consignment));
        warningDialog.setOnYesClickListener(view1 -> warningDialog.dismiss());
        warningDialog.show();
    }
}
