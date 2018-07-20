package com.jim.multipos.core;

import java.util.Calendar;

public interface BaseTableReportPresenter extends Presenter {
    void onChooseDateInterval(Calendar fromDate, Calendar toDate);

    void onSearchTyped(String searchText);

    void onClickedDateInterval();

    void onClickedExportExcel();

    void onClickedExportPDF();

    void onClickedFilter();

    void onChoisedPanel(int postion);

    void onTillPickerClicked();
}
