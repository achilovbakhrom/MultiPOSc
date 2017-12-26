package com.jim.multipos.ui.mainpospage.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpActionButton;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.customer.Customer;

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

    @Override
    public CustomerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_dialog_item, parent, false);
        return new CustomerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerListViewHolder holder, int position) {
        if (position % 2 == 0)
            holder.llBackground.setBackgroundColor(Color.parseColor("#f3f3f3"));
        else holder.llBackground.setBackgroundColor(Color.parseColor("#fdfdfd"));
        if (!searchMode) {
            holder.tvCustomerId.setText(String.valueOf(items.get(position).getClientId()));
            holder.tvCustomerName.setText(items.get(position).getName());
            holder.tvCustomerAddress.setText(items.get(position).getAddress());
            holder.tvCustomerContact.setText(items.get(position).getPhoneNumber());
        } else {
            colorSubSeq(String.valueOf(items.get(position).getClientId()), searchText, Color.parseColor("#95ccee"), holder.tvCustomerId);
            colorSubSeq(items.get(position).getName(), searchText, Color.parseColor("#95ccee"), holder.tvCustomerName);
            colorSubSeq(items.get(position).getAddress(), searchText, Color.parseColor("#95ccee"), holder.tvCustomerAddress);
            colorSubSeq(items.get(position).getPhoneNumber(), searchText, Color.parseColor("#95ccee"), holder.tvCustomerContact);
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

    public interface OnCustomerListItemCallback {
        void onItemEdit(Customer customer);

        void onItemDelete(Customer customer);
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

        public CustomerListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnEdit.setOnClickListener(view -> callback.onItemEdit(items.get(getAdapterPosition())));
            btnAddToOrder.setOnClickListener(view -> callback.onItemDelete(items.get(getAdapterPosition())));
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
