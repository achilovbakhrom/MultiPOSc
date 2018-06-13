package com.jim.multipos.ui.reports.summary_report;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.reports.summary_report.adapter.PairAdapter;
import com.jim.multipos.ui.reports.summary_report.adapter.PairString;
import com.jim.multipos.ui.reports.summary_report.adapter.TripleAdapter;
import com.jim.multipos.ui.reports.summary_report.adapter.TripleString;
import com.jim.multipos.utils.DateIntervalPicker;
import com.jim.multipos.utils.ExportDialog;
import com.jim.multipos.utils.ExportToDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.jim.multipos.utils.ExportUtils.EXCEL;

public class SummaryReportFragment extends BaseFragment implements SummaryReportView {
    @Inject
    SummaryReportPresenter presenter;
    PairAdapter pairSummaryAdapter;
    PairAdapter pairAnalitcsAdapter;
    TripleAdapter triplePaymentsAdapter;

    @Override
    protected int getLayout() {
        return R.layout.summary_report_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setSingleTitle(getString(R.string.summary_report));
        disableFilter();
        disableSearch();
        initTabLabs();
        presenter.onCreateView(savedInstanceState);
    }


    @Override
    public void updateRecyclerViewSummary(List<PairString> pairStringList) {
        if (pairSummaryAdapter == null) {
            pairSummaryAdapter = new PairAdapter(pairStringList);
            rvSummary.setLayoutManager(new LinearLayoutManager(getContext()));
            rvSummary.setAdapter(pairSummaryAdapter);
        } else {
            rvSummary.setAdapter(pairSummaryAdapter);
            pairSummaryAdapter.update(pairStringList);
        }
    }

    @Override
    public void updateRecyclerViewPayments(List<TripleString> tripleStrings) {
        if (triplePaymentsAdapter == null) {
            triplePaymentsAdapter = new TripleAdapter(tripleStrings);
            rvSummary.setLayoutManager(new LinearLayoutManager(getContext()));
            rvSummary.setAdapter(triplePaymentsAdapter);
        } else {
            rvSummary.setAdapter(triplePaymentsAdapter);
            triplePaymentsAdapter.update(tripleStrings);
        }
    }

    @Override
    public void updatecyclerViewAnalitcs(List<PairString> pairStringList) {
        if (pairAnalitcsAdapter == null) {
            pairAnalitcsAdapter = new PairAdapter(pairStringList);
            rvAnalitcs.setLayoutManager(new LinearLayoutManager(getContext()));
            rvAnalitcs.setAdapter(pairAnalitcsAdapter);
        } else {
            rvAnalitcs.setAdapter(pairAnalitcsAdapter);
            pairAnalitcsAdapter.update(pairStringList);
        }
    }

    ExportToDialog exportDialog;

    @Override
    public void openExportDialog(int mode) {
        exportDialog = new ExportToDialog(getContext(), mode, getString(R.string.summary_report), new ExportToDialog.OnExportListener() {
            @Override
            public void onFilePickerClicked() {
                openFilePickerDialog();
            }

            @Override
            public void onSaveToUSBClicked(String filename, UsbFile root) {
                if (mode == EXCEL)
                    presenter.exportExcelToUSB(filename, root);
                else presenter.exportPdfToUSB(filename, root);
            }

            @Override
            public void onSaveClicked(String fileName, String path) {
                if (mode == EXCEL)
                    presenter.exportExcel(fileName, path);
                else presenter.exportPdf(fileName, path);
            }
        });
        exportDialog.show();
    }

