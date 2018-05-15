package com.jim.multipos.ui.start_configuration.selection_panel;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.utils.CompletionMode;
import com.jim.multipos.ui.start_configuration.account.AccountFragment;
import com.jim.multipos.ui.start_configuration.currency.CurrencyFragment;
import com.jim.multipos.ui.start_configuration.payment_type.PaymentTypeFragment;
import com.jim.multipos.ui.start_configuration.pos_data.PosDataFragment;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class SelectionPanelPresenterImpl extends BasePresenterImpl<SelectionPanelView> implements SelectionPanelPresenter {

    public static final String POS_DATA_KEY = PosDataFragment.class.getName();
    public static final String CURRENCY_KEY = CurrencyFragment.class.getName();
    public static final String ACCOUNT_KEY = AccountFragment.class.getName();
    public static final String PAYMENT_TYPE_KEY = PaymentTypeFragment.class.getName();
    private Map<String, Boolean> completion;

    @Inject
    protected SelectionPanelPresenterImpl(SelectionPanelView selectionPanelView) {
        super(selectionPanelView);
        completion = new HashMap<>();
        completion.put(POS_DATA_KEY, false);
        completion.put(CURRENCY_KEY, false);
        completion.put(ACCOUNT_KEY, false);
        completion.put(PAYMENT_TYPE_KEY, false);
    }

    @Override
    public void checkCompletionMode(int position) {
        CompletionMode mode = CompletionMode.NEXT;
        switch (position) {
            case 0:
                if (isLast(POS_DATA_KEY))
                    mode = CompletionMode.FINISH;
                view.sendMode(mode, position);
                break;
            case 1:
                if (isLast(CURRENCY_KEY))
                    mode = CompletionMode.FINISH;
                view.sendMode(mode, position);
                break;
            case 2:
                if (isLast(ACCOUNT_KEY))
                    mode = CompletionMode.FINISH;
                view.sendMode(mode, position);
                break;
            case 3:
                if (isLast(PAYMENT_TYPE_KEY))
                    mode = CompletionMode.FINISH;
                view.sendMode(mode, position);
                break;
        }
        view.showNextFragment(position);
    }

    @Override
    public void putCompletion(String key, boolean status) {
        completion.put(key, status);
    }

    private boolean isLast(String name) {
        if (completion != null) {
            int notCompletedCount = 0;
            for (String key : completion.keySet()) {
                if (notCompletedCount > 1) {
                    return false;
                }
                if (!completion.get(key))
                    notCompletedCount++;
            }
            if (notCompletedCount == 1 && !completion.get(name)) {
                return true;
            }
            if (notCompletedCount == 0) {
                return true;
            }
        }
        return false;
    }

}
