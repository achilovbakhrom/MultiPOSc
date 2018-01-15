package com.jim.multipos.ui.cash_management.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.multipos.R;

/**
 * Created by Sirojiddin on 13.01.2018.
 */

public class CloseAmountAdapter extends RecyclerView.Adapter<CloseAmountAdapter.CloseAmountViewHolder> {

    @Override
    public CloseAmountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.close_amout_item, parent, false);
        return new CloseAmountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CloseAmountViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CloseAmountViewHolder extends RecyclerView.ViewHolder {

        public CloseAmountViewHolder(View itemView) {
            super(itemView);
        }
    }
}
