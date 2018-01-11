package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jim.mpviews.FloatingSearchView;
import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.suggestions.model.SearchSuggestion;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.ui.mainpospage.model.CustomerSuggestion;
import com.jim.multipos.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 05.01.2018.
 */

public class AddDebtDialog extends Dialog {

    @BindView(R.id.flSearchView)
    FloatingSearchView flSearchView;
    @BindView(R.id.spDebtType)
    MPosSpinner spDebtType;
    @BindView(R.id.etAmount)
    MpEditText etAmount;
    @BindView(R.id.tvDueDate)
    TextView tvDueDate;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    @BindView(R.id.etFee)
    MpEditText etFee;
    private List<Customer> customerList;
    private List<CustomerSuggestion> suggestionsList, foundSuggestions;
    private Customer customer;
    private onDebtSaveClickListener listener;
    private String searchText;

    public AddDebtDialog(@NonNull Context context, Customer item, DatabaseManager databaseManager, Order order, onDebtSaveClickListener listener) {
        super(context);
        this.customer = item;
        this.listener = listener;
        View dialogView = getLayoutInflater().inflate(R.layout.debt_details_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        suggestionsList = new ArrayList<>();
        customerList = databaseManager.getAllCustomers().blockingSingle();
        for (Customer customer1 : customerList) {
            CustomerSuggestion suggestions = new CustomerSuggestion();
            suggestions.setCustomer(customer1);
            suggestionsList.add(suggestions);
        }
        if (customer != null) {
            flSearchView.setSearchText(customer.getName());
        }
        flSearchView.setBackground(context.getDrawable(R.drawable.edit_text_bg));
        flSearchView.setDimBackground(false);
        flSearchView.setQueryTextSize(16);
        flSearchView.requestFocus();
        flSearchView.setQueryTextColor(ContextCompat.getColor(context, R.color.colorMainText));
        flSearchView.setSuggestionsTextColor(ContextCompat.getColor(context, R.color.colorMainText));
        flSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
            if (!oldQuery.equals("") && newQuery.equals("")) {
                flSearchView.clearSuggestions();
                foundSuggestions = null;
                customer = null;
            } else {
                flSearchView.showProgress();
                foundSuggestions = new ArrayList<>();
                for (int j = 0; j < suggestionsList.size(); j++) {
                    if (suggestionsList.get(j).getBody().toUpperCase().contains(newQuery.toUpperCase())) {
                        foundSuggestions.add(suggestionsList.get(j));
                        continue;
                    }
                    if (suggestionsList.get(j).getCustomer().getClientId().toString().toUpperCase().contains(newQuery.toUpperCase())) {
                        foundSuggestions.add(suggestionsList.get(j));
                    }
                }
                searchText = newQuery;
                flSearchView.swapSuggestions(foundSuggestions);
                flSearchView.hideProgress();
            }
        });

        flSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                CustomerSuggestion customerSuggestion = (CustomerSuggestion) searchSuggestion;
                flSearchView.setSearchText(customerSuggestion.getBody());
                customer = customerSuggestion.getCustomer();
                flSearchView.clearSuggestions();
                etAmount.requestFocus();
            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });
        String[] debtTypes = context.getResources().getStringArray(R.array.debt_type);
        etFee.setText(String.valueOf(0));
        spDebtType.setAdapter(debtTypes);
        tvDueDate.setText(simpleDateFormat.format(calendar.getTime()));
        GregorianCalendar now = new GregorianCalendar();
        tvDueDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                if (calendar.before(now))
                    tvDueDate.setText(simpleDateFormat.format(now.getTime()));
                else
                    tvDueDate.setText(simpleDateFormat.format(calendar.getTime()));

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        btnBack.setOnClickListener(view -> {
            UIUtils.closeKeyboard(btnBack, context);
            dismiss();
        });

        btnSave.setOnClickListener(view -> {
            if (customer == null) {
                flSearchView.setQueryError("Please, choose customer");
            } else if (!customer.getName().equals(searchText)) {
                flSearchView.setQueryError("Please, choose customer");
            } else if (etAmount.getText().toString().isEmpty()) {
                etAmount.setError("Please, enter debt amount");
            } else {
                if (etFee.getText().toString().isEmpty()) {
                    etFee.setText(String.valueOf(0));
                }
                Debt debt = new Debt();
                debt.setCustomer(customer);
                debt.setStatus(Debt.ACTIVE);
                debt.setTakenDate(now.getTimeInMillis());
                debt.setFee(Double.parseDouble(etFee.getText().toString()));
                debt.setDebtAmount(Double.parseDouble(etAmount.getText().toString()));
                debt.setEndDate(calendar.getTimeInMillis());
                debt.setDebtType(spDebtType.getSelectedPosition());
                debt.setOrder(order);
                listener.onDebtSave(debt);
                UIUtils.closeKeyboard(btnSave, context);
                dismiss();
            }
        });
    }

    public interface onDebtSaveClickListener {
        void onDebtSave(Debt debt);
    }

}
