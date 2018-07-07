package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.products.Return;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillOperation;
import com.jim.multipos.ui.mainpospage.adapter.ReturnsListAdapter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.rxevents.inventory_events.InventoryStateEvent;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 06.01.2018.
 */

public class ReturnsConfirmDialog extends Dialog {

    @BindView(R.id.rvReturnProducts)
    RecyclerView rvReturnProducts;
    @BindView(R.id.btnConfirm)
    MpButton btnConfirm;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.tvTotalReturnSum)
    TextView tvTotalReturnSum;
    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.spReturnPaymentType)
    MPosSpinner spReturnPaymentType;
    private List<Return> returnsList;
    private ReturnsListAdapter adapter;

    public ReturnsConfirmDialog(Context context, List<Return> returnsList, DatabaseManager databaseManager, DecimalFormat decimalFormat, RxBus rxBus) {
        super(context);
        this.returnsList = returnsList;
        View dialogView = getLayoutInflater().inflate(R.layout.return_second_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        List<PaymentType> paymentTypeList = databaseManager.getPaymentTypes();
        List<String> strings = new ArrayList<>();
        for (PaymentType paymentType : paymentTypeList) {
            strings.add(paymentType.getName());
        }
        spReturnPaymentType.setAdapter(strings);
        rvReturnProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReturnsListAdapter(decimalFormat, context);
        rvReturnProducts.setAdapter(adapter);
        ((SimpleItemAnimator) rvReturnProducts.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.setData(this.returnsList);
        double totalAmount = 0;
        for (int i = 0; i < this.returnsList.size(); i++) {
            totalAmount += this.returnsList.get(i).getReturnAmount() * this.returnsList.get(i).getQuantity();
        }
        tvTotalReturnSum.setText(decimalFormat.format(totalAmount) + " " + databaseManager.getMainCurrency().getAbbr());
        btnBack.setOnClickListener(view -> {
            UIUtils.closeKeyboard(btnBack, context);
            ReturnsDialog dialog = new ReturnsDialog(context, databaseManager, decimalFormat, returnsList, rxBus);
            dialog.show();
            dismiss();
        });

        btnConfirm.setOnClickListener(view -> {
            boolean hasOpenTill = databaseManager.hasOpenTill().blockingGet();
            if (!hasOpenTill) {
                UIUtils.showAlert(getContext(), getContext().getString(R.string.ok), context.getString(R.string.warning), context.getString(R.string.opened_till_wnt_found_pls_open_till), () -> {
                });
            } else if (etDescription.getText().toString().isEmpty()){
                etDescription.setError(context.getString(R.string.enter_return_reason));
            } else  {
                Till till = databaseManager.getOpenTill().blockingGet();
                UIUtils.closeKeyboard(btnConfirm, context);
                for (int i = 0; i < returnsList.size(); i++) {
                    returnsList.get(i).setDescription(etDescription.getText().toString());
                    returnsList.get(i).setPaymentType(paymentTypeList.get(spReturnPaymentType.getSelectedPosition()));

                    IncomeProduct incomeProduct = new IncomeProduct();
                    incomeProduct.setProduct(returnsList.get(i).getProduct());
                    incomeProduct.setIncomeType(IncomeProduct.RETURNED_PRODUCT);
                    incomeProduct.setDescription(etDescription.getText().toString());
                    incomeProduct.setIncomeDate(System.currentTimeMillis());
                    incomeProduct.setCountValue(returnsList.get(i).getQuantity());
                    incomeProduct.setCostValue(returnsList.get(i).getProduct().getPrice());
                    databaseManager.insertIncomeProduct(incomeProduct).subscribe(incomeProduct1 -> {
                        StockQueue stockQueue = new StockQueue();
                        stockQueue.setAvailable(incomeProduct1.getCountValue());
                        stockQueue.setIncomeProduct(incomeProduct1);
                        stockQueue.setIncomeProductDate(System.currentTimeMillis());
                        stockQueue.setProduct(incomeProduct1.getProduct());
                        stockQueue.setCost(incomeProduct1.getCostValue());
                        databaseManager.insertStockQueue(stockQueue).subscribe();
                    });
                    TillOperation tillOperation = new TillOperation();
                    tillOperation.setAmount(returnsList.get(i).getReturnAmount() * returnsList.get(i).getQuantity());
                    tillOperation.setType(TillOperation.PAY_OUT);
                    tillOperation.setPaymentType(paymentTypeList.get(spReturnPaymentType.getSelectedPosition()));
                    tillOperation.setTill(till);
                    tillOperation.setCreateAt(System.currentTimeMillis());
                    tillOperation.setDescription(context.getString(R.string.return_) + " " + context.getString(R.string.product) + " " + context.getString(R.string.operation) + ": " + etDescription.getText().toString());
                    databaseManager.insertTillOperation(tillOperation).subscribe();
                }
                databaseManager.insertReturns(returnsList).subscribe();
                rxBus.send(new InventoryStateEvent(GlobalEventConstants.UPDATE));
                dismiss();
            }

        });
    }
}
