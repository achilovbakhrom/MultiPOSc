package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpSearchView;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Return;
import com.jim.multipos.ui.mainpospage.adapter.ProductSearchResultsAdapter;
import com.jim.multipos.ui.mainpospage.adapter.ReturnsAdapter;
import com.jim.multipos.ui.mainpospage.adapter.ReturnsListAdapter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.WarningDialog;
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
        rvReturnProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReturnsListAdapter(decimalFormat, context);
        rvReturnProducts.setAdapter(adapter);
        ((SimpleItemAnimator) rvReturnProducts.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.setData(returnsList);
        double totalAmount = 0;
        for (int i = 0; i < returnsList.size(); i++) {
            totalAmount += returnsList.get(i).getReturnAmount() * returnsList.get(i).getQuantity();
        }
        tvTotalReturnSum.setText(decimalFormat.format(totalAmount) + " " + databaseManager.getMainCurrency().getAbbr());
        btnBack.setOnClickListener(view -> {
            UIUtils.closeKeyboard(btnBack, context);
            ReturnsDialog dialog = new ReturnsDialog(context, databaseManager, decimalFormat, returnsList, rxBus);
            dialog.show();
            dismiss();
        });

        btnConfirm.setOnClickListener(view -> {
            UIUtils.closeKeyboard(btnConfirm, context);
            if (!etDescription.getText().toString().isEmpty()) {
                for (int i = 0; i < returnsList.size(); i++) {
                    returnsList.get(i).setDescription(etDescription.getText().toString());
                }
            }
            databaseManager.insertReturns(returnsList).subscribe();
            for (int i = 0; i < returnsList.size(); i++) {
                WarehouseOperations operations = new WarehouseOperations();
                operations.setType(WarehouseOperations.RETURN_SOLD);
                operations.setValue(returnsList.get(i).getQuantity());
                operations.setCreateAt(System.currentTimeMillis());
                operations.setProduct(returnsList.get(i).getProduct());
                operations.setVendor(returnsList.get(i).getVendor());
                databaseManager.insertWarehouseOperation(operations).subscribe();
            }
            rxBus.send(new InventoryStateEvent(GlobalEventConstants.UPDATE));
            dismiss();
        });
    }
}
