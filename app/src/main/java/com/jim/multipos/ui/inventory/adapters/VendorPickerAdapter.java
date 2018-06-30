package com.jim.multipos.ui.inventory.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jim.multipos.ui.inventory.model.VendorPickerItem;

import java.util.List;

public class VendorPickerAdapter extends RecyclerView.Adapter<VendorPickerAdapter.VendorPickerViewHolder>{

    private List<VendorPickerItem> items;

    public VendorPickerAdapter() {
    }

    public void setDate(List<VendorPickerItem> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VendorPickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VendorPickerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VendorPickerViewHolder extends RecyclerView.ViewHolder {

        public VendorPickerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