    private void openFilePickerDialog() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.DIR_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        FilePickerDialog dialog = new FilePickerDialog(getContext(), properties);
        dialog.setNegativeBtnName(getContext().getString(R.string.cancel));
        dialog.setPositiveBtnName(getContext().getString(R.string.select));
        dialog.setTitle(getContext().getString(R.string.select_a_directory));
        dialog.setDialogSelectionListener(files -> {
            exportDialog.setPath(files);
        });
        dialog.show();
    }


    @BindView(R.id.rvSummary)
    RecyclerView rvSummary;
    @BindView(R.id.rvAnalitcs)
    RecyclerView rvAnalitcs;

    @BindView(R.id.llHeaderSalesSummary)
    LinearLayout llHeaderSalesSummary;
    @BindView(R.id.llHeaderPayments)
    LinearLayout llHeaderPayments;

    @BindView(R.id.llSalesSummary)
    LinearLayout llSalesSummary;
    @BindView(R.id.tvSalesSummary)
    TextView tvSalesSummary;
    @BindView(R.id.flSalesSummaryBottomLine)
    FrameLayout flSalesSummaryBottomLine;

    @BindView(R.id.llPaymentSummary)
    LinearLayout llPaymentSummary;
    @BindView(R.id.tvPaymentSummary)
    TextView tvPaymentSummary;
    @BindView(R.id.flPaymentSummaryButtonLine)
    FrameLayout flPaymentSummaryButtonLine;

    @BindView(R.id.llAnalitcs)
    LinearLayout llAnalitcs;
    @BindView(R.id.tvAnalitcs)
    TextView tvAnalitcs;
    @BindView(R.id.flAnalitcs)
    FrameLayout flAnalitcs;

    @BindView(R.id.tvDateInterval)
    TextView tvDateInterval;
    @BindView(R.id.llDateInterval)
    LinearLayout llDateInterval;
    @BindView(R.id.ivSearchImage)
    ImageView ivSearchImage;
    @BindView(R.id.mpSearchEditText)
    MpEditText mpSearchEditText;


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


    int panelCount = 0;
    private SimpleDateFormat simpleDateFormat;

    public SummaryReportFragment() {
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    public void disableSearch() {
        llSearch.setVisibility(View.GONE);
    }

    public void disableFilter() {
        llFilter.setVisibility(View.GONE);
    }

    public void enableFilter() {
        llFilter.setVisibility(View.VISIBLE);
    }

    public void disableDateIntervalPicker() {
        llDateInterval.setVisibility(View.GONE);
        tvDateInterval.setVisibility(View.INVISIBLE);
    }

    public void disableExport() {
        llExpert.setVisibility(View.GONE);
    }

    public void enableDateIntervalPicker() {
        llDateInterval.setVisibility(View.VISIBLE);
        tvDateInterval.setVisibility(View.VISIBLE);
    }

    public void updateDateIntervalUi(Calendar fromDate, Calendar toDate) {
        tvDateInterval.setText(simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime()));
    }

    public void openDateInterval(Calendar fromDate, Calendar toDate) {
        DateIntervalPicker dateIntervalPicker = new DateIntervalPicker(getContext(), fromDate, toDate, (fromDate1, toDate1) -> presenter.onChooseDateInterval(fromDate1, toDate1));
        dateIntervalPicker.show();
    }

    public void setSingleTitle(String titleReport) {
        llChoiserPanel.setVisibility(View.GONE);
        tvTitleReport.setVisibility(View.VISIBLE);
        tvTitleReport.setText(titleReport);
        panelCount = 1;
    }

    public void setChoiserPanel(String[] titles) {
        tvTitleReport.setVisibility(View.GONE);
        llChoiserPanel.setVisibility(View.VISIBLE);
        panelCount = titles.length;
        if (panelCount > 5) {
            new Exception("Panel title can maximum 5, for all questions ISLOMOV SARDOR").printStackTrace();
        }
        if (panelCount == 1) {
            setSingleTitle(titles[0]);
        } else if (panelCount == 2) {
            tvFirtPanel.setVisibility(View.VISIBLE);
            tvSecondPanel.setVisibility(View.GONE);
            tvThirdPanel.setVisibility(View.GONE);
            tvFourPanel.setVisibility(View.GONE);
            tvFivePanel.setVisibility(View.VISIBLE);

            tvFirtPanel.setText(titles[0]);
            tvFivePanel.setText(titles[1]);

            tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title_pressed);
            tvFirtPanel.setTextColor(Color.parseColor("#2e91cc"));

            tvFirtPanel.setOnClickListener((view) -> {
                disableAllPanelButtons();
                presenter.onChoisedPanel(0);
                tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title_pressed);
                tvFirtPanel.setTextColor(Color.parseColor("#2e91cc"));
            });
            tvFivePanel.setOnClickListener((view) -> {
                disableAllPanelButtons();
                presenter.onChoisedPanel(1);
                tvFivePanel.setBackgroundResource(R.drawable.right_switch_title_pressed);
                tvFivePanel.setTextColor(Color.parseColor("#2e91cc"));
            });
        } else if (panelCount == 3) {
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
                presenter.onChoisedPanel(0);
                tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title_pressed);
                tvFirtPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvSecondPanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                presenter.onChoisedPanel(1);
                tvSecondPanel.setBackgroundResource(R.drawable.center_switch_title_pressed);
                tvSecondPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvFivePanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                presenter.onChoisedPanel(2);
                tvFivePanel.setBackgroundResource(R.drawable.right_switch_title_pressed);
                tvFivePanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
        } else if (panelCount == 4) {
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
                presenter.onChoisedPanel(0);
                tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title_pressed);
                tvFirtPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvSecondPanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                presenter.onChoisedPanel(1);
                tvSecondPanel.setBackgroundResource(R.drawable.center_switch_title_pressed);
                tvSecondPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvThirdPanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                presenter.onChoisedPanel(2);
                tvThirdPanel.setBackgroundResource(R.drawable.center_switch_title_pressed);
                tvThirdPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvFivePanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                presenter.onChoisedPanel(3);
                tvFivePanel.setBackgroundResource(R.drawable.right_switch_title_pressed);
                tvFivePanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
        } else if (panelCount == 5) {
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
                disableAllPanelButtons();
                presenter.onChoisedPanel(0);
                tvFirtPanel.setBackgroundResource(R.drawable.left_switch_title_pressed);
                tvFirtPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvSecondPanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                presenter.onChoisedPanel(1);
                tvSecondPanel.setBackgroundResource(R.drawable.center_switch_title_pressed);
                tvSecondPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvThirdPanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                presenter.onChoisedPanel(2);
                tvThirdPanel.setBackgroundResource(R.drawable.center_switch_title_pressed);
                tvThirdPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvFourPanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                presenter.onChoisedPanel(3);
                tvFourPanel.setBackgroundResource(R.drawable.center_switch_title_pressed);
                tvFourPanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
            tvFivePanel.setOnClickListener((view -> {
                disableAllPanelButtons();
                presenter.onChoisedPanel(4);
                tvFivePanel.setBackgroundResource(R.drawable.right_switch_title_pressed);
                tvFivePanel.setTextColor(Color.parseColor("#2e91cc"));
            }));
        }

    }

    public void disableAllPanelButtons() {
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
    public void onClickedFilterButton() {
        Runnable runnable = () -> presenter.onClickedFilter();
        runnable.run();
    }

    @OnTextChanged(R.id.mpSearchEditText)
    protected void handleTextChange(Editable editable) {
        if (isAllright) {
            Runnable runnable = () -> presenter.onSearchTyped(editable.toString());
            runnable.run();
        } else isAllright = true;
        if (editable.toString().isEmpty()) {
            ivSearchImage.setImageResource(R.drawable.search_app);
        } else {
            ivSearchImage.setImageResource(R.drawable.cancel_search);
        }
    }

    @OnClick(R.id.llDateInterval)
    public void onClickedDateIntervalButton() {
        llDateInterval.setEnabled(false);
        clearSearch();
        Runnable runnable = () -> presenter.onClickedDateInterval();
        llDateInterval.postDelayed(runnable, 30);
        llDateInterval.setEnabled(true);
    }

    @OnClick(R.id.flCleareSearch)
    public void onCleareSearch() {
        if (!mpSearchEditText.getText().toString().isEmpty())
            mpSearchEditText.setText("");
    }

    @OnClick(R.id.llExpert)
    public void showExportPanel() {
        ExportDialog exportDialog = new ExportDialog(getContext(), panelCount, new ExportDialog.OnExportItemClick() {
            @Override
            public void onToExcel() {
                presenter.onClickedExportExcel();
            }

            @Override
            public void onToPdf() {
                presenter.onClickedExportPDF();
            }
        });
        exportDialog.show();
    }

    boolean isAllright = true;

    public void clearSearch() {
        isAllright = false;
        mpSearchEditText.setText("");
    }

    ;

    private void initTabLabs() {
        llSalesSummary.setOnClickListener(view -> {
            presenter.onSalesSummary();
        });
        llPaymentSummary.setOnClickListener(view -> {
            presenter.onPaymentsSummary();
        });
    }

    @Override
    public void activeSalesSummary() {
        tvSalesSummary.setTextColor(Color.parseColor("#2e91cc"));
        flSalesSummaryBottomLine.setVisibility(View.VISIBLE);
        tvPaymentSummary.setTextColor(Color.parseColor("#a9a9a9"));
        flPaymentSummaryButtonLine.setVisibility(View.INVISIBLE);
        tvAnalitcs.setText(getContext().getString(R.string.sales_analitcs));
        llHeaderSalesSummary.setVisibility(View.VISIBLE);
        llHeaderPayments.setVisibility(View.GONE);
    }

    @Override
    public void activePaymentsSummary() {
        tvSalesSummary.setTextColor(Color.parseColor("#a9a9a9"));
        flSalesSummaryBottomLine.setVisibility(View.INVISIBLE);
        tvPaymentSummary.setTextColor(Color.parseColor("#2e91cc"));
        flPaymentSummaryButtonLine.setVisibility(View.VISIBLE);
        tvAnalitcs.setText(getContext().getString(R.string.payment_analytics));
        llHeaderSalesSummary.setVisibility(View.GONE);
        llHeaderPayments.setVisibility(View.VISIBLE);

    }


}
