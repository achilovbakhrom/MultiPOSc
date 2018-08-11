package com.jim.multipos.ui.admin_main_page.fragments.dashboard.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.model.Pos;

import java.util.List;

import butterknife.BindView;

public class PosAdapter extends BaseAdapter<Pos, PosAdapter.PosViewHolder> {


    public PosAdapter(List<Pos> items) {
        super(items);
    }

    @NonNull
    @Override
    public PosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PosViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_dashboard_pos_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PosViewHolder holder, int position) {
        Pos pos = items.get(position);
        holder.tvCard.setText(pos.getCard());
        holder.tvCash.setText(pos.getCash());
        holder.tvOrders.setText(pos.getOrders());
    }

    public class PosViewHolder extends BaseViewHolder {
        @BindView(R.id.tvCash)
        TextView tvCash;
        @BindView(R.id.tvCard)
        TextView tvCard;
        @BindView(R.id.tvOrders)
        TextView tvOrders;

        public PosViewHolder(View itemView) {
            super(itemView);
        }
    }
}
