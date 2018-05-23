package com.jim.multipos.ui.reports.inventory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.till.Till;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TillPickerAdapter extends RecyclerView.Adapter<TillPickerAdapter.TillPickerViewHolder> {

    private List<Till> tills;
    private OnTillPicked onTillPicked;
    private Context context;
    private SimpleDateFormat simpleDateFormat;

    public TillPickerAdapter(List<Till> tills, OnTillPicked onTillPicked, Context context) {
        this.tills = tills;
        this.onTillPicked = onTillPicked;
        this.context = context;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    @Override
    public TillPickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.till_item, parent, false);
        return new TillPickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TillPickerViewHolder holder, int position) {
        holder.tvTillId.setText(context.getString(R.string.till)+ "#" + tills.get(position).getId());
        holder.tvFromDate.setText(simpleDateFormat.format(tills.get(position).getOpenDate()));
        holder.tvToDate.setText(simpleDateFormat.format(tills.get(position).getCloseDate()));
    }

    @Override
    public int getItemCount() {
        return tills.size();
    }

    class TillPickerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTillId)
        TextView tvTillId;
        @BindView(R.id.tvFromDate)
        TextView tvFromDate;
        @BindView(R.id.tvToDate)
        TextView tvToDate;
        @BindView(R.id.llContainer)
        LinearLayout llContainer;

        public TillPickerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            llContainer.setOnClickListener(view -> onTillPicked.onTillPicked(tills.get(getAdapterPosition()), getAdapterPosition()));
        }
    }

    public interface OnTillPicked{
        void onTillPicked(Till till, int position);
    }

}
