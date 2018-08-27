package com.jim.multipos.ui.admin_main_page.fragments.company.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
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
    int lastPos = -1;
    OnItemClick click;

    public CompanyInfoAdapter(ArrayList<CompanyModel> items, Context context, OnItemClick click) {
        this.items = items;
        this.context = context;
        this.click = click;
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
            click.onClick(position);
            lastPos = position;
            notifyDataSetChanged();
        });
        if (lastPos == position) {
            holder.ivCompanyCheck.setImageResource(R.drawable.company_selected);
            holder.tvCompanyName.setTextColor(Color.WHITE);
            holder.tvCompanyDescription.setTextColor(Color.WHITE);
            ((CardView)holder.itemView).setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAdminPageSelectedItemColor));
        } else {
            holder.ivCompanyCheck.setImageResource(R.drawable.company_unselected);
            holder.tvCompanyName.setTextColor(holder.defTitleColor);
            holder.tvCompanyDescription.setTextColor(holder.defDescriptionColor);
            ((CardView)holder.itemView).setCardBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClick{
        void onClick(int pos);
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

        int defTitleColor, defDescriptionColor;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            defTitleColor = tvCompanyName.getCurrentTextColor();
            defDescriptionColor = tvCompanyDescription.getCurrentTextColor();
        }
    }
}
