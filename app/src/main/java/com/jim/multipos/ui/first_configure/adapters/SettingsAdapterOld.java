package com.jim.multipos.ui.first_configure.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 27.07.17.
 */

public class SettingsAdapterOld extends RecyclerView.Adapter<SettingsAdapterOld.ViewHolder> {
    private int current;
    private Context context;
    private boolean isCheckedItems[];
    private List<TitleDescription> titleDescriptions;
    //private FirstConfigureLeftSideFragment fragment;
    private OnClick itemClickCallback;

    //TODO was comment

    public SettingsAdapterOld(Context context, OnClick itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
        this.context = context;
        current = 0;

    }

    public void setData(List<TitleDescription> titleDescriptions/*, FirstConfigureLeftSideFragment fragment*/) {
        this.titleDescriptions = titleDescriptions;
        isCheckedItems = new boolean[titleDescriptions.size()];
        //this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.chbDone.setCheckBoxClickable(false);
        holder.chbDone.setCheckBoxEnabled(false);
        holder.tvTitle.setText(titleDescriptions.get(position).getTitle());
        holder.tvDescription.setText(titleDescriptions.get(position).getDescription());

        if (current == position) {
            holder.flStrip.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));

        } else {
            holder.flStrip.setVisibility(View.INVISIBLE);
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorBackgroundGrey));
        }

        if (isCheckedItems[position]) {
            holder.chbDone.setChecked(true);
        } else {
            holder.chbDone.setChecked(false);
        }

    }

    public void onFragmentNextClickListener(int position, boolean isChecked, int nextPosition) {
        isCheckedItems[position] = isChecked;
        current = nextPosition;
        notifyItemChanged(nextPosition);
        notifyItemChanged(position);
    }

    public void changeCheckBoxIndicator(int position, boolean isChecked) {
        isCheckedItems[position] = isChecked;
        notifyItemChanged(position);
    }

    public interface OnClick {
        void itemClick(int position);
    }

    @Override
    public int getItemCount() {
        return titleDescriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.strip)
        FrameLayout flStrip;
        @BindView(R.id.chbDone)
        MpCheckbox chbDone;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //RxView.clicks(itemView).subscribe(aVoid -> fragment.itemClick(getAdapterPosition()));
        }
    }
}
