package com.jim.multipos.ui.reports.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportPickerAdapter extends RecyclerView.Adapter<ReportPickerAdapter.ReportPickerViewHolder>{
    List<String> reportNames;
    private ReportPickerListner reportPickerListner;
    int currentPosition = 0;
    public interface ReportPickerListner{
        void onReportClick(int reportPicker);
    }

    public ReportPickerAdapter(List<String> reportNames,ReportPickerListner reportPickerListner){
        this.reportPickerListner = reportPickerListner;
        this.reportNames = reportNames;
    }
    @Override
    public ReportPickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_picker_item, parent, false);
        return new ReportPickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportPickerViewHolder holder, int position) {
        int pL = holder.tvReportName.getPaddingLeft();
        int pT = holder.tvReportName.getPaddingTop();
        int pR = holder.tvReportName.getPaddingRight();
        int pB = holder.tvReportName.getPaddingBottom();
        if(currentPosition == position){
            holder.tvReportName.setBackgroundResource(R.drawable.pressed_effect);
            holder.tvReportName.setTextColor(Color.parseColor("#ffffff"));
            holder.tvReportName.setPadding(pL, pT, pR, pB);
        }else {
            holder.tvReportName.setBackgroundResource(0);
            holder.tvReportName.setTextColor(Color.parseColor("#aed3f0"));
            holder.tvReportName.setPadding(pL, pT, pR, pB);
        }
        holder.tvReportName.setText(reportNames.get(position));
    }

    @Override
    public int getItemCount() {
        return reportNames.size();
    }

    class ReportPickerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvReportName)
        TextView tvReportName;

        public ReportPickerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener((view)->{
                reportPickerListner.onReportClick(getAdapterPosition());
                int oldPosition = currentPosition;
                currentPosition = getAdapterPosition();
                notifyItemChanged(oldPosition);
                notifyItemChanged(currentPosition);
            });
        }
    }

}
