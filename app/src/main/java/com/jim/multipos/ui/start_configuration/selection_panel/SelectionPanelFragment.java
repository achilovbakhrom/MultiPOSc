package com.jim.multipos.ui.start_configuration.selection_panel;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.jim.mpviews.MpCompletedStateView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.utils.CompletionMode;
import com.jim.multipos.utils.FirstConfigureListItem;
import com.jim.multipos.ui.start_configuration.StartConfigurationActivity;
import com.jim.multipos.ui.start_configuration.connection.StartConfigurationConnection;
import com.jim.multipos.ui.start_configuration.selection_panel.adapter.StartConfigurationAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jim.mpviews.MpCompletedStateView.COMPLETED_STATE;
import static com.jim.mpviews.MpCompletedStateView.WARNING_STATE;
import static com.jim.multipos.ui.start_configuration.selection_panel.SelectionPanelPresenterImpl.ACCOUNT_KEY;
import static com.jim.multipos.ui.start_configuration.selection_panel.SelectionPanelPresenterImpl.CURRENCY_KEY;
import static com.jim.multipos.ui.start_configuration.selection_panel.SelectionPanelPresenterImpl.PAYMENT_TYPE_KEY;
import static com.jim.multipos.ui.start_configuration.selection_panel.SelectionPanelPresenterImpl.POS_DATA_KEY;

public class SelectionPanelFragment extends BaseFragment implements SelectionPanelView {

    @BindView(R.id.rvPanel)
    RecyclerView rvPanel;
    @Inject
    SelectionPanelPresenter presenter;
    @Inject
    StartConfigurationConnection connection;
    private StartConfigurationAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.start_configuration_fragment;
    }

    private int lastPosition = 0;

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setSelectionPanelView(this);
        String[] title = getResources().getStringArray(R.array.first_configure_items);
        String[] subTitle = getResources().getStringArray(R.array.first_configure_items_description);
        List<FirstConfigureListItem> panelData = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            FirstConfigureListItem item = new FirstConfigureListItem();
            item.setName(title[i]);
            item.setDescription(subTitle[i]);
            item.setSelected(i == 0);
            item.setState(MpCompletedStateView.EMPTY_STATE);
            panelData.add(item);
        }
        adapter = new StartConfigurationAdapter(getContext(), panelData, position -> {
            checkCompletionByPosition(lastPosition);
            checkCompletionByPosition(position);
            lastPosition = position;
            presenter.checkCompletionMode(position);
        });
        ((SimpleItemAnimator) rvPanel.getItemAnimator()).setSupportsChangeAnimations(false);

        rvPanel.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPanel.setAdapter(adapter);
    }

    private void checkCompletionByPosition(int position) {
        switch (position) {
            case 0:
                connection.checkPosDataCompletion();
                break;
            case 1:
                connection.checkCurrencyCompletion();
                break;
            case 2:
                connection.checkAccountCompletion();
                break;
            case 3:
                connection.checkPaymentTypeCompletion();
                break;
        }
    }

    @Override
    public void showNextFragment(int position) {
        ((StartConfigurationActivity) getActivity()).onPanelClicked(position);
    }

    @Override
    public void sendMode(CompletionMode mode, int position) {
        switch (position) {
            case 0:
                connection.sendModeToPosData(mode);
                break;
            case 1:
                connection.sendModeToCurrency(mode);
                break;
            case 2:
                connection.sendModeToAccount(mode);
                break;
            case 3:
                connection.sendModeToPaymentType(mode);
                break;
        }
    }

    @Override
    public void setPosDataCompletion(boolean state) {
        presenter.putCompletion(POS_DATA_KEY, state);
        if (state) {
            adapter.changeState(0, COMPLETED_STATE);
        } else
            adapter.changeState(0, WARNING_STATE);
    }

    @Override
    public void setCurrencyCompletion(boolean state) {
        presenter.putCompletion(CURRENCY_KEY, state);
        if (state) {
            adapter.changeState(1, COMPLETED_STATE);
        } else
            adapter.changeState(1, WARNING_STATE);
    }

    @Override
    public void setAccountCompletion(boolean state) {
        presenter.putCompletion(ACCOUNT_KEY, state);
        if (state) {
            adapter.changeState(2, COMPLETED_STATE);
        } else
            adapter.changeState(2, WARNING_STATE);
    }

    @Override
    public void setPaymentTypeCompletion(boolean state) {
        presenter.putCompletion(PAYMENT_TYPE_KEY, state);
        if (state) {
            adapter.changeState(3, COMPLETED_STATE);
        } else
            adapter.changeState(3, WARNING_STATE);
    }

    @Override
    public void openNextFragment(int position) {
        presenter.checkCompletionMode(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connection.setSelectionPanelView(null);
    }

    public void changePosition(int position) {
        adapter.changePosition(position);
    }
}
