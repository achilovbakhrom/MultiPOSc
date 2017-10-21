package com.jim.multipos.ui.first_configure_last.adapter;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jim.mpviews.MpCompletedStateView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ClickableBaseAdapter;

import java.util.List;

import android.content.Context;

import butterknife.BindView;

/**
 * Created by bakhrom on 10/17/17.
 */

public class FirstConfigureListAdapter extends ClickableBaseAdapter<FirstConfigureListItem, FirstConfigureListAdapter.ViewHolder> {

    private Context context;
    private int lastPos = 0;

    public FirstConfigureListAdapter(Context context, List<FirstConfigureListItem> items) {
        super(items);
        this.context = context;
    }

    @Override
    protected void onItemClicked(ViewHolder holder, int position) {
        Log.d("MultiPOS", "onItemClicked: " + position);
    }

    public void changePosition(int position) {
        items.get(lastPos).setSelected(false);
        items.get(position).setSelected(true);
        lastPos = position;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_item, parent, false);
        return new ViewHolder(view);
    }

    public void changeState(int position, int state) {
        items.get(position).setState(state);
        notifyDataSetChanged();
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
        public ViewHolder(View itemView) { super(itemView); }
    }
}
