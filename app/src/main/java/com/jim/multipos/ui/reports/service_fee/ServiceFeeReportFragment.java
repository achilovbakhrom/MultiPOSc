package com.jim.multipos.ui.reports.service_fee;

import android.os.Bundle;
import android.view.Gravity;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.ServiceFeeLog;
import com.jim.multipos.ui.reports.discount.dialogs.DiscountFilterDialog;

import javax.inject.Inject;

public class ServiceFeeReportFragment extends BaseTableReportFragment implements ServiceFeeReportView{

    @Inject
    ServiceFeeReportPresenter presenter;
    private ReportView.Builder itemBuilder, orderBuilder, logBuilder;
    private ReportView itemReportView, orderReportView, logReportView;

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        setChoiserPanel(new String[]{getString(R.string.item_service_fee), getString(R.string.order_service_fee), getString(R.string.service_fee_log)});
        initValues();
        presenter.onCreateView(savedInstanceState);
    }

    @Override
    public void initTable(Object[][] objects) {
        itemReportView.getBuilder().init(objects);
        setTable(itemReportView.getBuilder().getView());
    }

    @Override
    public void updateTable(Object[][] objects, int position) {
        switch (position) {
            case 0:
                itemReportView.getBuilder().update(objects);
                setTable(itemReportView.getBuilder().getView());
                break;
            case 1:
                orderReportView.getBuilder().update(objects);
                setTable(orderReportView.getBuilder().getView());
                break;
            case 2:
                logReportView.getBuilder().update(objects);
                setTable(logReportView.getBuilder().getView());
                break;
        }
    }

    @Override
    public void setSearchResults(Object[][] objectResults, String searchText, int position) {
        switch (position) {
            case 0:
                itemReportView.getBuilder().searchResults(objectResults, searchText);
                setTable(itemReportView.getBuilder().getView());
                break;
            case 1:
                orderReportView.getBuilder().searchResults(objectResults, searchText);
                setTable(orderReportView.getBuilder().getView());
                break;
            case 2:
                logReportView.getBuilder().searchResults(objectResults, searchText);
                setTable(logReportView.getBuilder().getView());
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

    public void initValues() {
        initDefaults();
        itemBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(firstTitles)
                .setDataTypes(firstDataType)
                .setWeight(firstWeights)
                .setDataAlignTypes(firstAligns)
                .setStatusTypes(singleStatusTypes)
                .build();
        orderBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(secondTitles)
                .setDataTypes(secondDataType)
                .setWeight(secondWeights)
                .setDataAlignTypes(secondAligns)
                .setStatusTypes(singleStatusTypes)
                .build();
        logBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(thirdTitles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .setStatusTypes(multipleStatusTypes)
                .build();
        itemReportView = new ReportView(itemBuilder);
        orderReportView = new ReportView(orderBuilder);
        logReportView = new ReportView(logBuilder);
    }

    private void initDefaults() {
        singleStatusTypes = new Object[][][]{
                {{0, getContext().getString(R.string.static_type), R.color.colorMainText},
                        {1, getContext().getString(R.string.manual), R.color.colorBlue}}
        };
        multipleStatusTypes = new Object[][][]{
                {{ServiceFeeLog.SERVICE_FEE_ADDED, getContext().getString(R.string.add), R.color.colorGreen},
                        {ServiceFeeLog.SERVICE_FEE_CANCELED, getString(R.string.canceled), R.color.colorBlue},
                        {ServiceFeeLog.SERVICE_FEE_DELETED, getString(R.string.deleted), R.color.colorRed}},
                {{ServiceFee.PERCENT, getContext().getString(R.string.percentage), R.color.colorMainText},
                        {ServiceFee.VALUE, getContext().getString(R.string.value), R.color.colorMainText}},
                {{ServiceFee.ORDER, getContext().getString(R.string.order), R.color.colorMainText},
                        {ServiceFee.ITEM, getContext().getString(R.string.item), R.color.colorMainText}},
                {{0, getContext().getString(R.string.static_type), R.color.colorMainText},
                        {1, getContext().getString(R.string.manual), R.color.colorBlue}}
        };
        firstTitles = new String[]{getString(R.string.order_num), getString(R.string.till_number), getString(R.string.item), getString(R.string.sku), getString(R.string.qty), getString(R.string.service_fee_value), getString(R.string.date), getString(R.string.reason), getString(R.string.type)};
        secondTitles = new String[]{getString(R.string.order_num), getString(R.string.till_number), getString(R.string.order_subtotal), getString(R.string.customer), getString(R.string.service_fee_value), getString(R.string.date), getString(R.string.reason), getString(R.string.type)};
        thirdTitles = new String[]{getString(R.string.date), getString(R.string.operation), getString(R.string.reason), getString(R.string.value), getString(R.string.amount_type), getString(R.string.usage_type), getString(R.string.type)};
    }

    private Object[][][] singleStatusTypes;
    private Object[][][] multipleStatusTypes;
    private String thirdTitles[];
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
