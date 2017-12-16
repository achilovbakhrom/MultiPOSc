package com.jim.multipos.ui.consignment_list.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.ui.consignment_list.ConsignmentListActivity;
import com.jim.multipos.ui.consignment_list.adapter.ConsignmentListItemAdapter;
import com.jim.multipos.ui.consignment_list.presenter.ConsignmentListPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.rxevents.MessageWithIdEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.VENDOR_ID;
import static com.jim.multipos.ui.consignment.view.IncomeConsignmentFragment.CONSIGNMENT_UPDATE;
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

    @BindView(R.id.tvActions)
    TextView tvActions;

    @BindView(R.id.ivStatusSort)
    ImageView ivStatusSort;
    @BindView(R.id.ivConsignmentSort)
    ImageView ivConsignmentSort;
    @BindView(R.id.ivDateSort)
    ImageView ivDateSort;
    @BindView(R.id.ivDebtSort)
    ImageView ivDebtSort;

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
                    if (o instanceof MessageWithIdEvent) {
                        MessageWithIdEvent event = (MessageWithIdEvent) o;
                        switch (event.getMessage()) {
                            case CONSIGNMENT_UPDATE: {
                                presenter.initConsignmentListRecyclerViewData(event.getId());
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
        adapter.setCallback(new ConsignmentListItemAdapter.OnConsignmentListItemCallback() {
            @Override
            public void onItemClick(Consignment consignment) {
                presenter.setConsignment(consignment);
            }

            @Override
            public void onItemDelete(Consignment consignment) {
                WarningDialog warningDialog = new WarningDialog(getContext());
                warningDialog.setWarningMessage(getString(R.string.do_you_want_delete));
                warningDialog.setDialogTitle(getString(R.string.warning));
                warningDialog.setOnYesClickListener(view1 -> {
                    presenter.deleteConsignment(consignment);
                    warningDialog.dismiss();
                });
                warningDialog.setOnNoClickListener(view -> warningDialog.dismiss());
                warningDialog.show();
            }
        });
        if (getArguments() != null) {
            Long vendorId = (Long) getArguments().get(VENDOR_ID);
            presenter.initConsignmentListRecyclerViewData(vendorId);
        }

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
    }

    private void deselectAll() {
        ivStatusSort.setVisibility(View.GONE);
        ivConsignmentSort.setVisibility(View.GONE);
        ivDateSort.setVisibility(View.GONE);
        ivDebtSort.setVisibility(View.GONE);
    }

    @Override
    public void setConsignmentListRecyclerViewData(List<Consignment> consignmentList, Currency currency) {
        adapter.setItems(consignmentList, currency);
    }

    @Override
    public void notifyList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initSearchResults(List<Consignment> searchResults, String searchText, Currency currency) {
        adapter.setSearchResult(searchResults, currency, searchText);
    }

    @Override
    public void openConsignment(Long consignmentId, Integer consignmentType) {
        ((ConsignmentListActivity) getActivity()).openConsignment(consignmentId, consignmentType);
    }

    @Override
    public void sendEvent(String event) {
        rxBus.send(new MessageEvent(event));
    }

    public void setSearchText(String searchText) {
        presenter.search(searchText);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.removeListners(subscriptions);
    }
}
