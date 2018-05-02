package com.jim.multipos.core;

import android.graphics.Color;
import android.text.Editable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.utils.DateIntervalPicker;
import com.jim.multipos.utils.ExportDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public abstract class BaseTableReportFragment extends BaseFragment {



    @BindView(R.id.tvDateInterval)
    TextView tvDateInterval;
    @BindView(R.id.llDateInterval)
    LinearLayout llDateInterval;
    @BindView(R.id.ivSearchImage)
    ImageView ivSearchImage;
    @BindView(R.id.mpSearchEditText)
    MpEditText mpSearchEditText;
    @BindView(R.id.llChooseTill)
    LinearLayout llChooseTill;

    @BindView(R.id.tvFirtPanel)
    TextView tvFirtPanel;
    @BindView(R.id.tvSecondPanel)
    TextView tvSecondPanel;
    @BindView(R.id.tvThirdPanel)
    TextView tvThirdPanel;
    @BindView(R.id.tvFourPanel)
    TextView tvFourPanel;
    @BindView(R.id.tvFivePanel)
    TextView tvFivePanel;
    @BindView(R.id.llChoiserPanel)
    LinearLayout llChoiserPanel;
    @BindView(R.id.llFilter)
    LinearLayout llFilter;
    @BindView(R.id.llExpert)
    LinearLayout llExpert;
    @BindView(R.id.tvTitleReport)
    TextView tvTitleReport;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    @BindView(R.id.flTable)
    FrameLayout flTable;
    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;

    int panelCount=0;
    private BaseTableReportPresenter baseTableReportPresenter;
    private SimpleDateFormat simpleDateFormat;

    protected BaseTableReportFragment() {
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    @Override
    protected int getLayout() {
        return R.layout.report_fragment;
    }

    protected void init(BaseTableReportPresenter baseTableReportPresenter) {
        this.baseTableReportPresenter = baseTableReportPresenter;


    }
    public void setTable(FrameLayout frameLayout){
        flTable.removeAllViews();
        flTable.addView(frameLayout);
        pbLoading.setVisibility(View.GONE);
    }
    public ProgressBar getPbLoading(){return pbLoading;}
    public void disableSearch(){
        llSearch.setVisibility(View.GONE);
    }
    public void enableSearch(){
        llSearch.setVisibility(View.VISIBLE);
    }
    public void disableFilter(){
        llFilter.setVisibility(View.GONE);
    }
    public void enableFilter(){
        llFilter.setVisibility(View.VISIBLE);
    }
    public void enableTillChooseBtn(){llChooseTill.setVisibility(View.VISIBLE);}
    public void disableTillChooseBtn(){llChooseTill.setVisibility(View.GONE);}
    public void disableDateIntervalPicker(){
        llDateInterval.setVisibility(View.GONE);
        tvDateInterval.setVisibility(View.INVISIBLE);
    }
    public void disableExport(){
        llExpert.setVisibility(View.GONE);
    }
    public void enableDateIntervalPicker(){
        llDateInterval.setVisibility(View.VISIBLE);
        tvDateInterval.setVisibility(View.VISIBLE);
    }

    public void updateDateIntervalUi(Calendar fromDate,Calendar toDate){
        tvDateInterval.setText(simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime()));
    }
    public void openDateInterval(Calendar fromDate,Calendar toDate){
        DateIntervalPicker dateIntervalPicker = new DateIntervalPicker(getContext(), fromDate, toDate, (fromDate1, toDate1) -> {
            pbLoading.setVisibility(View.VISIBLE);
            pbLoading.postDelayed(() -> baseTableReportPresenter.onChooseDateInterval(fromDate1, toDate1), 50);
        });
        dateIntervalPicker.show();
    }
    public void setSingleTitle(String titleReport){
        llChoiserPanel.setVisibility(View.GONE);
        tvTitleReport.setVisibility(View.VISIBLE);
        tvTitleReport.setText(titleReport);
        panelCount = 1;
    }
    public void setChoiserPanel(String[] titles){

        tvTitleReport.setVisibility(View.GONE);
        llChoiserPanel.setVisibility(View.VISIBLE);
        panelCount = titles.length;
        if(panelCount>5) {
            new Exception("Panel title can maximum 5, for all questions ISLOMOV SARDOR").printStackTrace();
        }
        if(panelCount == 1){

            setSingleTitle(titles[0]);
        }else if(panelCount == 2){
            tvFirtPanel.setVisibility(View.VISIBLE);
            tvSecondPanel.setVisibility(View.GONE);
            tvThirdPanel.setVisibility(View.GONE);
            tvFourPanel.setVisibility(View.GONE);
            tvFivePanel.setVisibility(View.VISIBLE);

            tvFirtPanel.setText(titles[0]);
            tvFivePanel.setText(titles[1]);

            tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title_pressed);
            tvFirtPanel.setTextColor(Color.parseColor("#2e91cc"));

            tvFirtPanel.setOnClickListener((view)->{
                disableAllPanelButtons();
                pbLoading.setVisibility(View.VISIBLE);
                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(0),50);

                tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title_pressed);
                tvFirtPanel.setTextColor(Color.parseColor("#2e91cc"));
            });
            tvFivePanel.setOnClickListener((view)->{
                disableAllPanelButtons();
                pbLoading.setVisibility(View.VISIBLE);

                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(1),50);

                tvFivePanel.setBackgroundResource(R.drawable.right_switch_title_pressed);
                tvFivePanel.setTextColor(Color.parseColor("#2e91cc"));
            });
        }else if(panelCount == 3){
            tvFirtPanel.setVisibility(View.VISIBLE);
            tvSecondPanel.setVisibility(View.VISIBLE);
            tvThirdPanel.setVisibility(View.GONE);
            tvFourPanel.setVisibility(View.GONE);
            tvFivePanel.setVisibility(View.VISIBLE);

            tvFirtPanel.setText(titles[0]);
            tvSecondPanel.setText(titles[1]);
            tvFivePanel.setText(titles[2]);

            tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title_pressed);
            tvFirtPanel.setTextColor(Color.parseColor("#2e91cc"));
            tvFirtPanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                pbLoading.setVisibility(View.VISIBLE);
                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(0),50);
                tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title_pressed);
                tvFirtPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvSecondPanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                pbLoading.setVisibility(View.VISIBLE);
                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(1),50);
                tvSecondPanel.setBackgroundResource(R.drawable.center_switch_title_pressed);
                tvSecondPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvFivePanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                pbLoading.setVisibility(View.VISIBLE);

                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(2),50);

                tvFivePanel.setBackgroundResource(R.drawable.right_switch_title_pressed);
                tvFivePanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
        }else if(panelCount == 4){
            tvFirtPanel.setVisibility(View.VISIBLE);
            tvSecondPanel.setVisibility(View.VISIBLE);
            tvThirdPanel.setVisibility(View.VISIBLE);
            tvFourPanel.setVisibility(View.GONE);
            tvFivePanel.setVisibility(View.VISIBLE);

            tvFirtPanel.setText(titles[0]);
            tvSecondPanel.setText(titles[1]);
            tvThirdPanel.setText(titles[2]);
            tvFivePanel.setText(titles[3]);

            tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title_pressed);
            tvFirtPanel.setTextColor(Color.parseColor("#2e91cc"));
            tvFirtPanel.setOnClickListener((view -> {
                disableAllPanelButtons();

                pbLoading.setVisibility(View.VISIBLE);

                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(0),50);

                tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title_pressed);
                tvFirtPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvSecondPanel.setOnClickListener((view -> {
                pbLoading.setVisibility(View.VISIBLE);
                disableAllPanelButtons();
                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(1),50);

                tvSecondPanel.setBackgroundResource(R.drawable.center_switch_title_pressed);
                tvSecondPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvThirdPanel.setOnClickListener((view -> {
                pbLoading.setVisibility(View.VISIBLE);
                disableAllPanelButtons();
                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(2),50);

                tvThirdPanel.setBackgroundResource(R.drawable.center_switch_title_pressed);
                tvThirdPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvFivePanel.setOnClickListener((view -> {
                pbLoading.setVisibility(View.VISIBLE);
                disableAllPanelButtons();
                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(3),50);

                tvFivePanel.setBackgroundResource(R.drawable.right_switch_title_pressed);
                tvFivePanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
        }else if(panelCount == 5){
            tvFirtPanel.setVisibility(View.VISIBLE);
            tvSecondPanel.setVisibility(View.VISIBLE);
            tvThirdPanel.setVisibility(View.VISIBLE);
            tvFourPanel.setVisibility(View.VISIBLE);
            tvFivePanel.setVisibility(View.VISIBLE);

            tvFirtPanel.setText(titles[0]);
            tvSecondPanel.setText(titles[1]);
            tvThirdPanel.setText(titles[2]);
            tvFivePanel.setText(titles[3]);
            tvFivePanel.setText(titles[4]);

            tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title_pressed);
            tvFirtPanel.setTextColor(Color.parseColor("#2e91cc"));
            tvFirtPanel.setOnClickListener((view -> {
                pbLoading.setVisibility(View.VISIBLE);
                disableAllPanelButtons();
                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(0),50);

                tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title_pressed);
                tvFirtPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvSecondPanel.setOnClickListener((view -> {
                pbLoading.setVisibility(View.VISIBLE);
                disableAllPanelButtons();
                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(1),50);

                tvSecondPanel.setBackgroundResource(R.drawable.center_switch_title_pressed);
                tvSecondPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvThirdPanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                pbLoading.setVisibility(View.VISIBLE);
                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(2),50);

                tvThirdPanel.setBackgroundResource(R.drawable.center_switch_title_pressed);
                tvThirdPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvFourPanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                pbLoading.setVisibility(View.VISIBLE);
                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(3),50);

                tvFourPanel.setBackgroundResource(R.drawable.center_switch_title_pressed);
                tvFourPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvFivePanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                pbLoading.setVisibility(View.VISIBLE);
                pbLoading.postDelayed(()-> baseTableReportPresenter.onChoisedPanel(4),50);

                tvFivePanel.setBackgroundResource(R.drawable.right_switch_title_pressed);
                tvFivePanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
        }

    }
    public void disableAllPanelButtons(){
        tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title);
        tvSecondPanel.setBackgroundResource(R.drawable.center_switch_title);
        tvThirdPanel.setBackgroundResource(R.drawable.center_switch_title);
        tvFourPanel.setBackgroundResource(R.drawable.center_switch_title);
        tvFivePanel.setBackgroundResource(R.drawable.right_switch_title);
        tvFirtPanel.setTextColor(Color.parseColor("#999999"));
        tvSecondPanel.setTextColor(Color.parseColor("#999999"));
        tvThirdPanel.setTextColor(Color.parseColor("#999999"));
        tvFourPanel.setTextColor(Color.parseColor("#999999"));
        tvFivePanel.setTextColor(Color.parseColor("#999999"));
    }
    @OnClick(R.id.llFilter)
    public void onClickedFilterButton(){
        Runnable runnable = () -> baseTableReportPresenter.onClickedFilter();
        runnable.run();
    }
    @OnClick(R.id.llChooseTill)
    public void onTillChooseButton(){
        Runnable runnable = () -> baseTableReportPresenter.onTillPickerClicked();
        runnable.run();
    }
    @OnTextChanged(R.id.mpSearchEditText)
    protected void handleTextChange(Editable editable) {
        if(isAllright) {
            Runnable runnable = () -> baseTableReportPresenter.onSearchTyped(editable.toString());
            runnable.run();
        }else isAllright = true;
        if(editable.toString().isEmpty()){
            ivSearchImage.setImageResource(R.drawable.search_app);
        }else {
            ivSearchImage.setImageResource(R.drawable.cancel_search);
        }
    }
    @OnClick(R.id.llDateInterval)
    public void onClickedDateIntervalButton(){
        llDateInterval.setEnabled(false);
        clearSearch();
        Runnable runnable = () -> baseTableReportPresenter.onClickedDateInterval();
        llDateInterval.postDelayed(runnable,30);
        llDateInterval.setEnabled(true);
    }
    @OnClick(R.id.flCleareSearch)
    public void onCleareSearch(){
        if(!mpSearchEditText.getText().toString().isEmpty())
            mpSearchEditText.setText("");
    }
    @OnClick(R.id.llExpert)
    public void showExportPanel(){
        ExportDialog exportDialog = new ExportDialog(getContext(), panelCount, new ExportDialog.OnExportItemClick() {
            @Override
            public void onToExcel() {
                baseTableReportPresenter.onClickedExportExcel();
            }

            @Override
            public void onToPdf() {
                baseTableReportPresenter.onClickedExportPDF();
            }
        });
        exportDialog.show();
    }
    boolean isAllright = true;
    public void clearSearch(){
        isAllright = false;
        mpSearchEditText.setText("");
    };
    public void setTextToSearch(String searchText) {
        mpSearchEditText.setText(searchText);
    }

}
