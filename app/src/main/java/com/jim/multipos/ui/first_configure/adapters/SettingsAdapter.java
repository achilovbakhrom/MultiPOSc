package com.jim.multipos.ui.first_configure.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpCompletedStateView;
import com.jim.multipos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 10.10.17.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {
    public interface OnClickListener {
        void onClick(int position, int nextPosition);
    }

    private Context context;
    private OnClickListener onClickListener;
    private String[] titles;
    private String[] descriptions;
    private int current = 0;
    private int[] isCompletedFragments;

    public SettingsAdapter(Context context, OnClickListener onClickListener, String[] titles, String[] descriptions, int[] isCompletedFragments) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.titles = titles;
        this.descriptions = descriptions;
        this.isCompletedFragments = isCompletedFragments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.settings_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*holder.chbDone.setCheckBoxClickable(false);
        holder.chbDone.setEnabled(false);*/
        holder.tvTitle.setText(titles[position]);
        holder.tvDescription.setText(descriptions[position]);

        if (current == position) {
            holder.flStrip.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(getColor(R.color.colorWhite));

        } else {
            holder.flStrip.setVisibility(View.INVISIBLE);
            holder.itemView.setBackgroundColor(getColor(R.color.colorBackgroundGrey));
        }

        /*if (isCompletedFragments[position]) {
            holder.chbDone.setChecked(true);
        } else {
            holder.chbDone.setChecked(false);
        }*/

        switch (isCompletedFragments[position]) {
            case MpCompletedStateView.EMPTY_STATE:
                holder.completedStateView.setState(MpCompletedStateView.EMPTY_STATE);
                break;
            case MpCompletedStateView.COMPLETED_STATE:
                holder.completedStateView.setState(MpCompletedStateView.COMPLETED_STATE);
                break;
            case MpCompletedStateView.WARNING_STATE:
                holder.completedStateView.setState(MpCompletedStateView.WARNING_STATE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public void updateAdapter(int position) {
        notifyItemChanged(current);
        notifyItemChanged(position);
        current = position;
    }

    private int getColor(int resId) {
        return context.getResources().getColor(resId);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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

            completedStateView.setState(MpCompletedStateView.EMPTY_STATE);

            RxView.clicks(itemView).subscribe(o -> {
                onClickListener.onClick(current, getAdapterPosition());
            });
        }
    }
}
