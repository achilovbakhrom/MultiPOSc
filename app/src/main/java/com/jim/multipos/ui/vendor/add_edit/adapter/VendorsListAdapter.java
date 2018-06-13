package com.jim.multipos.ui.vendor.add_edit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.mpviews.MpLongItemWithList;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;

import butterknife.BindView;

/**
 * Created by bakhrom on 10/23/17.
 */

public class VendorsListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int ADD = 0, ITEM = 1;
    private Context context;
    private DatabaseManager databaseManager;
    List<Vendor> items;
    public VendorsListAdapter(List<Vendor> items, Context context, DatabaseManager databaseManager) {
        this.items = items;
        this.context = context;
        this.databaseManager = databaseManager;
        selectedPosition = 0;
    }
    public void setItems(List<Vendor> items){
        this.items = items;
        notifyDataSetChanged();
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        BaseViewHolder holder;
        if (viewType == ADD) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_list_add_item, parent, false);
            holder = new AddItemViewHolder(view);
        }
        else  {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_list_item, parent, false);
            holder = new VendorViewHolder(view);
        }

        return holder;
    }
    private com.jim.multipos.utils.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(com.jim.multipos.utils.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    protected int selectedPosition = -1;

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        if (holder instanceof VendorViewHolder) {
            ((VendorViewHolder) holder).item.setFirstItemText(items.get(position).getName());
            int size;
            size = databaseManager.getAllProductsCountVendor(items.get(position).getId()).blockingGet();
            ((VendorViewHolder) holder).item.setSecondItemText(context.getString(R.string.items_) + size);
            ((VendorViewHolder) holder).item.setThirdItemText(items.get(position).getContactName());
            ((VendorViewHolder) holder).item.setTextSize(12);
            ((VendorViewHolder) holder).item.setActivate(position == selectedPosition);
            ((VendorViewHolder) holder).item.makeDeletable(!items.get(position).getIsActive());
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

    class VendorViewHolder extends BaseViewHolder {
        @BindView(R.id.aivItem)
        MpLongItemWithList item;
        VendorViewHolder(View itemView) {
            super(itemView);
            item.setOnClickListener(view1 -> {
                if(selectedPosition!=getAdapterPosition() )
                    if (onItemClickListener != null) {
                        int temp = selectedPosition;
                        onItemClickListener.onItemClicked(items.get(getAdapterPosition()));
                        onItemClickListener.onItemClicked(getAdapterPosition());
                        selectedPosition = getAdapterPosition();
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
                if(selectedPosition!=getAdapterPosition() )
                    if (onItemClickListener != null) {
                        int temp = selectedPosition;
                        onItemClickListener.onItemClicked(items.get(getAdapterPosition()));
                        onItemClickListener.onItemClicked(getAdapterPosition());
                        selectedPosition = getAdapterPosition();
                        notifyItemChanged(temp);
                        notifyItemChanged(selectedPosition);
                    }
            });
        }
    }

    public void select(int pos) {
        selectedPosition = pos;
        notifyItemChanged(selectedPosition);
    }

    public void setSelectedPositionWithId(Long id) {
        for (Vendor vendor : items) {
            if (vendor == null) continue;
            if (vendor.getId().equals(id)) {
                this.selectedPosition = items.indexOf(vendor);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
