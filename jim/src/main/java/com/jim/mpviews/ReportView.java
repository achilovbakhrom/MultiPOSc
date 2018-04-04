package com.jim.mpviews;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.adapters.ReportViewAdapter;

import java.util.Arrays;

/**
 * Created by Sirojiddin on 03.03.2018.
 */

public class ReportView {

    private Builder builder;

    public ReportView(Builder builder) {
        this.builder = builder;
    }

    public Builder getBuilder() {
        return builder;
    }

    public static class Builder {

        Context context;
        int[] weight;
        FrameLayout view;
        Object[][] objects;
        int[] dataTypes;
        String[] titles;
        RecyclerView recyclerView;
        RecyclerViewWithMaxHeight recyclerViewWithMaxHeight;
        int[] alignTypes;
        int sorting = -1;
        int defaultSort = 0;
        int maxHeight = 0;
        ReportViewAdapter adapter;
        Object[][][] statusTypes;
        OnReportViewResponseListener listener;
        ExpandableView titleView;

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

        public Builder setViewMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
            return this;
        }

        public Builder setStatusTypes(Object[][][] statusTypes) {
            this.statusTypes = statusTypes;
            return this;
        }

        public Builder setOnReportViewResponseListener(OnReportViewResponseListener onReportViewListener) {
            this.listener = onReportViewListener;
            return this;
        }

        public Builder build() {
            //creating title for view
            titleView = new ExpandableView(context);
            titleView.setWeight(weight);
            titleView.setAlign(alignTypes);
            titleView.setHasTitle(true);
            titleView.setSize(titles.length);
            titleView.create();

            //filling data
            if (titleView.getChildAt(0) instanceof LinearLayout) {
                LinearLayout row = (LinearLayout) titleView.getChildAt(0);
                int count = 0;
                for (int i = 0; i < row.getChildCount(); i++) {
                    if (row.getChildAt(i) instanceof LinearLayout) {
                        LinearLayout col = (LinearLayout) row.getChildAt(i);
                        col.setBackgroundResource(R.color.colorReportTitleBackGround);
                        final int finalCount = count;
                        col.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sortObjects(finalCount);
                                titleView.sorted(sorting, finalCount);
                            }
                        });
                        for (int j = 0; j < col.getChildCount(); j++) {
                            if (col.getChildAt(j) instanceof TextView) {
                                TextView textView = (TextView) col.getChildAt(j);
                                textView.setText(titles[count]);
                                count++;
                            }
                        }
                    }
                }
            }

            //creating adapter
            adapter = new ReportViewAdapter(context);
            adapter.setData(objects, weight, dataTypes, alignTypes, statusTypes);
            adapter.setListener((rowPosition, colPosition) -> listener.onAction(objects, rowPosition, colPosition));
            //creating main view
            view = new FrameLayout(context);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);

            // creating container for title and recyclerView
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(linearParams);
            layout.addView(titleView);

            //check if recyclerView has max height value
            if (maxHeight == 0) {
                recyclerView = new RecyclerView(context);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
                layout.addView(recyclerView);
            } else {
                recyclerViewWithMaxHeight = new RecyclerViewWithMaxHeight(context);
                recyclerViewWithMaxHeight.setLayoutManager(new LinearLayoutManager(context));
                recyclerViewWithMaxHeight.setAdapter(adapter);
                recyclerViewWithMaxHeight.setMaxHeight(maxHeight);
                layout.addView(recyclerViewWithMaxHeight);
            }
            view.addView(layout);
            return this;
        }

        public Builder init(Object[][] objects) {
            this.objects = objects;
            adapter.setData(objects, weight, dataTypes, alignTypes, statusTypes);
            sortObjects(defaultSort);
            adapter.notifyDataSetChanged();
            return this;
        }

        public Builder update(Object[][] objects) {
            this.objects = objects;
            adapter.setData(objects, weight, dataTypes, alignTypes, statusTypes);
            adapter.notifyDataSetChanged();
            return this;
        }

        public Builder searchResults(Object[][] objects, String searchedText) {
            this.objects = objects;
            adapter.setData(objects, weight, dataTypes, alignTypes, statusTypes);
            adapter.setSearchText(searchedText);
            adapter.notifyDataSetChanged();
            return this;
        }

        public FrameLayout getView() {
            return view;
        }

        //sorting objects
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

    public interface OnReportViewResponseListener {
        void onAction(Object[][] objects, int row, int column);
    }
}
