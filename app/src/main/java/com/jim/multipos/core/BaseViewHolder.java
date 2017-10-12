package com.jim.multipos.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 12.10.2017.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    protected View view;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        ButterKnife.bind(this, itemView);
    }
}
