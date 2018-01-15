package com.jim.multipos.ui.cash_management.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jim.multipos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 13.01.2018.
 */

public class ReconcileOrdersAdapter extends RecyclerView.Adapter<ReconcileOrdersAdapter.ReconcileOrdersViewHolder> {


    @Override
    public ReconcileOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reconcile_order_list_item, parent, false);
        return new ReconcileOrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReconcileOrdersViewHolder holder, int position) {
        if (position % 2 == 0)
            holder.llBackground.setBackgroundColor(Color.parseColor("#e4f5ff"));
        else holder.llBackground.setBackgroundColor(Color.parseColor("#d1eafa"));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ReconcileOrdersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llBackground)
        LinearLayout llBackground;

        public ReconcileOrdersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
