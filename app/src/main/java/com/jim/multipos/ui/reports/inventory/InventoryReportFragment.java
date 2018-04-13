package com.jim.multipos.ui.reports.inventory;

import android.os.Bundle;
import android.view.Gravity;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.ui.reports.inventory.dialogs.InventoryFilterDialog;

import javax.inject.Inject;

public class InventoryReportFragment extends BaseTableReportFragment implements InventoryReportView {

    @Inject
    InventoryReportPresenter presenter;
    private ReportView firstView, secondView, thirdView, forthView;
    private int firstDataType[] = {ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME};
    private int firstWeights[] = {15, 10, 15, 8, 10, 10, 8, 8};
    private int firstAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER};
    private int secondDataType[] = {ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME};
    private int secondWeights[] = {10, 10, 10, 10, 10, 10, 10, 10};
    private int secondAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT};
    private int thirdDataType[] = {ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.NAME};
    private int thirdWeights[] = {10, 10, 10, 10, 10, 10};
    private int thirdAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.LEFT};
    private int forthDataType[] = {ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.QUANTITY, ReportViewConstants.NAME, ReportViewConstants.NAME};
    private int forthWeights[] = {10, 10, 10, 10, 10, 10, 10};
    private int forthAligns[] = {Gravity.LEFT, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER, Gravity.LEFT, Gravity.LEFT};

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        setChoiserPanel(new String[]{getString(R.string.inventory_log), getString(R.string.unventory_summary), getString(R.string.inventory_state), getString(R.string.returns)});
        initDefaults();
        presenter.onCreateView(savedInstanceState);
    }

    private void initDefaults() {
        Object[][][] status = {{{WarehouseOperations.CANCELED_SOLD, getString(R.string.canceled_order), R.color.colorMainText},
                {WarehouseOperations.CONSIGNMENT_DELETED, getString(R.string.consignment_canceled), R.color.colorMainText},
                {WarehouseOperations.INCOME_FROM_VENDOR, getString(R.string.income_from_vendor), R.color.colorMainText},
                {WarehouseOperations.RETURN_HOLDED, getString(R.string.held_product_return), R.color.colorMainText},
                {WarehouseOperations.RETURN_SOLD, getString(R.string.return_from_customer), R.color.colorMainText},
                {WarehouseOperations.RETURN_TO_VENDOR, getString(R.string.return_to_vendor), R.color.colorMainText},
                {WarehouseOperations.SOLD, getString(R.string.sold), R.color.colorMainText},
                {WarehouseOperations.VOID_INCOME, getString(R.string.void_income), R.color.colorMainText},
                {WarehouseOperations.WASTE, getString(R.string.wasted), R.color.colorMainText},}};
        String firstTitles[] = new String[]{getString(R.string.product), getString(R.string.vendor), getString(R.string.action), getString(R.string.qty), getString(R.string.date), getString(R.string.reason), getString(R.string.order), getString(R.string.consignment)};
        String secondTitles[] = new String[]{getString(R.string.product), getString(R.string.vendor), getString(R.string.sold), getString(R.string.received_from_vendor), getString(R.string.return_to_vendor), getString(R.string.return_from_customer), getString(R.string.void_income), getString(R.string.wasted)};
        String thirdTitles[] = new String[]{getString(R.string.product), getString(R.string.vendor), getString(R.string.qty), getString(R.string.unit_price), getString(R.string.total), getString(R.string.gross_qty)};
        String forthTitles[] = new String[]{getString(R.string.product), getString(R.string.date), getString(R.string.price), getString(R.string.return_cost), getString(R.string.qty), getString(R.string.payment_type), getString(R.string.description)};
        ReportView.Builder firstBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(firstTitles)
                .setDataTypes(firstDataType)
                .setWeight(firstWeights)
                .setStatusTypes(status)
                .setDataAlignTypes(firstAligns)
                .build();
        firstView = new ReportView(firstBuilder);
        ReportView.Builder secondBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(secondTitles)
                .setDataTypes(secondDataType)
                .setWeight(secondWeights)
                .setDataAlignTypes(secondAligns)
                .build();
        secondView = new ReportView(secondBuilder);
        ReportView.Builder thirdBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(thirdTitles)
                .setDataTypes(thirdDataType)
                .setWeight(thirdWeights)
                .setDataAlignTypes(thirdAligns)
                .build();
        thirdView = new ReportView(thirdBuilder);
        ReportView.Builder forthBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(forthTitles)
                .setDataTypes(forthDataType)
                .setWeight(forthWeights)
                .setDataAlignTypes(forthAligns)
                .build();
        forthView = new ReportView(forthBuilder);
    }

    @Override
    public void initTable(Object[][] objects) {
        firstView.getBuilder().init(objects);
        setTable(firstView.getBuilder().getView());
    }

    @Override
    public void updateTable(Object[][] objects, int position) {
        switch (position) {
            case 0:
                firstView.getBuilder().update(objects);
                setTable(firstView.getBuilder().getView());
                enableFilter();
                enableSearch();
                break;
            case 1:
                secondView.getBuilder().update(objects);
                setTable(secondView.getBuilder().getView());
                disableFilter();
                enableSearch();
                break;
            case 2:
                thirdView.getBuilder().update(objects);
                setTable(thirdView.getBuilder().getView());
                disableFilter();
                disableSearch();
                break;
            case 3:
                forthView.getBuilder().update(objects);
                setTable(forthView.getBuilder().getView());
                disableFilter();
                enableSearch();
                break;
        }
    }

    @Override
    public void setSearchResults(Object[][] searchResults, String searchText, int position) {
        switch (position) {
            case 0:
                firstView.getBuilder().searchResults(searchResults, searchText);
                setTable(firstView.getBuilder().getView());
                enableFilter();
                enableSearch();
                break;
            case 1:
                secondView.getBuilder().searchResults(searchResults, searchText);
                setTable(secondView.getBuilder().getView());
                disableFilter();
                enableSearch();
                break;
            case 2:
                thirdView.getBuilder().searchResults(searchResults, searchText);
                setTable(thirdView.getBuilder().getView());
                disableFilter();
                disableSearch();
                break;
            case 3:
                forthView.getBuilder().searchResults(searchResults, searchText);
                setTable(forthView.getBuilder().getView());
                disableFilter();
                enableSearch();
                break;
        }
    }

    @Override
    public void showFilterDialog(int[] filterConfig) {
        InventoryFilterDialog dialog = new InventoryFilterDialog(getContext(), filterConfig, config -> presenter.setFilterConfigs(config));
        dialog.show();
    }
}
