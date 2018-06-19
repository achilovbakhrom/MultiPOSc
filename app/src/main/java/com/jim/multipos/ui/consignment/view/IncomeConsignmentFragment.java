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
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.consignment.adapter.IncomeItemsListAdapter;
import com.jim.multipos.ui.consignment.adapter.VendorItemsListAdapter;
import com.jim.multipos.ui.consignment.dialogs.IncomeProductConfigsDialog;
import com.jim.multipos.ui.consignment.dialogs.ProductsForIncomeDialog;
import com.jim.multipos.ui.consignment.presenter.IncomeConsignmentPresenter;
import com.jim.multipos.utils.BarcodeStack;
import com.jim.multipos.utils.NumberTextWatcher;
import com.jim.multipos.utils.RxBus;
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
    DecimalFormat decimalFormat;
    @Inject
    BarcodeStack barcodeStack;
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
    @BindView(R.id.tvTotalShouldPayAbbr)
    TextView tvTotalShouldPayAbbr;
    @BindView(R.id.tvTotalPaidAbbr)
    TextView tvTotalPaidAbbr;

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
            presenter.setData(productId, vendorId);
        }
        rvConsignmentItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvConsignmentItems.setAdapter(itemsListAdapter);
        ((SimpleItemAnimator) rvConsignmentItems.getItemAnimator()).setSupportsChangeAnimations(false);
        chbFromAccount.setTextSize(16);

        itemsListAdapter.setListeners(new IncomeItemsListAdapter.OnInvoiceCallback() {
            @Override
            public void onDelete(int position) {
                WarningDialog warningDialog = new WarningDialog(getContext());
                warningDialog.setWarningMessage(getContext().getString(R.string.do_you_want_delete));
                warningDialog.setOnYesClickListener(view1 -> {
                    presenter.deleteFromList(position);
                    itemsListAdapter.notifyItemRemoved(position);
                    warningDialog.dismiss();
                });
                warningDialog.setOnNoClickListener(view -> warningDialog.dismiss());
                warningDialog.show();
            }

            @Override
            public void onSettings(IncomeProduct incomeProduct, int position) {
                presenter.openSettingsDialogForProduct(incomeProduct, position);
            }

            @Override
            public void onSumChanged() {
                presenter.calculateInvoiceSum();
            }
        });

        chbFromAccount.setCheckedChangeListener(isChecked -> {
            if (isChecked) {
                llAccounts.setVisibility(View.VISIBLE);
            } else llAccounts.setVisibility(View.GONE);
        });
        barcodeStack.register(barcode -> {
            if (isAdded() && isVisible())
                presenter.onBarcodeScanned(barcode);
        });
        etTotalPaid.addTextChangedListener(new NumberTextWatcher(etTotalPaid));
    }

    @OnClick(R.id.btnBack)
    public void onBack() {
        presenter.checkChanges(etConsignmentNumber.getText().toString(), etConsignmentDescription.getText().toString(), etTotalPaid.getText().toString(), chbFromAccount.isChecked(), spAccounts.getSelectedPosition());
    }

    @OnClick(R.id.btnAddProductToConsignment)
    public void onAddProductsToConsignment() {
        presenter.loadVendorProducts();
    }

    @OnClick(R.id.btnAddConsignment)
    public void onAddConsignment() {
        if (isValid()) {
            presenter.saveInvoice(etConsignmentNumber.getText().toString(), etConsignmentDescription.getText().toString(), tvTotalShouldPay.getText().toString(), etTotalPaid.getText().toString(), chbFromAccount.isChecked(), spAccounts.getSelectedPosition());
        }
    }

    @Override
    public void fillDialogItems(List<Product> productList, List<Product> vendorProducts) {
        ProductsForIncomeDialog dialog = new ProductsForIncomeDialog(getContext(), productList, vendorProducts);
        dialog.setListener(product -> presenter.setInvoiceItem(product));
        dialog.show();
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
    public void closeFragment(Vendor vendor) {
        rxBus.send(new ConsignmentWithVendorEvent(vendor, GlobalEventConstants.UPDATE));
        rxBus.send(new BillingOperationEvent(GlobalEventConstants.BILLING_IS_DONE));
        rxBus.send(new InventoryStateEvent(GlobalEventConstants.UPDATE));
        getActivity().finish();
    }

    @Override
    public void setInvoiceNumberError() {
        etConsignmentNumber.setError(getString(R.string.consignment_with_such_number_exists));
    }

    @Override
    public void setInvoiceNumber(int number) {
        etConsignmentNumber.setText(String.valueOf(number));
    }

    @Override
    public void setCurrency(String abbr) {
        tvTotalPaidAbbr.setText(abbr);
        tvTotalShouldPayAbbr.setText(abbr);
    }

    @Override
    public void fillInvoiceProductList(List<IncomeProduct> incomeProductList) {
        itemsListAdapter.setData(incomeProductList);
    }

    @Override
    public void openSettingsDialog(IncomeProduct incomeProduct, StockQueue stockQueue, int position) {
        IncomeProductConfigsDialog dialog = new IncomeProductConfigsDialog(getContext(), stockQueue, incomeProduct);
        dialog.setListener((incomeProduct1, stockQueue1) -> presenter.setConfigsToProduct(incomeProduct1, stockQueue1, position));
        dialog.show();
    }

    @Override
    public void onDetach() {
        barcodeStack.unregister();
        super.onDetach();
    }
}
