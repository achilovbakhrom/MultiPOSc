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
    private ReportView.Builder builder;
    private ReportView reportView;
    private int dataType[];
    private int weights[];
    private int aligns[];
    private String titles[];

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        setChoiserPanel(new String[]{"Item discount", "Order discount", "Discount creation log"});
        initItemDiscount();
        builder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .setOnReportViewResponseListener((objects1, row, column) -> {

                })
                .build();
        reportView = new ReportView(builder);
        presenter.onCreateView(savedInstanceState);
    }

    @Override
    public void initItemDiscount() {
        dataType = new int[]{
                ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME,
                ReportViewConstants.NAME, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT,
                ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS};
        weights = new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10};
        aligns = new int[]{Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT};
        titles = new String[]{"Order#", "Till number", "Item", "SKU", "Qty", "Discount value", "Date", "Reason", "Type"};
    }

    @Override
    public void initOrderDiscount() {
        dataType = new int[]{
                ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.AMOUNT,
                ReportViewConstants.NAME, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT,
                ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS};
        weights = new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10};
        aligns = new int[]{Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT};
        titles = new String[]{"Order#", "Till number", "Order subtotal", "Customer", "Qty", "Discount value", "Date", "Reason", "Type"};
    }

    @Override
    public void initDiscountCreationLog() {
        dataType = new int[]{
                ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.NAME,
                ReportViewConstants.AMOUNT, ReportViewConstants.STATUS, ReportViewConstants.STATUS,
                ReportViewConstants.STATUS};
        weights = new int[]{10, 10, 10, 10, 10, 10, 10};
        aligns = new int[]{Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT};
        titles = new String[]{"Date", "Operation", "Reason", "Value", "Amount type", "Usage type", "Type"};
    }

    @Override
    public void initTable(Object[][] objects) {
        reportView.getBuilder().init(objects);
        fl = reportView.getBuilder().getView();
        setTable(fl);
    }
}
