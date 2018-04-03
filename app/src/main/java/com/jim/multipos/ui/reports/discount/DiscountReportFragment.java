package com.jim.multipos.ui.reports.discount;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.order.Order;

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
        initDefaults();
        itemDiscountBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(firstTitles)
                .setDataTypes(firstDataType)
                .setWeight(firstWeights)
                .setDataAlignTypes(firstAligns)
                .setStatusTypes(firstStatusTypes)
                .setOnReportViewResponseListener((objects1, row, column) -> {

                })
                .build();
        orderDiscountBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(secondTitles)
                .setDataTypes(secondDataType)
                .setWeight(secondWeights)
                .setDataAlignTypes(secondAligns)
                .setStatusTypes(firstStatusTypes)
                .setOnReportViewResponseListener((objects1, row, column) -> {

                })
                .build();
        discountLogBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .setStatusTypes(statusTypes)
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

    private void initDefaults() {
        firstStatusTypes = new Object[][][]{
                {{0, getContext().getString(R.string.static_type), R.color.colorMainText},
                        {1, getContext().getString(R.string.manual), R.color.colorMainText}}
        };
        statusTypes = new Object[][][]{
                {{Discount.PERCENT, getContext().getString(R.string.percentage), R.color.colorMainText},
                        {Discount.VALUE, getContext().getString(R.string.value), R.color.colorMainText}},
                {{Discount.ORDER, getContext().getString(R.string.order), R.color.colorMainText},
                        {Discount.ITEM, getContext().getString(R.string.item), R.color.colorMainText}},
                {{0, getContext().getString(R.string.static_type), R.color.colorMainText},
                        {1, getContext().getString(R.string.manual), R.color.colorMainText}}
        };
        firstTitles = new String[]{"Order#", "Till number", "Item", "SKU", "Qty", "Discount value", "Date", "Reason", "Type"};
        secondTitles = new String[]{"Order#", "Till number", "Order subtotal", "Customer", "Qty", "Discount value", "Date", "Reason", "Type"};
        titles = new String[]{"Date", "Operation", "Reason", "Value", "Amount type", "Usage type", "Type"};
    }

    private Object[][][] firstStatusTypes;
    private Object[][][] statusTypes;
    private String titles[];
    private String firstTitles[];
    private String secondTitles[];

    private int firstDataType[] = {
            ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME,
            ReportViewConstants.NAME, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT,
            ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS};
    private int firstWeights[] = {10, 10, 10, 10, 10, 10, 10, 10, 10};
    private int firstAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT};
    private int secondDataType[] = {
            ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.AMOUNT,
            ReportViewConstants.NAME, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT,
            ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS};
    private int secondWeights[] = {10, 10, 10, 10, 10, 10, 10, 10, 10};
    private int secondAligns[] = new int[]{Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT};
    private int dataType[] = {
            ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.NAME,
            ReportViewConstants.AMOUNT, ReportViewConstants.STATUS, ReportViewConstants.STATUS,
            ReportViewConstants.STATUS};
    private int weights[] = new int[]{10, 10, 10, 10, 10, 10, 10};
    private int aligns[] = new int[]{Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT};
}
