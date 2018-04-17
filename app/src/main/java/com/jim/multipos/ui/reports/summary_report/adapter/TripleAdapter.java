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

public class TripleAdapter extends RecyclerView.Adapter<TripleAdapter.TripleViewHolder>{
    List<TripleString> tripleStrings;
    public TripleAdapter(List<TripleString> tripleStrings){
        this.tripleStrings = tripleStrings;
    }
    public void update(List<TripleString> tripleStrings){
        this.tripleStrings = tripleStrings;
        notifyDataSetChanged();
    }
    @Override
    public TripleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_triple, parent, false);
        return new TripleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TripleViewHolder holder, int position) {
        holder.tvFirst.setText(tripleStrings.get(position).getFirstString());
        holder.tvSecond.setText(tripleStrings.get(position).getSecondString());
        holder.tvThree.setText(tripleStrings.get(position).getThirdString());
    }

    @Override
    public int getItemCount() {
        return tripleStrings.size();
    }

    public class TripleViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvFirst)
        TextView tvFirst;
        @BindView(R.id.tvSecond)
        TextView tvSecond;
        @BindView(R.id.tvThree)
        TextView tvThree;

        public TripleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
