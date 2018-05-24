package com.jim.multipos.ui.settings.common;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class CommonConfigPresenterImpl extends BasePresenterImpl<CommonConfigView> implements CommonConfigPresenter {

    private final DatabaseManager databaseManager;
    private Context context;

    @Inject
    protected CommonConfigPresenterImpl(CommonConfigView commonConfigView, DatabaseManager databaseManager, Context context) {
        super(commonConfigView);
        this.databaseManager = databaseManager;
        this.context = context;
    }

    @Override
    public void changeDefaultsLanguage() {
        for (Account account : databaseManager.getAccounts()) {
            if (account.getStaticAccountType() == Account.CASH_ACCOUNT) {
                account.setName(context.getString(R.string.till));
                databaseManager.addAccount(account).subscribe();
            }
        }
        for (PaymentType paymentType : databaseManager.getPaymentTypes()) {
            if (paymentType.getTypeStaticPaymentType() == PaymentType.CASH_PAYMENT_TYPE) {
                paymentType.setName(context.getString(R.string.cash));
                databaseManager.addPaymentType(paymentType).subscribe();
            }
        }
        Account account = databaseManager.getSystemAccount().blockingGet();
        PaymentType paymentType = databaseManager.getSystemPaymentType().blockingGet();
        account.setName(context.getString(R.string.debt_report));
        databaseManager.addAccount(account).subscribe();
        paymentType.setName(context.getString(R.string.debt_report));
        databaseManager.addPaymentType(paymentType).subscribe();
        Map<Integer, Integer> titles = new HashMap<>();
        titles.put(0, R.array.piece_title);
        titles.put(1, R.array.weight_title);
        titles.put(2, R.array.length_title);
        titles.put(3, R.array.area_title);
        titles.put(4, R.array.volume_title);
        Map<Integer, Integer> abbrs = new HashMap<>();
        abbrs.put(0, R.array.piece_abbr);
        abbrs.put(1, R.array.weight_abbr);
        abbrs.put(2, R.array.length_abbr);
        abbrs.put(3, R.array.area_abbr);
        abbrs.put(4, R.array.volume_abbr);
        Map<Integer, Integer> factors = new HashMap<>();
        factors.put(0, R.array.piece_factor);
        factors.put(1, R.array.weight_factor);
        factors.put(2, R.array.length_factor);
        factors.put(3, R.array.area_factor);
        factors.put(4, R.array.volume_factor);
        List<UnitCategory> tempUnitCategories = databaseManager.getAllUnitCategories().blockingSingle();
        String[] categories = context.getResources().getStringArray(R.array.unit_categories);
        for (int i = 0; i < tempUnitCategories.size(); i++) {
            UnitCategory unitCategory = tempUnitCategories.get(i);
            unitCategory.setName(categories[i]);
            databaseManager.addUnitCategory(unitCategory).subscribe();
            String[] unitTitles = context.getResources().getStringArray(titles.get(i));
            String[] unitAbbrs = context.getResources().getStringArray(abbrs.get(i));
            String[] unitFactors = context.getResources().getStringArray(factors.get(i));
            for (int j = 0; j < unitCategory.getUnits().size(); j++) {
                Unit unit = unitCategory.getUnits().get(j);
                unit.setName(unitTitles[j]);
                unit.setAbbr(unitAbbrs[j]);
                unit.setFactorRoot(Float.valueOf(unitFactors[j]));
                databaseManager.addUnit(unit).subscribe();
            }
        }
    }
}
