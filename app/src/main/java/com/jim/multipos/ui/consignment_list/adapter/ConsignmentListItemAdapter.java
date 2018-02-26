package com.jim.multipos.ui.consignment_list.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.currency.Currency;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 30.11.2017.
 */

public class ConsignmentListItemAdapter extends RecyclerView.Adapter<ConsignmentListItemAdapter.ConsignmentListViewHolder> {

    private List<Consignment> items;
    private Currency currency;
    private String searchText;
    private OnConsignmentListItemCallback callback;
    private Context context;
    private boolean searchMode = false;
    private boolean isExpended = false;

    public ConsignmentListItemAdapter(Context context) {
        this.context = context;
    }

    public void setSearchResult(List<Consignment> searchResults, Currency currency, String searchText) {
        this.items = searchResults;
        this.currency = currency;
        this.searchText = searchText;
        searchMode = true;
        notifyDataSetChanged();
    }


    public void setItems(List<Consignment> items, Currency currency) {
        this.items = items;
        this.currency = currency;
        searchMode = false;
        notifyDataSetChanged();
    }

    public void setCallback(OnConsignmentListItemCallback callback) {
        this.callback = callback;
    }

    @Override
    public ConsignmentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consignment_item, parent, false);
        return new ConsignmentListViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ConsignmentListViewHolder holder, int position) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Consignment consignment = items.get(position);
        List<ConsignmentProduct> productList = consignment.getAllConsignmentProducts();
        if (position % 2 == 0)
            holder.llBackground.setBackgroundColor(Color.parseColor("#f9f9f9"));
        else holder.llBackground.setBackgroundColor(Color.parseColor("#f0f0f0"));
        holder.ivShowHide.setImageResource(R.drawable.arrow_down);
        holder.ivShowHide.setVisibility(View.GONE);
        if (consignment.getConsignmentType() == Consignment.INCOME_CONSIGNMENT) {
            holder.ivStatus.setImageResource(R.drawable.income_2nd);
            holder.ivStatus.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorRedLight));
            holder.tvDebtAmount.setTextColor(Color.parseColor("#df4f4f"));
        } else {
            holder.ivStatus.setImageResource(R.drawable.expense_2nd);
            holder.ivStatus.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorGreenLight));
            holder.tvDebtAmount.setTextColor(Color.parseColor("#4fc82b"));
        }
        if (!searchMode) {
            setUnderlineText(holder.tvConsignmentNumber, consignment.getConsignmentNumber());
            if (consignment.getConsignmentType() == Consignment.INCOME_CONSIGNMENT)
                holder.tvDebtAmount.setText(String.valueOf(-1 * consignment.getTotalAmount()) + " " + this.currency.getAbbr());
            else
                holder.tvDebtAmount.setText(String.valueOf(consignment.getTotalAmount()) + " " + this.currency.getAbbr());
            Date date = new Date(consignment.getCreatedDate());
            holder.tvDate.setText(formatter.format(date));
            if (productList.size() > 3) {
                holder.ivShowHide.setVisibility(View.VISIBLE);
                holder.llConsignmentProducts.setOnClickListener(view -> {
                    isExpended = !isExpended;
                    if (isExpended)
                        holder.ivShowHide.setImageResource(R.drawable.arrow_up);
                    else holder.ivShowHide.setImageResource(R.drawable.arrow_down);

                    for (int i = 0; i < holder.llConsignmentProducts.getChildCount(); i++) {
                        if (i > 0) {
                            holder.llConsignmentProducts.removeViewAt(i);
                            i--;
                        }
                    }

                    for (int i = 0; i < productList.size(); i++) {
                        LinearLayout layout = new LinearLayout(context);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, 0, 0, 5);
                        layout.setLayoutParams(layoutParams);
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        if (i > 2 && isExpended)
                            layout.setVisibility(View.VISIBLE);
                        else if (i > 2 && !isExpended)
                            layout.setVisibility(View.GONE);
                        holder.llConsignmentProducts.addView(layout);
                        TextView sku = new TextView(context);
                        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        sku.setPadding(40, 0, 0, 0);
                        sku.setTextColor(context.getColor(R.color.colorSecondaryText));
                        sku.setTextSize(14);
                        sku.setTypeface(sku.getTypeface(), Typeface.ITALIC);
                        sku.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        sku.setText(productList.get(i).getProduct().getSku() + " : ");
                        sku.setLayoutParams(textParams);
                        layout.addView(sku);
                        TextView name = new TextView(context);
                        name.setTextColor(context.getColor(R.color.colorSecondaryText));
                        name.setTextSize(14);
                        name.setTypeface(sku.getTypeface(), Typeface.ITALIC);
                        name.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        name.setText(productList.get(i).getProduct().getName());
                        name.setLayoutParams(textParams);
                        layout.addView(name);
                        ImageView dots = new ImageView(context);
                        dots.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                        LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(0, 5);
                        ivParams.weight = 1f;
                        ivParams.gravity = Gravity.BOTTOM;
                        dots.setImageResource(R.drawable.dash);
                        dots.setLayoutParams(ivParams);
                        layout.addView(dots);
                        TextView count = new TextView(context);
                        count.setTextColor(context.getColor(R.color.colorSecondaryText));
                        count.setTextSize(14);
                        count.setTypeface(sku.getTypeface(), Typeface.ITALIC);
                        count.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        count.setText(String.valueOf(productList.get(i).getCountValue()));
                        count.setLayoutParams(textParams);
                        layout.addView(count);
                    }
                });
            }
            for (int i = 0; i < holder.llConsignmentProducts.getChildCount(); i++) {
                if (i > 0) {
                    holder.llConsignmentProducts.removeViewAt(i);
                    i--;
                }
            }
            for (int i = 0; i < productList.size(); i++) {
                LinearLayout layout = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 5);
                layout.setLayoutParams(layoutParams);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                if (i > 2)
                    layout.setVisibility(View.GONE);
                holder.llConsignmentProducts.addView(layout);
                TextView sku = new TextView(context);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                sku.setPadding(40, 0, 0, 0);
                sku.setTextColor(context.getColor(R.color.colorSecondaryText));
                sku.setTextSize(14);
                sku.setTypeface(sku.getTypeface(), Typeface.ITALIC);
                sku.setGravity(View.TEXT_ALIGNMENT_CENTER);
                sku.setText(productList.get(i).getProduct().getSku() + " : ");
                sku.setLayoutParams(textParams);
                layout.addView(sku);
                TextView name = new TextView(context);
                name.setTextColor(context.getColor(R.color.colorSecondaryText));
                name.setTextSize(14);
                name.setTypeface(sku.getTypeface(), Typeface.ITALIC);
                name.setGravity(View.TEXT_ALIGNMENT_CENTER);
                name.setText(productList.get(i).getProduct().getName());
                name.setLayoutParams(textParams);
                layout.addView(name);
                ImageView dots = new ImageView(context);
                dots.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(0, 5);
                ivParams.weight = 1f;
                ivParams.gravity = Gravity.BOTTOM;
                dots.setImageResource(R.drawable.dash);
                dots.setLayoutParams(ivParams);
                layout.addView(dots);
                TextView count = new TextView(context);
                count.setTextColor(context.getColor(R.color.colorSecondaryText));
                count.setTextSize(14);
                count.setTypeface(sku.getTypeface(), Typeface.ITALIC);
                count.setGravity(View.TEXT_ALIGNMENT_CENTER);
                count.setText(String.valueOf(productList.get(i).getCountValue()));
                count.setLayoutParams(textParams);
                layout.addView(count);
            }
        } else {
            colorSubSeqUnderLine(consignment.getConsignmentNumber(), searchText, Color.parseColor("#95ccee"), holder.tvConsignmentNumber);
            if (consignment.getConsignmentType() == Consignment.INCOME_CONSIGNMENT)
                colorSubSeq(String.valueOf(-1 * consignment.getTotalAmount()) + " " + this.currency.getAbbr(), searchText, Color.parseColor("#95ccee"), holder.tvDebtAmount);
            else
                colorSubSeq(String.valueOf(consignment.getTotalAmount()) + " " + this.currency.getAbbr(), searchText, Color.parseColor("#95ccee"), holder.tvDebtAmount);
            Date date = new Date(consignment.getCreatedDate());
            colorSubSeq(formatter.format(date), searchText, Color.parseColor("#95ccee"), holder.tvDate);
            for (int i = 0; i < holder.llConsignmentProducts.getChildCount(); i++) {
                if (i > 0) {
                    holder.llConsignmentProducts.removeViewAt(i);
                    i--;
                }
            }
            for (int i = 0; i < productList.size(); i++) {
                LinearLayout layout = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 5);
                layout.setLayoutParams(layoutParams);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                holder.llConsignmentProducts.addView(layout);
                TextView sku = new TextView(context);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                sku.setPadding(40, 0, 0, 0);
                sku.setTextColor(context.getColor(R.color.colorSecondaryText));
                sku.setTextSize(14);
                sku.setTypeface(sku.getTypeface(), Typeface.ITALIC);
                sku.setGravity(View.TEXT_ALIGNMENT_CENTER);
                colorSubSeq(productList.get(i).getProduct().getSku() + " : ", searchText, Color.parseColor("#95ccee"), sku);
                sku.setLayoutParams(textParams);
                layout.addView(sku);
                TextView name = new TextView(context);
                name.setTextColor(context.getColor(R.color.colorSecondaryText));
                name.setTextSize(14);
                name.setTypeface(sku.getTypeface(), Typeface.ITALIC);
                name.setGravity(View.TEXT_ALIGNMENT_CENTER);
                colorSubSeq(productList.get(i).getProduct().getName(), searchText, Color.parseColor("#95ccee"), name);
                name.setLayoutParams(textParams);
                layout.addView(name);
                ImageView dots = new ImageView(context);
                dots.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(0, 5);
                ivParams.weight = 1f;
                ivParams.gravity = Gravity.BOTTOM;
                dots.setImageResource(R.drawable.dash);
                dots.setLayoutParams(ivParams);
                layout.addView(dots);
                TextView count = new TextView(context);
                count.setTextColor(context.getColor(R.color.colorSecondaryText));
                count.setTextSize(14);
                count.setTypeface(sku.getTypeface(), Typeface.ITALIC);
                count.setGravity(View.TEXT_ALIGNMENT_CENTER);
                colorSubSeq(String.valueOf(productList.get(i).getCountValue()), searchText, Color.parseColor("#95ccee"), count);
                count.setLayoutParams(textParams);
                layout.addView(count);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnConsignmentListItemCallback {
        void onItemClick(Consignment consignment);

        void onItemDelete(Consignment consignment);
    }

    private void setUnderlineText(TextView textView, String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
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

    private void colorSubSeqUnderLine(String text, String whichWordColor, int colorCode, TextView textView) {
        String textUpper = text.toUpperCase();
        String whichWordColorUpper = whichWordColor.toUpperCase();
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new UnderlineSpan(), 0, ss.length(), 0);
        int strar = 0;

        while (textUpper.indexOf(whichWordColorUpper, strar) >= 0 && whichWordColor.length() != 0) {
            ss.setSpan(new BackgroundColorSpan(colorCode), textUpper.indexOf(whichWordColorUpper, strar), textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strar = textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length();
        }
        textView.setText(ss);
    }

    public class ConsignmentListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llBackground)
        LinearLayout llBackground;
        @BindView(R.id.ivDeleteConsignment)
        ImageView ivDeleteConsignment;
        @BindView(R.id.ivEditConsignment)
        ImageView ivEditConsignment;
        @BindView(R.id.llConsignmentProducts)
        LinearLayout llConsignmentProducts;
        @BindView(R.id.llConsignmentNumber)
        LinearLayout llConsignmentNumber;
        @BindView(R.id.ivStatus)
        ImageView ivStatus;
        @BindView(R.id.tvConsignmentNumber)
        TextView tvConsignmentNumber;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvDebtAmount)
        TextView tvDebtAmount;
        @BindView(R.id.ivShowHide)
        ImageView ivShowHide;

        public ConsignmentListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivEditConsignment.setOnClickListener(view -> callback.onItemClick(items.get(getAdapterPosition())));
            ivDeleteConsignment.setOnClickListener(view -> callback.onItemDelete(items.get(getAdapterPosition())));
        }
    }
}
