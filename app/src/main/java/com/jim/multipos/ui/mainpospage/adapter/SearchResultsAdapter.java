package com.jim.multipos.ui.mainpospage.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MPListItemView;
import com.jim.mpviews.MpVendorItem;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.intosystem.FolderItem;
import com.jim.multipos.data.db.model.products.Product;

import java.util.List;

import butterknife.BindView;

/**
 * Created by developer on 08.11.2017.
 */

public class SearchResultsAdapter extends ClickableBaseAdapter<Product, SearchResultsAdapter.SearchResultHolder> {

    private String textSearched;

    public SearchResultsAdapter(List<Product> items) {
        super(items);
    }

    public void setItems(List<Product> items,String textSearched) {
        this.textSearched = textSearched;
        super.setItems(items);

    }

    @Override
    protected void onItemClicked(SearchResultHolder holder, int position) {

    }

    @Override
    public SearchResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.square_view_search_item, parent, false);
        return new SearchResultHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultHolder holder, int position) {
        super.onBindViewHolder(holder, position);
       colorSubSeq(items.get(position).getName(),textSearched,Color.parseColor("#95ccee"),holder.mpSquareItem.getVendorTv());
       colorSubSeq("Sku: "+items.get(position).getSku(),textSearched,Color.parseColor("#95ccee"),holder.mpSquareItem.getVendorItemTv());
       colorSubSeq("Barcode: "+items.get(position).getBarcode(),textSearched,Color.parseColor("#95ccee"),holder.mpSquareItem.getVendorNameTv());

    }

    public class SearchResultHolder extends BaseViewHolder {
        @BindView(R.id.mpSquareItem)
        MpVendorItem mpSquareItem;

        public SearchResultHolder(View itemView) {
            super(itemView);
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
