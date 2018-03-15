package com.jim.mpviews;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jim.mpviews.adapters.ReportViewAdapter;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Sirojiddin on 03.03.2018.
 */

public class ReportView {

    public static class Builder {

        Context context;
        int[] weight;
        FrameLayout view;
        Object[][] objects;
        int[] dataTypes;
        String[] titles;
        RecyclerView recyclerView;
        int[] alignTypes;
        int sorting = -1;
        int defaultSort = 0;
        ReportViewAdapter adapter;
        Object[][] statusTypes;
        private OnReportItemActionClicked listener;

        public Builder setTitles(String[] titles) {
            this.titles = titles;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setWeight(int[] weight) {
            this.weight = weight;
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

        public Builder setDataAlignTypes(int[] alignTypes) {
            this.alignTypes = alignTypes;
            return this;
        }

        public Builder setDefaultSort(int defaultSort) {
            this.defaultSort = defaultSort;
            return this;
        }

        public Builder setStatusTypes(Object[][] statusTypes) {
            this.statusTypes = statusTypes;
            return this;
        }

        public Builder setListener(OnReportItemActionClicked listener) {
            this.listener = listener;
            return this;
        }

        public FrameLayout build() {
            recyclerView = new RecyclerView(context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new ReportViewAdapter(context);
            recyclerView.setAdapter(adapter);
            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            adapter.setData(objects, weight, dataTypes, titles, alignTypes, statusTypes);
            sortObjects(defaultSort);
            adapter.setListener(new ReportViewAdapter.OnItemClickListener() {
                @Override
                public void onSort(int position) {
                    sortObjects(position);
                }

                @Override
                public void onAction(int rowPosition, int colPosition) {
                    if (listener != null)
                        listener.onAction(rowPosition, colPosition);
                }
            });
            view = new FrameLayout(context);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(layoutParams);
            view.addView(recyclerView);
            return view;
        }

        private void sortObjects(final int position) {
            sorting *= -1;
            Arrays.sort(objects, (objects, t1) -> {
                if (objects[position] instanceof Long && t1[position] instanceof Long) {
                    Long ob1 = (Long) objects[position];
                    Long ob2 = (Long) t1[position];
                    return ob1.compareTo(ob2) * sorting;
                }
                if (objects[position] instanceof String && t1[position] instanceof String) {
                    String ob1 = (String) objects[position];
                    String ob2 = (String) t1[position];
                    return ob1.toUpperCase().compareTo(ob2.toUpperCase()) * sorting;
                }
                if (objects[position] instanceof Integer && t1[position] instanceof Integer) {
                    Integer ob1 = (Integer) objects[position];
                    Integer ob2 = (Integer) t1[position];
                    return ob1.compareTo(ob2) * sorting;
                }
                if (objects[position] instanceof Double && t1[position] instanceof Double) {
                    Double ob1 = (Double) objects[position];
                    Double ob2 = (Double) t1[position];
                    return ob1.compareTo(ob2) * sorting;
                }
                return 0;
            });
            adapter.notifyDataSetChanged();
        }
    }

    public interface OnReportItemActionClicked {
        void onAction(int rowPosition, int colPosition);
    }
}
