package com.jim.multipos.ui.reports.stock_operations;

import android.os.Bundle;
import android.view.Gravity;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.ui.reports.debts.dialogs.DebtFilterDialog;
import com.jim.multipos.ui.reports.stock_operations.dialog.IncomeFilterDialog;
import com.jim.multipos.ui.reports.stock_operations.dialog.OutcomeFilterDialog;

import javax.inject.Inject;

public class StockOperationFragment extends BaseTableReportFragment implements StockOperationView{
    @Inject
    StockOperationPresenter presenter;

    private ReportView firstView, secondView, thirdView, forthView, fifthView;
    private int firstDataType[] = {ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.NAME};
    private int firstWeights[] = {15, 10, 10, 10, 10, 10, 10, 10, 7};
    private int firstAligns[] = {Gravity.LEFT,Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT};

    private int secondDataType[] = {ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.ACTION, ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.AMOUNT,ReportViewConstants.AMOUNT,ReportViewConstants.NAME};
    private int secondWeights[] = {10, 8, 6, 8, 8, 8, 8, 10};
    private int secondAligns[] = {Gravity.LEFT, Gravity.CENTER,Gravity.CENTER,Gravity.LEFT,Gravity.LEFT,Gravity.RIGHT,Gravity.RIGHT,Gravity.LEFT};

