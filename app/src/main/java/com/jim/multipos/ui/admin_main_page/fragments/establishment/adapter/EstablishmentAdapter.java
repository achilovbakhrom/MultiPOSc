package com.jim.multipos.ui.admin_main_page.fragments.establishment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.model.Establishment;
import com.jim.multipos.utils.RxBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class EstablishmentAdapter extends BaseAdapter<Establishment, EstablishmentAdapter.ViewHolder> {

    OnItemClick click;
    Context context;
    int lastPos = -1;

    public EstablishmentAdapter(List<Establishment> items, OnItemClick click, Context context) {
        super(items);
        this.click = click;
        this.context = context;
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
        holder.itemView.setOnClickListener(v -> {
            click.onClick(establishment);
            lastPos = position;
            notifyDataSetChanged();
        });
        if(lastPos == position){
            holder.tvName.setTextColor(Color.WHITE);
            holder.tvDescription.setTextColor(Color.WHITE);
            holder.tvPosCount.setTextColor(Color.WHITE);
            ((CardView)holder.itemView).setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorCircleBlue));
        }else {
            holder.tvName.setTextColor(holder.defColor);
            holder.tvDescription.setTextColor(holder.defDescriptionColor);
            holder.tvPosCount.setTextColor(holder.defDescriptionColor);
            ((CardView)holder.itemView).setCardBackgroundColor(Color.WHITE);
        }
    }


    public interface OnItemClick{
        void onClick(Establishment establishment);
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.tvPosCount)
        TextView tvPosCount;

        int defColor, defDescriptionColor;

        public ViewHolder(View itemView) {
            super(itemView);
            defColor = tvName.getCurrentTextColor();
            defDescriptionColor = tvDescription.getCurrentTextColor();
        }
    }
}
