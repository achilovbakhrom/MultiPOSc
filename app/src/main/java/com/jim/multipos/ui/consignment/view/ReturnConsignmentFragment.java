package com.jim.multipos.ui.consignment.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.consignment.adapter.ReturnItemsListAdapter;
import com.jim.multipos.ui.consignment.dialogs.ProductsForIncomeDialog;
import com.jim.multipos.ui.consignment.dialogs.StockPositionsDialog;
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

import static com.jim.multipos.ui.consignment.ConsignmentActivity.PRODUCT_ID;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.VENDOR_ID;

/**
 * Created by Sirojiddin on 24.11.2017.
 */

public class ReturnConsignmentFragment extends BaseFragment implements ReturnConsignmentView {

    @Inject
    ReturnConsignmentPresenter presenter;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    RxBus rxBus;
    @Inject
    BarcodeStack barcodeStack;
    @Inject
    ReturnItemsListAdapter adapter;
    @Inject
    DatabaseManager databaseManager;
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
    @BindView(R.id.ivBarcodeScanner)
    ImageView ivBarcodeScanner;
    private ProductsForIncomeDialog dialog;
    private boolean forDialog = false;

    @Override
    protected int getLayout() {
        return R.layout.return_consignment_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            Long productId = (Long) getArguments().get(PRODUCT_ID);
            Long vendorId = (Long) getArguments().get(VENDOR_ID);
            presenter.setData(productId, vendorId);
        }
        rvReturnProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReturnProducts.setAdapter(adapter);
        ((SimpleItemAnimator) rvReturnProducts.getItemAnimator()).setSupportsChangeAnimations(false);

        adapter.setListener(new ReturnItemsListAdapter.OnOutComeItemSelectListener() {
            @Override
            public void onSumChanged() {
                presenter.calculateConsignmentSum();
            }

            @Override
            public void onDelete(int position) {
                WarningDialog warningDialog = new WarningDialog(getContext());
                warningDialog.setWarningMessage(getContext().getString(R.string.do_you_want_delete));
                warningDialog.setOnYesClickListener(view1 -> {
                    presenter.deleteFromList(position);
                    adapter.notifyDataSetChanged();
                    warningDialog.dismiss();
                });
                warningDialog.setOnNoClickListener(view -> warningDialog.dismiss());
                warningDialog.show();
            }

            @Override
            public void onCustomStock(OutcomeProduct outcomeProduct, int position) {
                presenter.openCustomStockPositionsDialog(outcomeProduct, position);
            }
        });

        barcodeStack.register(barcode -> {
            if (isAdded() && isVisible())
                presenter.onBarcodeScanned(barcode);
        });

        ivBarcodeScanner.setOnClickListener(v -> initScan());
    }

    @OnClick(R.id.btnBack)
    public void onBack() {
        presenter.checkChanges(etReturnNumber.getText().toString(), etReturnDescription.getText().toString());
    }

    @OnClick(R.id.btnAddProductToReturnConsignment)
    public void onAddProductToReturnConsignment() {
        presenter.loadVendorProducts();
    }

    @OnClick(R.id.btnAddReturn)
    public void onAddReturn() {
        if (isValid())
            presenter.saveReturnConsignment(etReturnNumber.getText().toString(), etReturnDescription.getText().toString());
    }

    @Override
    public void setTotalProductsSum(double sum) {
        tvTotalReturnSum.setText(decimalFormat.format(sum));
    }

    @Override
    public void fillDialogItems(List<Product> productList, List<Product> vendorProducts) {
        dialog = new ProductsForIncomeDialog(getContext(), productList, vendorProducts, barcodeStack);
        dialog.setListener(new ProductsForIncomeDialog.OnSelectClickListener() {
            @Override
            public void onSelect(Product product) {
                presenter.setReturnItem(product);
            }

            @Override
            public void onBarcodeClick() {
                initScan();
            }
        });
        dialog.show();
    }

    private void initScan() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                if (forDialog) {
                    dialog.setBarcode(intentResult.getContents());
                } else presenter.onBarcodeScanned(intentResult.getContents());
            }
        }
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
    public void setConsignmentNumberError() {
        etReturnNumber.setError(getString(R.string.consignment_with_such_number_exists));
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

    @Override
    public void fillReturnList(List<OutcomeProduct> outcomeProduct) {
        adapter.setItems(outcomeProduct);
    }

    @Override
    public void openCustomStockPositionsDialog(OutcomeProduct outcomeProduct, List<OutcomeProduct> outcomeProductList, List<OutcomeProduct> exceptionList,int position) {
        StockPositionsDialog dialog = new StockPositionsDialog(getContext(), outcomeProduct, outcomeProductList, exceptionList, databaseManager);
        dialog.setListener(new StockPositionsDialog.OnStockPositionsChanged() {
            @Override
            public void onConfirm(OutcomeProduct outcomeProduct) {
                presenter.setOutcomeProduct(outcomeProduct);
            }
        });
        dialog.show();
    }

    @Override
    public void updateChangedPosition(int position) {
        adapter.notifyItemChanged(position);
    }
}
