package com.jim.multipos.ui.cash_management.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpSwitcher;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.ui.cash_management.adapter.OpenTillAdapter;
import com.jim.multipos.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 13.01.2018.
 */

public class OpenTillDialog extends Dialog {

    private OpenTillAdapter adapter;
    @BindView(R.id.swConfirmModifyTill)
    MpSwitcher swConfirmModifyTill;
    @BindView(R.id.tvModification)
    TextView tvModification;
    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.rvOpenAmount)
    RecyclerView rvOpenAmount;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;
    @BindView(R.id.btnConfirm)
    MpButton btnConfirm;
    private List<TillManagementOperation> operations, tempList;

    public OpenTillDialog(@NonNull Context context, DatabaseManager databaseManager, onTillOpenCallback callback) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.open_till_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        swConfirmModifyTill.setLeft(true);
        tvModification.setVisibility(View.GONE);
        etDescription.setVisibility(View.GONE);
        rvOpenAmount.setLayoutManager(new LinearLayoutManager(context));
        List<Account> accountList = databaseManager.getAccounts();
        operations = new ArrayList<>();
        tempList = new ArrayList<>();

        boolean isNoTill = databaseManager.isNoTills().blockingGet();
        if (!isNoTill) {
            for (int i = 0; i < accountList.size(); i++) {
                TillManagementOperation operation = new TillManagementOperation();
                operation.setAccount(accountList.get(i));
                operation.setType(TillManagementOperation.OPENED_WITH);
                operation.setAmount(0.d);
                operations.add(operation);
            }
        } else {
            Till closedTill = databaseManager.getLastClosedTill().blockingGet();
            databaseManager.getTillManagementOperationsByTillId(closedTill.getId()).subscribe(tillManagementOperations -> {

                for (int i = 0; i < tillManagementOperations.size(); i++) {
                    if (tillManagementOperations.get(i).getType() == TillManagementOperation.TO_NEW_TILL) {
                        operations.add(tillManagementOperations.get(i));
                        TillManagementOperation operation = new TillManagementOperation();
                        operation.setAccount(tillManagementOperations.get(i).getAccount());
                        operation.setAmount(tillManagementOperations.get(i).getAmount());
                        tempList.add(operation);
                    }
                }
            });
        }


        adapter = new OpenTillAdapter(context, operations, databaseManager.getMainCurrency());
        adapter.setLeft(swConfirmModifyTill.isLeft());
        rvOpenAmount.setAdapter(adapter);

        swConfirmModifyTill.setSwitcherStateChangedListener((isRight, isLeft) -> {
            if (isRight) {
                tvModification.setVisibility(View.VISIBLE);
                etDescription.setVisibility(View.VISIBLE);
                operations.clear();
                if (!isNoTill) {
                    for (int i = 0; i < accountList.size(); i++) {
                        TillManagementOperation operation = new TillManagementOperation();
                        operation.setAccount(accountList.get(i));
                        operation.setAmount(0.d);
                        operations.add(operation);
                    }
                } else {
                    for (int i = 0; i < tempList.size(); i++) {
                        TillManagementOperation operation = new TillManagementOperation();
                        operation.setAccount(tempList.get(i).getAccount());
                        operation.setAmount(tempList.get(i).getAmount());
                        operations.add(operation);
                    }
                }
            } else {
                operations.clear();
                for (int i = 0; i < tempList.size(); i++) {
                    TillManagementOperation operation = new TillManagementOperation();
                    operation.setAccount(tempList.get(i).getAccount());
                    operation.setType(TillManagementOperation.OPENED_WITH);
                    operation.setAmount(tempList.get(i).getAmount());
                    operations.add(operation);
                }
                tvModification.setVisibility(View.GONE);
                etDescription.setVisibility(View.GONE);
            }
            adapter.setData(operations);
            adapter.setLeft(isLeft);
        });

        btnRevert.setOnClickListener(view -> {
            UIUtils.closeKeyboard(btnRevert, context);
            dismiss();
        });

        btnConfirm.setOnClickListener(view -> {
            if (isValid()) {
                Till till = new Till();
                till.setStatus(Till.OPEN);
                till.setOpenDate(System.currentTimeMillis());
                databaseManager.insertTill(till).subscribe((till1, throwable) -> {
                    for (TillManagementOperation managementOperation : operations) {
                        TillManagementOperation operation = new TillManagementOperation();
                        operation.setAccount(managementOperation.getAccount());
                        operation.setType(TillManagementOperation.OPENED_WITH);
                        operation.setAmount(managementOperation.getAmount());
                        operation.setTill(till);
                        operation.setDescription(etDescription.getText().toString());
                        databaseManager.insertTillManagementOperation(operation).subscribe();
                    }
                });
                UIUtils.closeKeyboard(btnRevert, context);
                callback.onTillOpen(Till.OPEN);
                dismiss();
            }
        });
    }

    private boolean isValid() {
        int count = 0;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAmount() == null) {
                return false;
            } else count++;
        }
        return count == operations.size();
    }

    public interface onTillOpenCallback {
        void onTillOpen(int status);
    }
}
