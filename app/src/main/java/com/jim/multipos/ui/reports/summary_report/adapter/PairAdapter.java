package com.jim.multipos.ui.reports.summary_report.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PairAdapter extends RecyclerView.Adapter<PairAdapter.PairViewHolder>{
    List<PairString> pairStrings;

    public PairAdapter(List<PairString> pairStrings){
        this.pairStrings = pairStrings;
    }
    public void update(List<PairString> pairStrings){
        this.pairStrings = pairStrings;
        notifyDataSetChanged();
    }

    @Override
    public PairViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pair, parent, false);
        return new PairViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PairViewHolder holder, int position) {
        holder.tvFirst.setText(pairStrings.get(position).getFirstString());
        holder.tvSecond.setText(pairStrings.get(position).getSecondString());
    }

    @Override
    public int getItemCount() {
        return pairStrings.size();
    }

    public class PairViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvFirst)
        TextView tvFirst;
        @BindView(R.id.tvSecond)
        TextView tvSecond;

        public PairViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
