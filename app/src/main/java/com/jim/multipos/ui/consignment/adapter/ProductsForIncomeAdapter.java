package com.jim.multipos.ui.consignment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
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

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.consignment.model.ProductForIncomeItem;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsForIncomeAdapter extends RecyclerView.Adapter<ProductsForIncomeAdapter.ProductForIncomeViewHolder> {

    private final DecimalFormat decimalFormat;
    private List<ProductForIncomeItem> items;
    private String searchText;
    private boolean searchMode = false;
    private Context context;
    private OnProductIncomeClicked listener;

    public ProductsForIncomeAdapter(Context context) {
        this.context = context;
        decimalFormat = BaseAppModule.getFormatterFourGrouping();
    }

    @NonNull
    @Override
    public ProductForIncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_product_item, parent, false);
        return new ProductForIncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductForIncomeViewHolder holder, int position) {
        if (position % 2 == 0)
            holder.llBackground.setBackgroundColor(Color.parseColor("#e4f5ff"));
        else holder.llBackground.setBackgroundColor(Color.parseColor("#d1eafa"));
        if (!searchMode) {
            holder.tvArticle.setText(items.get(position).getProduct().getSku());
            holder.tvBarcode.setText(items.get(position).getProduct().getBarcode());
            holder.tvProductName.setText(items.get(position).getProduct().getName());
            holder.tvPrice.setText(decimalFormat.format(items.get(position).getProduct().getPrice()));
        } else {
            colorSubSeq(items.get(position).getProduct().getSku(), searchText, Color.parseColor("#95ccee"), holder.tvArticle);
            colorSubSeq(items.get(position).getProduct().getBarcode(), searchText, Color.parseColor("#95ccee"), holder.tvBarcode);
            colorSubSeq(items.get(position).getProduct().getName(), searchText, Color.parseColor("#95ccee"), holder.tvProductName);
            colorSubSeq(decimalFormat.format(items.get(position).getProduct().getPrice()), searchText, Color.parseColor("#95ccee"), holder.tvPrice);
        }
        if (items.get(position).isWasSupplied()) {
            holder.tvSupplied.setText(context.getString(R.string.yes));
            holder.tvSupplied.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        } else {
            holder.tvSupplied.setText(context.getString(R.string.no));
            holder.tvSupplied.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setData(List<ProductForIncomeItem> items) {
        this.items = items;
        searchMode = false;
        notifyDataSetChanged();
    }

    public void setSearchResult(List<ProductForIncomeItem> items, String searchText) {
        this.items = items;
        this.searchText = searchText;
        searchMode = true;
        notifyDataSetChanged();
    }

    public void setListener(OnProductIncomeClicked listener) {
        this.listener = listener;
    }

    class ProductForIncomeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvBarcode)
        TextView tvBarcode;
        @BindView(R.id.tvArticle)
        TextView tvArticle;
        @BindView(R.id.tvSupplied)
        TextView tvSupplied;
        @BindView(R.id.btnSelect)
        MpButton btnSelect;
        @BindView(R.id.llBackground)
        LinearLayout llBackground;
        @BindView(R.id.tvPrice)
        TextView tvPrice;

        public ProductForIncomeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnSelect.setOnClickListener(v -> listener.onSelect(items.get(getAdapterPosition()).getProduct()));
        }
    }

    public interface OnProductIncomeClicked {
        void onSelect(Product product);
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
