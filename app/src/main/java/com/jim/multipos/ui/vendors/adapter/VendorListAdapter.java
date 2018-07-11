package com.jim.multipos.ui.vendors.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.mpviews.MpLongItemWithList;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;

import butterknife.BindView;

/**
 * Created by bakhrom on 10/23/17.
 */

public class VendorListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int ADD = 0, ITEM = 1;
    private Context context;
    private List<Vendor> items;
    private OnVendorSelectedListener listener;
    private int selectedPosition = -1;

    public VendorListAdapter(Context context) {
        this.context = context;
        selectedPosition = 0;
    }

    public void setItems(List<Vendor> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        BaseViewHolder holder;
        if (viewType == ADD) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_list_add_item, parent, false);
            holder = new AddItemViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_list_item, parent, false);
            holder = new VendorViewHolder(view);
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        if (holder instanceof VendorViewHolder) {
            ((VendorViewHolder) holder).item.setFirstItemText(items.get(position).getName());
            ((VendorViewHolder) holder).item.setSecondItemText(items.get(position).getContactName());
            ((VendorViewHolder) holder).item.setTextSize(12);
            ((VendorViewHolder) holder).item.setActivate(position == selectedPosition);
            ((VendorViewHolder) holder).item.makeDeletable(!items.get(position).getActive());
        } else {
            ((AddItemViewHolder) holder).item.setVisibility(View.VISIBLE, View.GONE, View.GONE);
            ((AddItemViewHolder) holder).item.itIsAddButton(true);
            ((AddItemViewHolder) holder).item.setActivate(position == selectedPosition);
            ((AddItemViewHolder) holder).item.setTextSize(14);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? ADD : ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setSelectedPosition(int selectedPosition) {
        notifyItemChanged(this.selectedPosition);
        this.selectedPosition = selectedPosition;
        notifyItemChanged(this.selectedPosition);
    }

    class VendorViewHolder extends BaseViewHolder {
        @BindView(R.id.aivItem)
        MpLongItemWithList item;

        VendorViewHolder(View itemView) {
            super(itemView);
            item.setOnClickListener(view1 -> {
                if (selectedPosition != getAdapterPosition())
                    if (listener != null) {
                        int temp = selectedPosition;
                        selectedPosition = getAdapterPosition();
                        listener.onSelect(items.get(getAdapterPosition()), getAdapterPosition(), temp);
                        notifyItemChanged(temp);
                        notifyItemChanged(selectedPosition);
                    }
            });
        }
    }

    class AddItemViewHolder extends BaseViewHolder {
        @BindView(R.id.aivAddItem)
        MpLongItemWithList item;

        AddItemViewHolder(View itemView) {
            super(itemView);
            item.setOnClickListener(view1 -> {
                if (selectedPosition != getAdapterPosition())
                    if (listener != null) {
                        int temp = selectedPosition;
                        selectedPosition = getAdapterPosition();
                        listener.onSelect(items.get(getAdapterPosition()), getAdapterPosition(), temp);
                        notifyItemChanged(temp);
                        notifyItemChanged(selectedPosition);
                    }
            });
        }
    }

    public int getSelectedPosition(){
        return selectedPosition;
    }

    public void setListener(OnVendorSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnVendorSelectedListener {
        void onSelect(Vendor vendor, int position, int previousPosition);
    }
}
