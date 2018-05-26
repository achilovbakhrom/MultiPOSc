package com.jim.multipos.ui.customer_debt.adapter;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 29.12.2017.
 */

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.CustomerListViewHolder> {
    private OnCustomerClickListener listener;
    private List<Customer> items;
    private String searchText;
    private Context context;
    protected int selectedPosition = -1;
    private boolean searchMode = false;
    private boolean isFirstTime = false;

    public CustomerListAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<Customer> items) {
        this.items = items;
        searchMode = false;
        isFirstTime = true;
        notifyDataSetChanged();
    }

    public void setSearchResult(List<Customer> searchResults, String searchText) {
        this.items = searchResults;
        this.searchText = searchText;
        searchMode = true;
        isFirstTime = true;
        notifyDataSetChanged();
    }

    @Override
    public CustomerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customers_list_item, parent, false);
        return new CustomerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerListViewHolder holder, int position) {
        Customer customer = items.get(position);
        if (isFirstTime) {
            if (position == 0) {
                selectedPosition = holder.getAdapterPosition();
                holder.flBlueBar.setVisibility(View.VISIBLE);
                holder.llBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                listener.onItemClicked(items.get(position), position);
                isFirstTime = false;
            }
        }
        if (position == selectedPosition) {
            holder.flBlueBar.setVisibility(View.VISIBLE);
            holder.llBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            holder.flBlueBar.setVisibility(View.INVISIBLE);
            holder.llBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground));
        }
        holder.ivWarning.setVisibility(View.GONE);
        GregorianCalendar now = new GregorianCalendar();
        for (int i = 0; i < customer.getActiveDebts().size(); i++) {
            Date date = new Date(customer.getActiveDebts().get(i).getEndDate());
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            if (now.after(calendar) || now.equals(calendar)) {
                holder.ivWarning.setVisibility(View.VISIBLE);
                break;
            } else holder.ivWarning.setVisibility(View.GONE);
        }
        if (!searchMode)
            holder.tvCustomerName.setText(customer.getName());
        else
            colorSubSeq(customer.getName(), searchText, Color.parseColor("#95ccee"), holder.tvCustomerName);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setListener(OnCustomerClickListener listener) {
        this.listener = listener;
    }

    public interface OnCustomerClickListener {
        void onItemClicked(Customer item, int position);
    }

    class CustomerListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvCustomerName)
        TextView tvCustomerName;
        @BindView(R.id.ivWarning)
        ImageView ivWarning;
        @BindView(R.id.flBlueBar)
        FrameLayout flBlueBar;
        @BindView(R.id.llBackground)
        LinearLayout llBackground;

        public CustomerListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                if (selectedPosition != getAdapterPosition())
                    if (listener != null) {
                        notifyItemChanged(selectedPosition);
                        notifyItemChanged(getAdapterPosition());
                        listener.onItemClicked(items.get(getAdapterPosition()), getAdapterPosition());
                        selectedPosition = getAdapterPosition();
                    }
            });
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
