package com.jim.multipos.ui.admin_main_page.fragments.establishment.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.model.Establishment;

import java.util.List;

import butterknife.BindView;

public class EstablishmentAdapter extends BaseAdapter<Establishment, EstablishmentAdapter.ViewHolder> {

    OnItemClick itemClick;

    public EstablishmentAdapter(List<Establishment> items) {
        super(items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_establishment_rv_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Establishment establishment = items.get(position);
        holder.tvName.setText(establishment.getName());
        holder.tvDescription.setText(establishment.getDescription());
        holder.tvPosCount.setText(establishment.getPosCount());
        holder.itemView.setOnClickListener(v -> itemClick.onItemClick(position));
    }

    public interface OnItemClick {
        void onItemClick(int pos);
    }

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.tvPosCount)
        TextView tvPosCount;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
