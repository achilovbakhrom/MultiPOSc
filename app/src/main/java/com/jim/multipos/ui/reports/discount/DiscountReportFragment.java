package com.jim.multipos.ui.reports.discount;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.core.BaseTableReportFragment;

import javax.inject.Inject;

public class DiscountReportFragment extends BaseTableReportFragment implements DiscountReportView {

    @Inject
    DiscountReportPresenter presenter;
    private FrameLayout fl;
    private ReportView.Builder itemDiscountBuilder, orderDiscountBuilder, discountLogBuilder;
    private ReportView itemDiscountReportView, orderDiscountReportView, discountLogReportView;

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        setChoiserPanel(new String[]{"Item discount", "Order discount", "Discount creation log"});
        initValues();
        presenter.onCreateView(savedInstanceState);
    }

    @Override
    public void initValues() {
        itemDiscountBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(firstTitles)
                .setDataTypes(firstDataType)
                .setWeight(firstWeights)
                .setDataAlignTypes(firstAligns)
                .setOnReportViewResponseListener((objects1, row, column) -> {

                })
                .build();
        orderDiscountBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(secondTitles)
                .setDataTypes(secondDataType)
                .setWeight(secondWeights)
                .setDataAlignTypes(secondAligns)
                .setOnReportViewResponseListener((objects1, row, column) -> {

                })
                .build();
        discountLogBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .setOnReportViewResponseListener((objects1, row, column) -> {

                })
                .build();
        itemDiscountReportView = new ReportView(itemDiscountBuilder);
        orderDiscountReportView = new ReportView(orderDiscountBuilder);
        discountLogReportView = new ReportView(discountLogBuilder);
    }

    @Override
    public void initTable(Object[][] objects) {
        itemDiscountReportView.getBuilder().init(objects);
        fl = itemDiscountReportView.getBuilder().getView();
        setTable(fl);
    }

    @Override
    public void updateTable(Object[][] objects, int position) {
        switch (position) {
            case 0:
                itemDiscountReportView.getBuilder().update(objects);
                fl = itemDiscountReportView.getBuilder().getView();
                break;
            case 1:
                orderDiscountReportView.getBuilder().update(objects);
                fl = orderDiscountReportView.getBuilder().getView();
                break;
            case 2:
                discountLogReportView.getBuilder().update(objects);
                fl = discountLogReportView.getBuilder().getView();
                break;
        }
        setTable(fl);
    }

    private int firstDataType[] = {
            ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME,
            ReportViewConstants.NAME, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT,
            ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS};
    private int firstWeights[] = {10, 10, 10, 10, 10, 10, 10, 10, 10};
    private int firstAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT};
    private String firstTitles[] = {"Order#", "Till number", "Item", "SKU", "Qty", "Discount value", "Date", "Reason", "Type"};
    private int secondDataType[] = {
            ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.AMOUNT,
            ReportViewConstants.NAME, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT,
            ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS};
    private int secondWeights[] = {10, 10, 10, 10, 10, 10, 10, 10, 10};
    private int secondAligns[] = new int[]{Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT};
    private String secondTitles[] = {"Order#", "Till number", "Order subtotal", "Customer", "Qty", "Discount value", "Date", "Reason", "Type"};
    private int dataType[] = {
            ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.NAME,
            ReportViewConstants.AMOUNT, ReportViewConstants.STATUS, ReportViewConstants.STATUS,
            ReportViewConstants.STATUS};
    private int weights[] = new int[]{10, 10, 10, 10, 10, 10, 10};
    private int aligns[] = new int[]{Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT};
    private String titles[] = new String[]{"Date", "Operation", "Reason", "Value", "Amount type", "Usage type", "Type"};
}
