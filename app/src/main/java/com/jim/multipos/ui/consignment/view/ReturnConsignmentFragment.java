package com.jim.multipos.ui.consignment.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.consignment.adapter.IncomeItemsListAdapter;
import com.jim.multipos.ui.consignment.adapter.VendorItemsListAdapter;
import com.jim.multipos.ui.consignment.presenter.ReturnConsignmentPresenter;
import com.jim.multipos.utils.BarcodeStack;
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

import static com.jim.multipos.ui.consignment.ConsignmentActivity.CONSIGNMENT_ID;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.PRODUCT_ID;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.VENDOR_ID;

/**
 * Created by Sirojiddin on 24.11.2017.
 */

public class ReturnConsignmentFragment extends BaseFragment implements ReturnConsignmentView {

    @Inject
    ReturnConsignmentPresenter presenter;
    @Inject
    IncomeItemsListAdapter itemsListAdapter;
    @Inject
    VendorItemsListAdapter vendorItemsListAdapter;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    RxBus rxBus;
    @Inject
    BarcodeStack barcodeStack;
    @BindView(R.id.rvReturnProducts)
    RecyclerView rvReturnProducts;
    @NotEmpty(messageId = R.string.consignment_number_is_empty)
    @BindView(R.id.etReturnNumber)
    MpEditText etReturnNumber;
    @BindView(R.id.etReturnDescription)
    EditText etReturnDescription;
    @BindView(R.id.tvTotalReturnSum)
    TextView tvTotalReturnSum;
    @BindView(R.id.tvVendorName)
    TextView tvVendorName;
    @BindView(R.id.tvCurrencyAbbr)
    TextView tvCurrencyAbbr;
    private Dialog dialog;


    @Override
    protected int getLayout() {
        return R.layout.return_consignment_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            Long productId = (Long) getArguments().get(PRODUCT_ID);
            Long vendorId = (Long) getArguments().get(VENDOR_ID);
            Long consignmentId = (Long) getArguments().get(CONSIGNMENT_ID);
            presenter.setData(productId, vendorId, consignmentId);
        }
        rvReturnProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReturnProducts.setAdapter(itemsListAdapter);
        ((SimpleItemAnimator) rvReturnProducts.getItemAnimator()).setSupportsChangeAnimations(false);
        dialog = new Dialog(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.vendor_product_list_dialog, null, false);
        RecyclerView rvProductList = (RecyclerView) dialogView.findViewById(R.id.rvProductList);
        rvProductList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProductList.setAdapter(vendorItemsListAdapter);
        dialog.setContentView(dialogView);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        vendorItemsListAdapter.setListener(product -> {
            presenter.setReturnItem(product);
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
        barcodeStack.register(barcode -> {
            if (isAdded() && isVisible())
                presenter.onBarcodeScaned(barcode);
        });
    }

    @OnClick(R.id.btnBack)
    public void onBack() {
        presenter.checkChanges(etReturnNumber.getText().toString(), etReturnDescription.getText().toString());
    }

    @OnClick(R.id.btnAddProductToReturnConsignment)
    public void onAddProductToReturnConsignment() {
        dialog.show();
        presenter.loadVendorProducts();
    }

    @OnClick(R.id.btnAddReturn)
    public void onAddReturn() {
        if (isValid())
            presenter.saveReturnConsignment(etReturnNumber.getText().toString(), etReturnDescription.getText().toString());
    }

    @Override
    public void fillReturnList(List<ConsignmentProduct> consignmentProductList, int type) {
        itemsListAdapter.setData(consignmentProductList, type);
    }

    @Override
    public void setTotalProductsSum(double sum) {
        tvTotalReturnSum.setText(decimalFormat.format(sum));
    }

    @Override
    public void fillDialogItems(List<Product> productList) {
        vendorItemsListAdapter.setData(productList);
        vendorItemsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setError(String string) {
        WarningDialog warningDialog = new WarningDialog(getContext());
        warningDialog.onlyText(true);
        warningDialog.setWarningMessage(getString(R.string.add_product_to_consignment));
        warningDialog.setOnYesClickListener(view1 -> warningDialog.dismiss());
        warningDialog.show();
    }

    @Override
    public void setVendorName(String name) {
        tvVendorName.setText(name);
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
    public void fillConsignmentData(String consignmentNumber, String description) {
        etReturnNumber.setText(consignmentNumber);
        etReturnDescription.setText(description);
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
        warningDialog.setOnNoClickListener(view -> {
            warningDialog.dismiss();
            getActivity().finish();
        });
        warningDialog.show();
    }

    @Override
    public void setConsignmentNumberError() {
        etReturnNumber.setError("Consignment with such number exists");
    }

    @Override
    public void onDetach() {
        barcodeStack.unregister();
        super.onDetach();
    }

    @Override
    public void setConsignmentNumber(int number) {
        etReturnNumber.setText(String.valueOf(number));
    }

    @Override
    public void setCurrency(String abbr) {
        tvCurrencyAbbr.setText(abbr);
    }
}
