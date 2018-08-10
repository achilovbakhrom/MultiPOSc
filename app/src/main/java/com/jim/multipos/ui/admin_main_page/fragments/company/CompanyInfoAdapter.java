package com.jim.multipos.ui.admin_main_page.fragments.company;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.ui.admin_main_page.fragments.company.model.CompanyModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompanyInfoAdapter extends RecyclerView.Adapter<CompanyInfoAdapter.ViewHolder> {

    ArrayList<CompanyModel> items;
    Context context;
    OnItemClick itemClick;
    int lastPos = 0;

    public CompanyInfoAdapter(ArrayList<CompanyModel> items, Context context, OnItemClick click) {
        this.items = items;
        this.context = context;
        this.itemClick = click;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_company_info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompanyModel model = items.get(position);
        holder.tvCompanyDescription.setText(model.getCompanyDescription());
        holder.tvCompanyName.setText(model.getCompanyName());
        holder.itemView.setOnClickListener(v -> {
            itemClick.onClick(position);
            lastPos = position;
            notifyDataSetChanged();
        });
        if (lastPos == position) {
            holder.ivCompanyCheck.setImageResource(R.drawable.company_selected);
            holder.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.cardview_border));
            holder.tvCompanyName.setTextColor(ContextCompat.getColor(context, R.color.colorBlueSecond));
        } else {
            holder.ivCompanyCheck.setImageResource(R.drawable.company_unselected);
            holder.layout.setBackgroundResource(0);
            holder.tvCompanyName.setTextColor(holder.defColor);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClick {
        void onClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivCompany)
        ImageView ivCompany;
        @BindView(R.id.tvCompanyName)
        TextView tvCompanyName;
        @BindView(R.id.tvCompanyDescription)
        TextView tvCompanyDescription;
        @BindView(R.id.ivCompanyCheck)
        ImageView ivCompanyCheck;
        @BindView(R.id.layout)
        LinearLayout layout;

        int defColor;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            defColor = tvCompanyDescription.getCurrentTextColor();
        }
    }
}
