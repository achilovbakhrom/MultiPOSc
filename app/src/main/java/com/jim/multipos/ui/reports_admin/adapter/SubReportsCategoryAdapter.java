package com.jim.multipos.ui.reports_admin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.ui.main_menu.MenuListAdapter;
import com.jim.multipos.ui.reports_admin.model.SubReportsCategory;
import com.jim.multipos.utils.OnItemClickListener;

import java.util.List;

import butterknife.BindView;

public class SubReportsCategoryAdapter extends RecyclerView.Adapter<SubReportsCategoryAdapter.ViewHolder> {

    List<SubReportsCategory> subCategories;

    public SubReportsCategoryAdapter(List<SubReportsCategory> subCategories) {
        this.subCategories = subCategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMenuItem.setText(subCategories.get(position).getTitle());
        holder.tvMenuItemDescription.setText(subCategories.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvMenuItem)
        TextView tvMenuItem;
        @BindView(R.id.tvMenuItemDescription)
        TextView tvMenuItemDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view1 -> {
                if(onItemClickListener!=null)
                    onItemClickListener.onItemClicked(getAdapterPosition());
            });
        }
    }
}
