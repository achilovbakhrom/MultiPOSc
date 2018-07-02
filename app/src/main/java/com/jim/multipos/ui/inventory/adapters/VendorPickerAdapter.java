package com.jim.multipos.ui.inventory.adapters;

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
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.model.VendorPickerItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VendorPickerAdapter extends RecyclerView.Adapter<VendorPickerAdapter.VendorPickerViewHolder>{

    private List<VendorPickerItem> items;
    private String searchText;
    private boolean searchMode = false;
    private Context context;
    private OnVendorSelectCallback callback;

    public VendorPickerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<VendorPickerItem> items){
        this.items = items;
        searchMode = false;
        notifyDataSetChanged();
    }

    public void setSearchResult(List<VendorPickerItem> items, String searchText) {
        this.items = items;
        this.searchText = searchText;
        searchMode = true;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public VendorPickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_picker_item, parent, false);
        return new VendorPickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorPickerViewHolder holder, int position) {
        if (position % 2 == 0)
            holder.llBackground.setBackgroundColor(Color.parseColor("#e4f5ff"));
        else holder.llBackground.setBackgroundColor(Color.parseColor("#d1eafa"));
        if (!searchMode) {
            holder.tvContact.setText(items.get(position).getVendor().getContactName());
            holder.tvVendorName.setText(items.get(position).getVendor().getName());
        } else {
            colorSubSeq(items.get(position).getVendor().getContactName(), searchText, Color.parseColor("#95ccee"), holder.tvContact);
            colorSubSeq(items.get(position).getVendor().getName(), searchText, Color.parseColor("#95ccee"), holder.tvVendorName);
        }
        if (items.get(position).isWasSupplied()) {
            holder.tvSupplied.setText("YES");
            holder.tvSupplied.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        } else {
            holder.tvSupplied.setText("NO");
            holder.tvSupplied.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setCallback(OnVendorSelectCallback callback) {
        this.callback = callback;
    }

    class VendorPickerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvVendorName)
        TextView tvVendorName;
        @BindView(R.id.tvContact)
        TextView tvContact;
        @BindView(R.id.tvSupplied)
        TextView tvSupplied;
        @BindView(R.id.btnSelect)
        MpButton btnSelect;
        @BindView(R.id.llBackground)
        LinearLayout llBackground;

        public VendorPickerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnSelect.setOnClickListener(v -> callback.onSelect(items.get(getAdapterPosition()).getVendor()));
        }
    }

    public interface OnVendorSelectCallback{
        void onSelect(Vendor vendor);
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
