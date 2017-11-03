package com.jim.multipos.ui.customer_group.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group.CustomerGroupActivity;
import com.jim.multipos.ui.customer_group.connector.CustomerGroupConnector;
import com.jim.multipos.ui.customer_group.presenters.AddCustomerGroupFragmentPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import io.reactivex.disposables.Disposable;

import static com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragment.CUSTOMER_GROUP_DELETED;
import static com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragment.CUSTOMER_GROUP_UPDATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCustomerGroupFragment extends BaseFragment implements AddCustomerGroupFragmentView {
    public static final String ITEM_CLICKED = "item_clicked";
    public static final String ADD_CUSTOMER_GROUP_CLICKED = "add_group";
    public static final String GET_CUSTOMER_GROUP_NAME = "get_customer_group_name";

    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    AddCustomerGroupFragmentPresenter presenter;
    @NotEmpty(messageId = R.string.enter_group_name)
    @BindView(R.id.etGroupName)
    EditText etGroupName;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.btnMembers)
    MpButton btnMembers;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    @BindView(R.id.btnDelete)
    MpButton btnDelete;
    private ArrayList<Disposable> subscriptions = new ArrayList<>();

    public void showGroupNameError(String message) {
        etGroupName.setError(message);
    }

    public void showCustomerGroup(CustomerGroup customerGroup) {
        etGroupName.setText(customerGroup.getName());
        chbActive.setChecked(customerGroup.getIsActive());
    }

    public void clearViews() {
        etGroupName.setText("");
    }

    public void requestFocus() {
        etGroupName.requestFocus();
    }

    public void showMembers() {
        ((CustomerGroupActivity) getActivity()).openCustomerGroupsFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.add_customer_group_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        RxView.clicks(btnSave).subscribe(o -> {
            if (isValid()) {
                presenter.addCustomerGroup(etGroupName.getText().toString(),
                        chbActive.isChecked());
            }
        });

        RxView.clicks(btnMembers).subscribe(o -> {
            presenter.getCustomerGroup();
        });

        RxView.clicks(btnBack).subscribe(o -> {
            ((CustomerGroupActivity) getActivity()).closeActivity();
        });

        RxView.clicks(btnDelete).subscribe(o -> {
            presenter.deleteCustomerGroup();
        });
    }

    @Override
    protected void rxConnections() {
        subscriptions.add(rxBusLocal.toObservable().subscribe(o -> {
            if (o instanceof CustomerGroupEvent) {
                CustomerGroupEvent customerGroupEvent = (CustomerGroupEvent) o;
                if (customerGroupEvent.getEventType().equals(ITEM_CLICKED)) {
                    btnDelete.setVisibility(View.VISIBLE);
                    btnSave.setText(getString(R.string.edit));
                    presenter.showCustomerGroup(customerGroupEvent.getCustomerGroup());
                } else if (customerGroupEvent.getEventType().equals(ADD_CUSTOMER_GROUP_CLICKED)) {
                    btnDelete.setVisibility(View.GONE);
                    btnSave.setText(getString(R.string.add));
                    presenter.addCustomerGroupClicked();
                } else if (customerGroupEvent.getEventType().equals(CustomerGroupConnector.CUSTOMER_GROUP_OPENED)) {
                    presenter.showCustomerGroup();
                } else if (customerGroupEvent.getEventType().equals(GET_CUSTOMER_GROUP_NAME)) {
                    presenter.getCustomerGroupName(etGroupName.getText().toString());
                } else if (customerGroupEvent.getEventType().equals(CUSTOMER_GROUP_UPDATE)) {
                    btnDelete.setVisibility(View.GONE);
                    btnSave.setText(getString(R.string.add));
                    presenter.addCustomerGroup(etGroupName.getText().toString(), chbActive.isChecked());
                } else if (customerGroupEvent.getEventType().equals(CUSTOMER_GROUP_DELETED)) {
                    btnDelete.setVisibility(View.GONE);
                    btnSave.setText(getString(R.string.add));
                    presenter.customerGroupDeleted();
                }
            }
        }));
    }

    @Override
    public void onDestroy() {
        RxBus.removeListners(subscriptions);
        super.onDestroy();
    }

    @Override
    public void showGroupNameExistError() {
        etGroupName.setError(getString(R.string.customer_group_name_exists));
    }

    @Override
    public void showCustomerGroupWarningDialog() {
        WarningDialog warningDialog = new WarningDialog(getContext());
        warningDialog.onlyText(true);
        warningDialog.setWarningText(getString(R.string.you_can_only_delete_inactive_customer_groups));
        warningDialog.setOnYesClickListener(view -> {
            warningDialog.dismiss();
        });
        warningDialog.show();
    }
}
