package com.jim.multipos.ui.mainpospage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpActionButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.customer.Debt;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 21.12.2017.
 */

public class CustomersListAdapter extends RecyclerView.Adapter<CustomersListAdapter.CustomerListViewHolder> {

    private List<Customer> items;
    private OnCustomerListItemCallback callback;
    private boolean searchMode = false;
    private String searchText;
    private DecimalFormat format;
    private Context context;
    private Currency currency;

    public CustomersListAdapter(Context context, Currency currency) {
        this.context = context;
        this.currency = currency;
        format = new DecimalFormat("#.##");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(' ');
        format.setDecimalFormatSymbols(symbols);
    }

    @Override
    public CustomerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_dialog_item, parent, false);
        return new CustomerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerListViewHolder holder, int position) {
        if (position % 2 == 0)
            holder.llBackground.setBackgroundColor(Color.parseColor("#e4f5ff"));
        else holder.llBackground.setBackgroundColor(Color.parseColor("#d1eafa"));
        if (getDebt(position) > 0)
            holder.tvDebtSum.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
        else holder.tvDebtSum.setTextColor(ContextCompat.getColor(context, R.color.colorMainText));

        if (!searchMode) {
            holder.tvCustomerId.setText(String.valueOf(items.get(position).getClientId()));
            holder.tvCustomerName.setText(items.get(position).getName());
            holder.tvCustomerAddress.setText(items.get(position).getAddress());
            holder.tvCustomerContact.setText(items.get(position).getPhoneNumber());
            holder.tvCustomerQrCode.setText(items.get(position).getQrCode());
            holder.tvDebtSum.setText(format.format(getDebt(position)) + " " + currency.getAbbr());
        } else {
            colorSubSeq(String.valueOf(items.get(position).getClientId()), searchText, Color.parseColor("#95ccee"), holder.tvCustomerId);
            colorSubSeq(items.get(position).getName(), searchText, Color.parseColor("#95ccee"), holder.tvCustomerName);
            colorSubSeq(items.get(position).getAddress(), searchText, Color.parseColor("#95ccee"), holder.tvCustomerAddress);
            colorSubSeq(items.get(position).getPhoneNumber(), searchText, Color.parseColor("#95ccee"), holder.tvCustomerContact);
            colorSubSeq(items.get(position).getQrCode(), searchText, Color.parseColor("#95ccee"), holder.tvCustomerQrCode);
            colorSubSeq(format.format(getDebt(position)) + " " + currency.getAbbr(), searchText, Color.parseColor("#95ccee"), holder.tvDebtSum);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<Customer> customerList) {
        this.items = customerList;
        searchMode = false;
        notifyDataSetChanged();
    }

    public void setSearchResult(List<Customer> searchResults, String searchText) {
        this.items = searchResults;
        this.searchText = searchText;
        searchMode = true;
        notifyDataSetChanged();
    }

    private double getDebt(int position) {
        items.get(position).resetDebtList();

        if (items.get(position).getDebtList().size() > 0) {
            List<Debt> debts = new ArrayList<>();
            for (Debt debt : items.get(position).getDebtList()) {
                if (debt.getStatus() == Debt.ACTIVE)
                    debts.add(debt);
            }
            double debtSum = 0;
            for (int i = 0; i < debts.size(); i++) {
                Debt debt = debts.get(i);
                debtSum += debt.getDebtAmount() + debt.getFee() * debt.getDebtAmount() / 100;
                if (debt.getCustomerPayments().size() > 0) {
                    for (CustomerPayment payment : debt.getCustomerPayments()) {
                        debtSum -= payment.getPaymentAmount();
                    }
                }
            }
            return debtSum;
        } else return 0;
    }

    public interface OnCustomerListItemCallback {
        void onItemEdit(Customer customer);

        void onItemSelect(Customer customer);
    }

    public void setCallback(OnCustomerListItemCallback callback) {
        this.callback = callback;
        this.callback = callback;
    }

    class CustomerListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvCustomerId)
        TextView tvCustomerId;
        @BindView(R.id.tvCustomerName)
        TextView tvCustomerName;
        @BindView(R.id.tvCustomerContact)
        TextView tvCustomerContact;
        @BindView(R.id.tvCustomerAddress)
        TextView tvCustomerAddress;
        @BindView(R.id.btnEdit)
        MpActionButton btnEdit;
        @BindView(R.id.btnAddToOrder)
        MpActionButton btnAddToOrder;
        @BindView(R.id.llBackground)
        LinearLayout llBackground;
        @BindView(R.id.tvCustomerQrCode)
        TextView tvCustomerQrCode;
        @BindView(R.id.tvDebtSum)
        TextView tvDebtSum;

        public CustomerListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnEdit.setOnClickListener(view -> callback.onItemEdit(items.get(getAdapterPosition())));
            btnAddToOrder.setOnClickListener(view -> callback.onItemSelect(items.get(getAdapterPosition())));
        }
    }

    private void colorSubSeq(String text, String whichWordColor, int colorCode, TextView textView) {
        String textUpper = text.toUpperCase();
        String whichWordColorUpper = whichWordColor.toUpperCase();
        SpannableString ss = new SpannableString(text);
        int strar = 0;

        while (textUpper.indexOf(whichWordColorUpper, strar) >= 0 && whichWordColor.length() != 0) {
            ss.setSpan(new BackgroundColorSpan(colorCode), textUpper.indexOf(whichWordColorUpper, strar), textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strar = textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length();
        }
        textView.setText(ss);
    }
}
