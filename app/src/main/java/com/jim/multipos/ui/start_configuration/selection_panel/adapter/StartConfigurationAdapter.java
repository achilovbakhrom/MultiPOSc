package com.jim.multipos.ui.start_configuration.selection_panel.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jim.mpviews.MpCompletedStateView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.utils.FirstConfigureListItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bakhrom on 10/17/17.
 */

public class StartConfigurationAdapter extends RecyclerView.Adapter<StartConfigurationAdapter.ViewHolder> {

    private Context context;
    private List<FirstConfigureListItem> items;
    private OnItemClicked callback;
    private int lastPos = 0;

    public StartConfigurationAdapter(Context context, List<FirstConfigureListItem> items, OnItemClicked callback) {
        this.context = context;
        this.items = items;
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FirstConfigureListItem item = items.get(position);
        holder.tvTitle.setText(item.getName());
        holder.tvDescription.setText(item.getDescription());
        int visibilty = item.isSelected() ? View.VISIBLE : View.GONE;
        int color = item.isSelected() ? R.color.colorWhite : R.color.colorBackgroundGrey;
        holder.flStrip.setVisibility(visibilty);
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, color));
        holder.completedStateView.setState(item.getState());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_item, parent, false);
        return new ViewHolder(view);
    }

    public void changeState(int position, int state) {
        items.get(position).setState(state);
        notifyDataSetChanged();
    }

    public void changePosition(int position) {
        items.get(lastPos).setSelected(false);
        items.get(position).setSelected(true);
        notifyItemChanged(lastPos);
        notifyItemChanged(position);
        lastPos = position;
    }


    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.strip)
        FrameLayout flStrip;
        @BindView(R.id.completedStateView)
        MpCompletedStateView completedStateView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                items.get(lastPos).setSelected(false);
                items.get(getAdapterPosition()).setSelected(true);
                notifyItemChanged(lastPos);
                notifyItemChanged(getAdapterPosition());
                lastPos = getAdapterPosition();
                callback.onItemClicked(getAdapterPosition());
            });
        }
    }

    public interface OnItemClicked {
        void onItemClicked(int position);
    }
}
