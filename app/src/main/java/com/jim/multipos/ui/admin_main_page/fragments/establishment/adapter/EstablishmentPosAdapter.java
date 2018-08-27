package com.jim.multipos.ui.admin_main_page.fragments.establishment.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.model.EstablishmentPos;

import java.util.List;

import butterknife.BindView;

public class EstablishmentPosAdapter extends BaseAdapter<EstablishmentPos, EstablishmentPosAdapter.ViewHolder> {

    OnEstablishmentPosItemClick click;

    public EstablishmentPosAdapter(List items, OnEstablishmentPosItemClick click) {
        super(items);
        this.click = click;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_establishment_pos_rv_item, parent, false));

    }

    public interface OnEstablishmentPosItemClick{
        void onClick(EstablishmentPos pos);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EstablishmentPos pos = items.get(position);
        holder.tvAddress.setText(pos.getAddress());
        holder.tvDescription.setText(pos.getDescription());
        holder.tvPosName.setText(pos.getName());
        holder.itemView.setOnClickListener(v -> click.onClick(pos));
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.tvPosName)
        TextView tvPosName;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.tvAddress)
        TextView tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
