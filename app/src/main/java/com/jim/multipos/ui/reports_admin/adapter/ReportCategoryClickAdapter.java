package com.jim.multipos.ui.reports_admin.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.ui.reports_admin.model.ReportsCategory;
import com.jim.multipos.utils.OnItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;

public class ReportCategoryClickAdapter extends RecyclerView.Adapter<ReportCategoryClickAdapter.ViewHolder>{

    private OnItemClickListener onItemClickListener;
    private ArrayList<ReportsCategory> items;
    CardView cardView;
    int rowIndex = -1;

    public ReportCategoryClickAdapter(ArrayList<ReportsCategory> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_admin_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivReports.setImageResource(items.get(position).getId());
        holder.tvReportsName.setText(items.get(position).getTitle());
        if(rowIndex == position)
            holder.setSelectedColors(position);
        else
            holder.setDefaultColors();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void changeSelectedColor(int oldPos, int newPos){

    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.ivReports)
        ImageView ivReports;
        @BindView(R.id.tvReportsName)
        TextView tvReportsName;
        @BindView(R.id.cvReports)
        CardView cvReports;

        int color;

        public ViewHolder(View itemView) {
            super(itemView);
            color = tvReportsName.getCurrentTextColor();
            itemView.setOnClickListener(v -> {
                if(onItemClickListener!=null)
                    onItemClickListener.onItemClicked(getAdapterPosition());
                rowIndex = getAdapterPosition();
                notifyDataSetChanged();
            });

        }

        public void setDefaultColors(){
            cvReports.setCardBackgroundColor(Color.parseColor("#ffffff"));
            tvReportsName.setTextColor(color);
        }

        public void setSelectedColors(int pos){
            cvReports.setCardBackgroundColor(Color.parseColor("#52ace5"));
            tvReportsName.setTextColor(Color.parseColor("#ffffff"));
            switch (pos){
                case 0:
                    ivReports.setImageResource(R.drawable.finance_report_selected);
                    break;
                case 1:
                    ivReports.setImageResource(R.drawable.product_report_selected);
                    break;
                case 2:
                    ivReports.setImageResource(R.drawable.stock_report_selected);
                    break;
                case 3:
                    ivReports.setImageResource(R.drawable.vendors_report_selected);
                    break;
                case 4:
                    ivReports.setImageResource(R.drawable.debts_report_selected);
                    break;
                case 5:
                    ivReports.setImageResource(R.drawable.discount_report_selected);
                    break;
                case 6:
                    ivReports.setImageResource(R.drawable.sales_report_selected);
                    break;
                case 7:
                    ivReports.setImageResource(R.drawable.tills_report_selected);
                    break;
                case 8:
                    ivReports.setImageResource(R.drawable.staff_report_selected);
                    break;
                case 9:
                    ivReports.setImageResource(R.drawable.customer_report_selected);
                    break;
                case 10:
                    ivReports.setImageResource(R.drawable.service_fee_report_selected);
                    break;
                case 11:
                    ivReports.setImageResource(R.drawable.pos_report_selected);
                    break;
            }
        }
    }
}
