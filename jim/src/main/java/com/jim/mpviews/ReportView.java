package com.jim.mpviews;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jim.mpviews.adapters.ReportViewAdapter;

/**
 * Created by Sirojiddin on 03.03.2018.
 */

public class ReportView {

    public static class Builder {

        protected Context context;
        protected int[] weight;
        protected FrameLayout view;
        protected Object[][] objects;
        protected int[] dataTypes;
        RecyclerView recyclerView;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setWeight(int[] weight) {
            this.weight = weight;
            return this;
        }

        public Builder setView(FrameLayout view) {
            this.view = view;
            return this;
        }

        public Builder setObjects(Object[][] objects) {
            this.objects = objects;
            return this;
        }

        public Builder setDataTypes(int[] dataTypes) {
            this.dataTypes = dataTypes;
            return this;
        }

        public Builder createList() {
            recyclerView = new RecyclerView(context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            ReportViewAdapter adapter = new ReportViewAdapter(context);
            recyclerView.setAdapter(adapter);
            adapter.setData(objects, weight, dataTypes);
            return this;
        }

        public FrameLayout build() {
            view = new FrameLayout(context);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(layoutParams);
            view.addView(recyclerView);
            return view;
        }
    }
}
