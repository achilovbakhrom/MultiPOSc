package com.jim.multipos.ui.cash_management.view;

import android.os.Bundle;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.ui.cash_management.connection.CashManagementConnection;
import com.jim.multipos.ui.cash_management.presenter.CashDetailsPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;
import com.jim.multipos.utils.rxevents.main_order_events.OrderEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public class CashDetailsFragment extends BaseFragment implements CashDetailsView {

    @BindView(R.id.tvStartingCash)
    TextView tvStartingCash;
    @BindView(R.id.tvPayOuts)
    TextView tvPayOuts;
    @BindView(R.id.tvPayIns)
    TextView tvPayIns;
    @BindView(R.id.tvPayToVendors)
    TextView tvPayToVendors;
    @BindView(R.id.tvIncomeDebt)
    TextView tvIncomeDebt;
    @BindView(R.id.tvBankDrops)
    TextView tvBankDrops;
    @BindView(R.id.tvCashTransactions)
    TextView tvCashTransactions;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvExpectedCash)
    TextView tvExpectedCash;

    @Inject
    CashDetailsPresenter presenter;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    CashManagementConnection connection;
    @Inject
    RxBus rxBus;

    private ArrayList<Disposable> subscriptions;

    @Override
    protected int getLayout() {
        return R.layout.cash_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setCashDetailsView(this);
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof OrderEvent) {
                        OrderEvent event = (OrderEvent) o;
                        switch (event.getType()) {
                            case GlobalEventConstants.CANCEL:
                                presenter.updateDetails();
                                break;
                            case GlobalEventConstants.CLOSE:
                                presenter.updateDetails();
                                break;
                        }
                    }
                }));
    }

    public void setData(Account account, Till till) {
        presenter.calculateCashDetails(account, till);
    }

    @Override
    public void fillTillDetails(double totalStartingCash, double payOut, double payIn, double payToVendor, double incomeDebt, double bankDrop, double expectedCash, double tips, double cashTransactions) {
        tvStartingCash.setText(decimalFormat.format(totalStartingCash));
        tvPayOuts.setText(decimalFormat.format(payOut));
        tvPayIns.setText(decimalFormat.format(payIn));
        tvPayToVendors.setText(decimalFormat.format(payToVendor));
        tvIncomeDebt.setText(decimalFormat.format(incomeDebt));
        tvBankDrops.setText(decimalFormat.format(bankDrop));
        tvCashTransactions.setText(decimalFormat.format(cashTransactions));
        tvTips.setText(decimalFormat.format(tips));
        tvExpectedCash.setText(decimalFormat.format(expectedCash));
    }

    @Override
    public void updateDetails() {
        presenter.updateDetails();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        connection.setCashDetailsView(null);
        RxBus.removeListners(subscriptions);
    }
}
