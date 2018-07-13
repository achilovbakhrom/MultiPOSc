package com.jim.multipos.ui.mainpospage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 05.01.2018.
 */

public class ProductSearchResultsAdapter extends RecyclerView.Adapter<ProductSearchResultsAdapter.ProductSearchViewHolder> {

    private List<Product> items;
    private onProductSearchResultClickListener listener;
    private Context context;
    private boolean searchMode = false;
    private String searchText;

    public ProductSearchResultsAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
    }

    @Override
    public ProductSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_for_return_item, parent, false);
        return new ProductSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductSearchViewHolder holder, int position) {

        if (!searchMode) {
            holder.tvProductName.setText(items.get(position).getName());
            holder.tvProductBarcode.setText(context.getString(R.string.barcode_) + items.get(position).getBarcode());
            holder.tvProductSKU.setText(context.getString(R.string.sku_) + items.get(position).getSku());
        } else {
            colorSubSeq(items.get(position).getName(), searchText, Color.parseColor("#95ccee"), holder.tvProductName);
            colorSubSeq(context.getString(R.string.barcode_) + items.get(position).getBarcode(), searchText, Color.parseColor("#95ccee"), holder.tvProductBarcode);
            colorSubSeq(context.getString(R.string.sku_) + items.get(position).getSku(), searchText, Color.parseColor("#95ccee"), holder.tvProductSKU);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<Product> products) {
        this.items = products;
        searchMode = false;
        notifyDataSetChanged();
    }

    public void setSearchResults(List<Product> items, String searchText) {
        this.items = items;
        this.searchText = searchText;
        searchMode = true;
        notifyDataSetChanged();
    }

    public void setListener(onProductSearchResultClickListener listener) {
        this.listener = listener;
    }

    public interface onProductSearchResultClickListener {
        void onClick(Product product);
    }

    class ProductSearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvProductBarcode)
        TextView tvProductBarcode;
        @BindView(R.id.tvProductSKU)
        TextView tvProductSKU;

        public ProductSearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> listener.onClick(items.get(getAdapterPosition())));
        }
    }

    public void colorSubSeq(String text, String whichWordColor, int colorCode, TextView textView) {
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
