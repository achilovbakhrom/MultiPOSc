package com.jim.multipos.ui.inventory.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.utils.OnItemClickListener;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by developer on 10.11.2017.
 */

public class InventoryItemAdapter  extends RecyclerView.Adapter<InventoryItemAdapter.InventoryItemViewHolder> {
    OnInvendoryAdapterCallback callback;
    private Context context;
    boolean searchMode = false;
    private String searchText;
    DecimalFormat decimalFormat;
    List<InventoryItem> items;
    @Inject
    public InventoryItemAdapter(List<InventoryItem> items, OnInvendoryAdapterCallback callback, Context context) {
        this.items = items;
        decimalFormat = BaseAppModule.getFormatterWithoutGroupingFourDecimal();
        this.callback = callback;
        this.context = context;
    }
    public void setSearchResult(List<InventoryItem> searchResult,String searchText){
        this.searchText = searchText;
        searchMode = true;
        items = searchResult;
    }
    public void setData(List<InventoryItem> items){
        this.items = items;
        searchMode = false;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public InventoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item, parent, false);
        return new InventoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InventoryItemViewHolder holder, int position) {
        if(position%2==0) holder.llBackground.setBackgroundColor(Color.parseColor("#f9f9f9"));
        else holder.llBackground.setBackgroundColor(Color.parseColor("#f0f0f0"));
        InventoryItem inventoryItem = items.get(position);

        if(!searchMode) {
            setUnderlineText(holder.tvProductName, inventoryItem.getProduct().getName());
            holder.tvProductSku.setText(context.getString(R.string.sku_)+ inventoryItem.getProduct().getSku());
            holder.tvProductBarcode.setText(context.getString(R.string.barcode_) + inventoryItem.getProduct().getBarcode());
            if (inventoryItem.getProduct().getProductClass() != null)
                holder.tvProductClassName.setText(context.getString(R.string.product_class_colon )+ inventoryItem.getProduct().getProductClass().getName());
            else holder.tvProductClassName.setVisibility(View.GONE);
            StringBuilder vendorsName = new StringBuilder();
            for (Vendor vendor : items.get(position).getProduct().getVendor()) {
                if (vendorsName.length() == 0)
                    vendorsName = new StringBuilder(vendor.getName());
                else vendorsName.append(", ").append(vendor.getName());
            }
            holder.tvVendorNames.setText(vendorsName.toString());

        }else {
            colorSubSeqUnderLine(inventoryItem.getProduct().getName(),searchText,Color.parseColor("#95ccee"),holder.tvProductName);
            colorSubSeq(context.getString(R.string.sku_) + inventoryItem.getProduct().getSku(),searchText,Color.parseColor("#95ccee"),holder.tvProductSku);
            colorSubSeq(context.getString(R.string.barcode_) + inventoryItem.getProduct().getBarcode(),searchText,Color.parseColor("#95ccee"),holder.tvProductBarcode);
            if (inventoryItem.getProduct().getProductClass() != null){
                colorSubSeq(context.getString(R.string.product_class_colon)+ inventoryItem.getProduct().getProductClass().getName(),searchText,Color.parseColor("#95ccee"),holder.tvProductClassName);
            }
            else holder.tvProductClassName.setVisibility(View.GONE);

            StringBuilder vendorsName = new StringBuilder();
            for (Vendor vendor : items.get(position).getProduct().getVendor()) {
                if (vendorsName.length() == 0)
                    vendorsName = new StringBuilder(vendor.getName());
                else vendorsName.append(", ").append(vendor.getName());
            }
            colorSubSeq(vendorsName.toString(),searchText,Color.parseColor("#95ccee"),holder.tvVendorNames);
        }
        holder.tvUnitAbr.setText(inventoryItem.getProduct().getMainUnit().getAbbr());
        holder.tvInventoryCount.setText(decimalFormat.format(inventoryItem.getInventory()));
        if (inventoryItem.getLowStockAlert() == -1)
            holder.etStockAlert.setText("");
        else
            holder.etStockAlert.setText(decimalFormat.format(inventoryItem.getLowStockAlert()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnInvendoryAdapterCallback{
        void onStockAlertChange(double newAlertCount, InventoryItem inventoryItem);
        void onIncomeProduct(InventoryItem inventoryItem);
        void onWriteOff(InventoryItem inventoryItem);
        void onSetActually(InventoryItem inventoryItem);
        void onConsigmentIn(InventoryItem inventoryItem);
        void onConsigmentOut(InventoryItem inventoryItem);
    }
    public class InventoryItemViewHolder extends BaseViewHolder {
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvProductSku)
        TextView tvProductSku;
        @BindView(R.id.tvProductBarcode)
        TextView tvProductBarcode;
        @BindView(R.id.tvProductClassName)
        TextView tvProductClassName;
        @BindView(R.id.tvVendorNames)
        TextView tvVendorNames;
        @BindView(R.id.etStockAlert)
        MpEditText etStockAlert;
        @BindView(R.id.tvInventoryCount)
        TextView tvInventoryCount;
        @BindView(R.id.tvUnitAbr)
        TextView tvUnitAbr;
        @BindView(R.id.ivIncome)
        ImageView ivIncome;
        @BindView(R.id.ivBackReturn)
        ImageView ivBackReturn;
        @BindView(R.id.ivAddProduct)
        ImageView ivAddProduct;
        @BindView(R.id.ivWriteOff)
        ImageView ivWriteOff;
        @BindView(R.id.llBackground)
        LinearLayout llBackground;
        public InventoryItemViewHolder(View itemView) {
            super(itemView);
            ivIncome.setOnClickListener(view1 -> {
                callback.onConsigmentIn(items.get(getAdapterPosition()));
            });
            ivBackReturn.setOnClickListener(view1 -> {
                callback.onConsigmentOut(items.get(getAdapterPosition()));
            });
            ivAddProduct.setOnClickListener(view1 -> {
                callback.onIncomeProduct(items.get(getAdapterPosition()));
            });
            ivWriteOff.setOnClickListener(view1 -> {
                callback.onWriteOff(items.get(getAdapterPosition()));
            });
            
            etStockAlert.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    double percent = 0;
                    if(etStockAlert.getText().toString().isEmpty()){
                        callback.onStockAlertChange(-1,items.get(getAdapterPosition()));
                        return;
                    }
                    try{
                        percent = decimalFormat.parse(etStockAlert.getText().toString().replace(',','.')).doubleValue();
                    }catch (Exception e){
                        etStockAlert.setError(context.getString(R.string.invalid));
                        return;
                    }
                    if(items.get(getAdapterPosition()).getLowStockAlert()!=percent)
                    callback.onStockAlertChange(percent,items.get(getAdapterPosition()));
                }
            });

        }
    }
    public void setUnderlineText(TextView textView, String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
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
    public void colorSubSeqUnderLine(String text, String whichWordColor, int colorCode, TextView textView) {
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

}
