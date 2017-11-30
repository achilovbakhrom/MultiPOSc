package com.jim.multipos.ui.mainpospage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.VendorItemViewHolder> {

    private List<Vendor> items;
    private OnVendorSelectCallback vendorSelectCallback;

    public void setData(List<Vendor> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public VendorItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_list_dialog_item, parent, false);
        return new VendorItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VendorItemViewHolder holder, int position) {
        holder.tvProductName.setText(items.get(position).getName());
    }

    public void setListener(OnVendorSelectCallback listener){
        this.vendorSelectCallback = listener;
    }

    public interface OnVendorSelectCallback {
        void onSelect(Vendor vendor);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VendorItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.llListContainer)
        LinearLayout llListContainer;

        public VendorItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            llListContainer.setOnClickListener(view -> vendorSelectCallback.onSelect(items.get(getAdapterPosition())));
        }
    }
}
