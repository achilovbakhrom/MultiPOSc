package com.jim.multipos.ui.inventory.adapters;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by developer on 10.11.2017.
 */

public class InventoryItemAdapter  extends ClickableBaseAdapter<InventoryItem, InventoryItemAdapter.InventoryItemViewHolder> {

    @Inject
    public InventoryItemAdapter(List<InventoryItem> items) {
        super(items);
    }
//    public void setData
    @Override
    protected void onItemClicked(InventoryItemViewHolder holder, int position) {

    }

    @Override
    public InventoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item, parent, false);
        return new InventoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InventoryItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        InventoryItem inventoryItem = items.get(position);
        setUnderlineText(holder.tvProductName, inventoryItem.getProduct().getName());
        holder.tvProductSku.setText(inventoryItem.getProduct().getSku());
        holder.tvProductBarcode.setText(inventoryItem.getProduct().getBarcode());
        if(inventoryItem.getProduct().getProductClass()!=null)
        holder.tvProductClassName.setText(inventoryItem.getProduct().getProductClass().getName());
        else holder.tvProductClassName.setVisibility(View.INVISIBLE);
        holder.tvUnitAbr.setText(inventoryItem.getProduct().getMainUnit().getAbbr());
        StringBuilder vendorsName= new StringBuilder();
        for(Vendor vendor:items.get(position).getProduct().getVendor()){
            if(vendorsName.length() == 0)
            vendorsName = new StringBuilder(vendor.getName());
            else vendorsName.append(",").append(vendor.getName());
        }
        holder.tvVendorNames.setText(vendorsName.toString());
        holder.tvInventoryCount.setText(String.valueOf(inventoryItem.getInventory()));
        holder.etStockAlert.setText(String.valueOf(inventoryItem.getLowStockAlert()));

    }

    public interface onInvendoryAdapterCallback{
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
        @BindView(R.id.ivSetActualy)
        ImageView ivSetActualy;
        @BindView(R.id.ivAddProduct)
        ImageView ivAddProduct;
        @BindView(R.id.ivWriteOff)
        ImageView ivWriteOff;

        public InventoryItemViewHolder(View itemView) {
            super(itemView);


        }
    }
    public void setUnderlineText(TextView textView, String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }
}
