package com.jim.multipos.ui.consignment_list.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.ui.consignment_list.adapter.ConsignmentListItemAdapter;
import com.jim.multipos.ui.consignment_list.dialogs.InvoiceDetailsDialog;
import com.jim.multipos.ui.consignment_list.dialogs.InvoiceListFilterDialog;
import com.jim.multipos.ui.consignment_list.model.InvoiceListItem;
import com.jim.multipos.ui.consignment_list.presenter.ConsignmentListPresenter;
import com.jim.multipos.utils.DateIntervalPicker;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.rxevents.inventory_events.ConsignmentWithVendorEvent;
import com.jim.multipos.utils.rxevents.inventory_events.InventoryStateEvent;
import com.jim.multipos.utils.rxevents.main_order_events.ConsignmentEvent;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.disposables.Disposable;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.VENDOR_ID;
import static com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment.SortingStates.FILTERED_BY_CONSIGNMENT;
import static com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment.SortingStates.FILTERED_BY_CONSIGNMENT_INVERT;
import static com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment.SortingStates.FILTERED_BY_DATE;
import static com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment.SortingStates.FILTERED_BY_DATE_INVERT;
import static com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment.SortingStates.FILTERED_BY_DEBT;
import static com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment.SortingStates.FILTERED_BY_DEBT_INVERT;
import static com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment.SortingStates.FILTERED_BY_STATUS;

/**
 * Created by Sirojiddin on 30.11.2017.
 */

public class ConsignmentListFragment extends BaseFragment implements ConsignmentListView {

    @Inject
    ConsignmentListPresenter presenter;
    @Inject
    RxBus rxBus;
    @Inject
    DatabaseManager databaseManager;

    @BindView(R.id.rvConsignmentList)
    RecyclerView rvConsignmentList;

    @BindView(R.id.llStatus)
    LinearLayout llStatus;
    @BindView(R.id.llConsignment)
    LinearLayout llConsignment;
    @BindView(R.id.llDate)
    LinearLayout llDate;
    @BindView(R.id.llTotalDebt)
    LinearLayout llTotalDebt;
    @BindView(R.id.llDateInterval)
    LinearLayout llDateInterval;
    @BindView(R.id.llFilter)
    LinearLayout llFilter;
    @BindView(R.id.flClearSearch)
    FrameLayout flClearSearch;