    private int thirdDataType[] = {ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.ACTION, ReportViewConstants.DATE, ReportViewConstants.AMOUNT,ReportViewConstants.AMOUNT, ReportViewConstants.NAME};
    private int thirdWeights[] = {10, 8, 6, 8, 8, 8, 8, 10};
    private int thirdAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT};

    private int forthDataType[] = {ReportViewConstants.ID, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.ACTION, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT,ReportViewConstants.NAME};
    private int forthWeights[] = {8, 10, 10, 8, 8, 10, 10, 8, 8 , 12};
    private int forthAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.LEFT};

    private int fifthDataTypes[] = {ReportViewConstants.ID, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.ACTION, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.NAME};
    private int fifthWeights[] = {8, 10, 8, 8, 8, 8, 10, 10};
    private int fifthAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.LEFT};

    private String firstTitles[], secondTitles[], thirdTitles[], forthTitles[], panelNames[], fifthTitles[];

    private Object[][][] secondStatusTypes, thirdStatusTypes, forthStatusTypes, fifthStatusTypes;
    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        panelNames = new String[]{"Operation Summary","Outcome logs","Income logs","Stock positions","Outcome details"};
        setChoiserPanel(panelNames);
        initDefaults();
        presenter.onCreateView(savedInstanceState);
    }
    void initDefaults(){
        disableFilter();

        secondStatusTypes = new Object[][][]{
                {
                        {OutcomeProduct.ORDER_SALES, getString(R.string.order_rep), R.color.colorGreen},
                        {OutcomeProduct.OUTVOICE_TO_VENDOR,getString(R.string.outvoice_rep),R.color.colorBlue},
                        {OutcomeProduct.WASTE, getString(R.string.waste_rep), R.color.colorRed}
                }
        };
        thirdStatusTypes = new Object[][][]{
                {
                        {IncomeProduct.INVOICE_PRODUCT, getString(R.string.invoice_rep), R.color.colorGreen},
                        {IncomeProduct.SURPLUS_PRODUCT, getString(R.string.surplus_rep), R.color.colorRed},
                        {IncomeProduct.RETURNED_PRODUCT, getString(R.string.customer_rep),R.color.colorBlue}
                }
        };

        forthStatusTypes = new Object[][][]{
                {
                        {IncomeProduct.INVOICE_PRODUCT, "INVOICE", R.color.colorGreen},
                        {IncomeProduct.SURPLUS_PRODUCT, "SURPLUS", R.color.colorRed},
                        {IncomeProduct.RETURNED_PRODUCT, "CUSTOMER RETURN",R.color.colorBlue}
                }
        };

        fifthStatusTypes = new Object[][][]{
                {
                        {OutcomeProduct.ORDER_SALES, "ORDER", R.color.colorGreen},
                        {OutcomeProduct.OUTVOICE_TO_VENDOR, "WASTE", R.color.colorRed},
                        {OutcomeProduct.WASTE, "OUTVOICE",R.color.colorBlue}
                }
        };

        firstTitles = new String[]{"Product","Sales","Recieve from Vendor","Return to Vendor","Return from customer","Surplus","Waste","Unit"};
        secondTitles = new String[]{"Product","Outcome For","Order Id","Getting type","Outcome date","Sum count","Sum cost","Reason"};
        thirdTitles = new String[]{"Product","Vendor", "Income type","Invoice id","Income Date","Count","Sum cost","Reason"};
        forthTitles = new String[]{"Stock Position id","Product","Vendor","Income type","Invoice Id","Income Date","Income Cost","Income Count","Available","Stock Keeping id"};
        fifthTitles = new String[]{"Stock Position id","Product","Outcome for","Order Id","Date","Count","Cost","Stocking keeping type"};


        ReportView.Builder firstBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(firstTitles)
                .setDataTypes(firstDataType)
                .setWeight(firstWeights)
                .setDataAlignTypes(firstAligns)
                .build();
        firstView = new ReportView(firstBuilder);
        ReportView.Builder secondBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(secondTitles)
                .setDataTypes(secondDataType)
                .setWeight(secondWeights)
                .setStatusTypes(secondStatusTypes)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onAction(objects, row, column);
                })
                .setDataAlignTypes(secondAligns)
                .build();
        secondView = new ReportView(secondBuilder);
        ReportView.Builder thirdBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(thirdTitles)
                .setDataTypes(thirdDataType)
                .setWeight(thirdWeights)
                .setStatusTypes(thirdStatusTypes)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onAction(objects, row, column);
                })
                .setDataAlignTypes(thirdAligns)
                .build();
        thirdView = new ReportView(thirdBuilder);
        ReportView.Builder forthBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(forthTitles)
                .setDataTypes(forthDataType)
                .setWeight(forthWeights)
                .setStatusTypes(forthStatusTypes)
                .setDataAlignTypes(forthAligns)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onAction(objects, row, column);
                })
                .build();
        forthView = new ReportView(forthBuilder);
        ReportView.Builder fifthBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(fifthTitles)
                .setDataTypes(fifthDataTypes)
                .setWeight(fifthWeights)
                .setStatusTypes(fifthStatusTypes)
                .setDataAlignTypes(fifthAligns)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onAction(objects, row, column);
                })
                .build();
        fifthView = new ReportView(fifthBuilder);
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
                disableFilter();
                clearSearch();
                firstView.getBuilder().update(objects);
                setTable(firstView.getBuilder().getView());
                break;
            case 1:
                enableFilter();
                clearSearch();
                secondView.getBuilder().update(objects);
                setTable(secondView.getBuilder().getView());
                break;
            case 2:
                enableFilter();
                clearSearch();
                thirdView.getBuilder().update(objects);
                setTable(thirdView.getBuilder().getView());
                break;
            case 3:
                enableFilter();
                clearSearch();
                forthView.getBuilder().update(objects);
                setTable(forthView.getBuilder().getView());

                break;
            case 4:
                enableFilter();
                clearSearch();
                fifthView.getBuilder().update(objects);
                setTable(fifthView.getBuilder().getView());
                break;
        }
    }

    @Override
    public void setSearchResults(Object[][] searchResults, String searchText, int position) {
        switch (position) {
            case 0:
                disableFilter();
                firstView.getBuilder().searchResults(searchResults, searchText);
                setTable(firstView.getBuilder().getView());
                break;
            case 1:
                enableFilter();
                secondView.getBuilder().searchResults(searchResults, searchText);
                setTable(secondView.getBuilder().getView());
                break;
            case 2:
                enableFilter();
                thirdView.getBuilder().searchResults(searchResults, searchText);
                setTable(thirdView.getBuilder().getView());
                break;
            case 3:
                enableFilter();
                forthView.getBuilder().searchResults(searchResults, searchText);
                setTable(forthView.getBuilder().getView());
                break;
            case 4:
                enableFilter();
                fifthView.getBuilder().searchResults(searchResults, searchText);
                setTable(fifthView.getBuilder().getView());
                break;
        }
    }

    @Override
    public void showFilterDialog(int[] filterConfig,int currentPage) {
        if(currentPage == 1 || currentPage == 4){
            OutcomeFilterDialog dialog = new OutcomeFilterDialog(getContext(), filterConfig, config -> {
                presenter.filterConfigsChanged(config);
                clearSearch();
            });
            dialog.show();
        }else if (currentPage == 2 || currentPage == 3){
            IncomeFilterDialog dialog = new IncomeFilterDialog(getContext(),filterConfig,config -> {
                presenter.filterConfigsChanged(config);
                clearSearch();
            });
            dialog.show();
        }

    }

}
