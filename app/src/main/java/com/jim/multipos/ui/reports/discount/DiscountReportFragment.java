package com.jim.multipos.ui.reports.discount;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.DiscountLog;
import com.jim.multipos.ui.reports.discount.dialogs.DiscountFilterDialog;

import javax.inject.Inject;

public class DiscountReportFragment extends BaseTableReportFragment implements DiscountReportView {

    @Inject
    DiscountReportPresenter presenter;
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
        setTable(itemDiscountReportView.getBuilder().getView());
    }

    @Override
    public void updateTable(Object[][] objects, int position) {
        switch (position) {
            case 0:
                itemDiscountReportView.getBuilder().update(objects);
                setTable(itemDiscountReportView.getBuilder().getView());
                break;
            case 1:
                orderDiscountReportView.getBuilder().update(objects);
                setTable(orderDiscountReportView.getBuilder().getView());
                break;
            case 2:
                discountLogReportView.getBuilder().update(objects);
                setTable(discountLogReportView.getBuilder().getView());
                break;
        }

    }

    @Override
    public void showFilterDialog(int[] filterConfig) {
        DiscountFilterDialog dialog = new DiscountFilterDialog(getContext(), filterConfig, config -> {
           presenter.filterConfigsHaveChanged(config);
           clearSearch();
        });
        dialog.show();
    }

    @Override
    public void setSearchResults(Object[][] objectResults, String searchText, int position) {
        switch (position) {
            case 0:
                itemDiscountReportView.getBuilder().searchResults(objectResults, searchText);
                setTable(itemDiscountReportView.getBuilder().getView());
                break;
            case 1:
                orderDiscountReportView.getBuilder().searchResults(objectResults, searchText);
                setTable(orderDiscountReportView.getBuilder().getView());
                break;
            case 2:
                discountLogReportView.getBuilder().searchResults(objectResults, searchText);
                setTable(discountLogReportView.getBuilder().getView());
                break;
        }
    }

    private void initDefaults() {
        firstStatusTypes = new Object[][][]{
                {{0, getContext().getString(R.string.static_type), R.color.colorMainText},
                        {1, getContext().getString(R.string.manual), R.color.colorBlue}}
        };
        statusTypes = new Object[][][]{
                {{DiscountLog.DISCOUNT_ADDED, getContext().getString(R.string.add), R.color.colorGreen},
                        {DiscountLog.DISCOUNT_CANCELED, getString(R.string.canceled), R.color.colorBlue},
                        {DiscountLog.DISCOUNT_DELETED, getString(R.string.deleted), R.color.colorRed}},
                {{Discount.PERCENT, getContext().getString(R.string.percentage), R.color.colorMainText},
                        {Discount.VALUE, getContext().getString(R.string.value), R.color.colorMainText}},
                {{Discount.ORDER, getContext().getString(R.string.order), R.color.colorMainText},
                        {Discount.ITEM, getContext().getString(R.string.item), R.color.colorMainText}},
                {{0, getContext().getString(R.string.static_type), R.color.colorMainText},
                        {1, getContext().getString(R.string.manual), R.color.colorBlue}}
        };
        firstTitles = new String[]{getString(R.string.order_num), getString(R.string.till_number), getString(R.string.item), getString(R.string.sku), getString(R.string.qty), getString(R.string.discount_value), getString(R.string.date), getString(R.string.reason), getString(R.string.type)};
        secondTitles = new String[]{getString(R.string.order_num), getString(R.string.till_number), getString(R.string.order_subtotal), getString(R.string.customer), getString(R.string.discount_value), getString(R.string.date), getString(R.string.reason), getString(R.string.type)};
        titles = new String[]{getString(R.string.date), getString(R.string.operation), getString(R.string.reason), getString(R.string.value), getString(R.string.amount_type), getString(R.string.usage_type), getString(R.string.type)};
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
    private int firstWeights[] = {15, 15, 20, 15, 10, 20, 20, 30, 15};
    private int firstAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.RIGHT, Gravity.CENTER, Gravity.LEFT, Gravity.CENTER};
    private int secondDataType[] = {
            ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.AMOUNT,
            ReportViewConstants.NAME, ReportViewConstants.AMOUNT,
            ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS};
    private int secondWeights[] = {15, 15, 20, 15, 20, 20, 30, 15};
    private int secondAligns[] = new int[]{Gravity.LEFT, Gravity.LEFT, Gravity.RIGHT, Gravity.LEFT, Gravity.RIGHT, Gravity.CENTER, Gravity.LEFT, Gravity.CENTER};
    private int dataType[] = {
            ReportViewConstants.DATE, ReportViewConstants.STATUS, ReportViewConstants.NAME,
            ReportViewConstants.AMOUNT, ReportViewConstants.STATUS, ReportViewConstants.STATUS,
            ReportViewConstants.STATUS};
    private int weights[] = new int[]{20, 10, 20, 10, 10, 10, 10};
    private int aligns[] = new int[]{Gravity.CENTER, Gravity.LEFT, Gravity.LEFT, Gravity.RIGHT, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER};
}