    @BindView(R.id.tvActions)
    TextView tvActions;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDateInterval)
    TextView tvDateInterval;
    @BindView(R.id.ivStatusSort)
    ImageView ivStatusSort;
    @BindView(R.id.ivConsignmentSort)
    ImageView ivConsignmentSort;
    @BindView(R.id.ivDateSort)
    ImageView ivDateSort;
    @BindView(R.id.ivDebtSort)
    ImageView ivDebtSort;
    @BindView(R.id.ivSearchImage)
    ImageView ivSearchImage;
    @BindView(R.id.mpSearchEditText)
    MpEditText mpSearchEditText;

    ConsignmentListItemAdapter adapter;
    SortingStates filterMode = SortingStates.FILTERED_BY_DATE;
    private ArrayList<Disposable> subscriptions;

    public enum SortingStates {
        FILTERED_BY_STATUS, FILTERED_BY_STATUS_INVERT, FILTERED_BY_CONSIGNMENT, FILTERED_BY_CONSIGNMENT_INVERT, FILTERED_BY_DATE, FILTERED_BY_DATE_INVERT, FILTERED_BY_DEBT, FILTERED_BY_DEBT_INVERT
    }

    @Override
    protected int getLayout() {
        return R.layout.consignment_list_fragment;
    }

    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof ConsignmentWithVendorEvent) {
                        ConsignmentWithVendorEvent event = (ConsignmentWithVendorEvent) o;
                        switch (event.getType()) {
                            case GlobalEventConstants.UPDATE: {
                                presenter.initConsignmentListRecyclerViewData(event.getVendor().getId());
                                break;
                            }
                        }
                    }
                }));
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        rvConsignmentList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ConsignmentListItemAdapter(getContext());
        rvConsignmentList.setAdapter(adapter);
        if (getArguments() != null) {
            Long vendorId = (Long) getArguments().get(VENDOR_ID);
            presenter.initConsignmentListRecyclerViewData(vendorId);
        }

        mpSearchEditText.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.search(mpSearchEditText.getText().toString());
            }
        });

        llDateInterval.setOnClickListener(v -> presenter.openDateIntervalPicker());

        llStatus.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != FILTERED_BY_STATUS) {
                filterMode = FILTERED_BY_STATUS;
                presenter.filterBy(FILTERED_BY_STATUS);
                ivStatusSort.setVisibility(View.VISIBLE);
                ivStatusSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SortingStates.FILTERED_BY_STATUS_INVERT;
                ivStatusSort.setVisibility(View.VISIBLE);
                ivStatusSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llConsignment.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != FILTERED_BY_CONSIGNMENT) {
                filterMode = FILTERED_BY_CONSIGNMENT;
                presenter.filterBy(FILTERED_BY_CONSIGNMENT);
                ivConsignmentSort.setVisibility(View.VISIBLE);
                ivConsignmentSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = FILTERED_BY_CONSIGNMENT_INVERT;
                ivConsignmentSort.setVisibility(View.VISIBLE);
                ivConsignmentSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llDate.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != FILTERED_BY_DATE) {
                filterMode = FILTERED_BY_DATE;
                presenter.filterBy(FILTERED_BY_DATE);
                ivDateSort.setVisibility(View.VISIBLE);
                ivDateSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = FILTERED_BY_DATE_INVERT;
                ivDateSort.setVisibility(View.VISIBLE);
                ivDateSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }

        });
        llTotalDebt.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != FILTERED_BY_DEBT) {
                filterMode = FILTERED_BY_DEBT;
                presenter.filterBy(FILTERED_BY_DEBT);
                ivDebtSort.setVisibility(View.VISIBLE);
                ivDebtSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = FILTERED_BY_DEBT_INVERT;
                ivDebtSort.setVisibility(View.VISIBLE);
                ivDebtSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });

        flClearSearch.setOnClickListener(v -> {
            if(!mpSearchEditText.getText().toString().isEmpty())
                mpSearchEditText.setText("");
        });

        adapter.setCallback(item -> {
            InvoiceDetailsDialog dialog = new InvoiceDetailsDialog(getContext(), databaseManager, item.getInvoice(), item.getOutvoice());
            dialog.show();
        });

    }

    private void deselectAll() {
        ivStatusSort.setVisibility(View.GONE);
        ivConsignmentSort.setVisibility(View.GONE);
        ivDateSort.setVisibility(View.GONE);
        ivDebtSort.setVisibility(View.GONE);
    }

    @Override
    public void seInvoiceListRecyclerViewData(List<InvoiceListItem> invoiceListItems, Currency currency) {
        adapter.setItems(invoiceListItems, currency);
    }

    @Override
    public void notifyList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initSearchResults(List<InvoiceListItem> searchResults, String searchText, Currency currency) {
        adapter.setSearchResult(searchResults, currency, searchText);
    }

    @Override
    public void openConsignment(Long consignmentId, Integer consignmentType) {
//        ((ConsignmentListActivity) getActivity()).openConsignment(consignmentId, consignmentType);
    }

    @Override
    public void sendConsignmentEvent(int event) {
        rxBus.send(new ConsignmentEvent(event));
    }

    @Override
    public void dateIntervalPicked(Calendar fromDate, Calendar toDate) {
        presenter.dateIntervalPicked(fromDate, toDate);
    }

    @Override
    public void datePicked(Calendar pickedDate) {
        presenter.datePicked(pickedDate);
    }

    @Override
    public void clearInterval() {
        presenter.clearIntervals();
    }

    @Override
    public void sendInventoryStateEvent(int event) {
        rxBus.send(new InventoryStateEvent(event));
    }

    @Override
    public void openDateIntervalPicker(Calendar fromDate, Calendar toDate) {
        DateIntervalPicker dateIntervalPicker = new DateIntervalPicker(getContext(), fromDate, toDate, (fromDate1, toDate1) -> {
            presenter.dateIntervalPicked(fromDate1, toDate1);
        });
        dateIntervalPicker.show();
    }

    @Override
    public void updateDateIntervalUI(String date) {
        tvDateInterval.setText(date);
    }

    @Override
    public void setVendorName(String name) {
        tvTitle.setText(name +  "'s purchase and return invoices list");
    }

    @Override
    public void openFilterDialog(int[] filterConfig) {
        InvoiceListFilterDialog dialog = new InvoiceListFilterDialog(getContext(), filterConfig, config -> presenter.onFilterConfigChanged(config));
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.removeListners(subscriptions);
    }

    @OnTextChanged(R.id.mpSearchEditText)
    protected void handleTextChange(Editable editable) {
        if(editable.toString().isEmpty()){
            ivSearchImage.setImageResource(R.drawable.search_app);
        }else {
            ivSearchImage.setImageResource(R.drawable.cancel_search);
        }
    }

    @OnClick(R.id.llFilter)
    public void onFilterClicked(){
        presenter.onFilterClicked();
    }
}
