package com.jim.multipos.ui.service_fee_new.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpActionButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.validator.MultipleCallback;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * Created by Portable-Acer on 28.10.2017.
 */

public class ServiceFeeAdapter extends BaseAdapter<ServiceFee, BaseViewHolder> {
    public interface OnClickListener {
        void onAddClicked(ServiceFee serviceFee);

        void onSaveClicked(ServiceFee serviceFee);

        void onDeleteClicked(ServiceFee serviceFee);

        void onSortByAmountClicked(List<ServiceFee> items);

        void onSortByTypeClicked(List<ServiceFee> items);

        void onSortByReasonClicked(List<ServiceFee> items);

        void onSortByAppTypeClicked(List<ServiceFee> items);

        void onSortByActiveClicked(List<ServiceFee> items);

        void onSortByDefaultClicked(List<ServiceFee> items);

        void showActiveItemWarningDialog();
    }

    public static final int SERVICE_FEE_ITEM = 0;
    public static final int SERVICE_FEE_TYPE_ITEM = 1;
    public static final String TYPE_PERCENT = "PERCENT";
    public static final String TYPE_VALUE = "VALUE";
    public static final String APP_TYPE_ITEM = "ITEM";
    public static final String APP_TYPE_ORDER = "ORDER";
    public static final String APP_TYPE_ALL = "ALL";
    private String[] types;
    private String[] appTypes;
    private Context context;
    private OnClickListener listener;
    private boolean isSortedByAmount = false;
    private boolean isSortedByType = false;
    private boolean isSortedByReason = false;
    private boolean isSortedByAppType = false;
    private boolean isSortedByActive = false;
    private Set<ServiceFee> changedItemPosition;
    private HashMap<Long, Boolean> changedActiveItem;
    private ServiceFee serviceFeeType;

    public ServiceFeeAdapter(Context context, OnClickListener listener, List<ServiceFee> items, String[] types, String[] appTypes) {
        super(items);
        this.context = context;
        this.listener = listener;
        this.types = types;
        this.appTypes = appTypes;
        changedItemPosition = new HashSet<>();
        changedActiveItem = new HashMap<>();
        serviceFeeType = new ServiceFee();
        serviceFeeType.setReason("");
        serviceFeeType.setIsActive(true);
        serviceFeeType.setType(getTypeFromPosition(0));
        serviceFeeType.setApplyingType(getAppTypeFromPosition(0));
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;

        switch (viewType) {
            case SERVICE_FEE_ITEM:
                viewHolder = new ServiceFeeItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_fee_item, parent, false));
                break;
            case SERVICE_FEE_TYPE_ITEM:
                viewHolder = new ServiceFeeTypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_fee_type_item, parent, false));
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return SERVICE_FEE_TYPE_ITEM;
        }

        return SERVICE_FEE_ITEM;
    }

    public void addItem(ServiceFee serviceFee) {
        items.add(0, serviceFee);
        notifyItemInserted(1);
    }

    public void removeItem(ServiceFee serviceFee) {
        int index = items.indexOf(serviceFee);
        items.remove(serviceFee);
        notifyItemRemoved(index + 1);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (position == 0) {
            if (serviceFeeType.getAmount() != null) {
                ((ServiceFeeTypeViewHolder) holder).etAmount.setText(serviceFeeType.getAmount().toString());
            }

            ((ServiceFeeTypeViewHolder) holder).etReason.setText(serviceFeeType.getReason());
            ((ServiceFeeTypeViewHolder) holder).chbActive.setChecked(serviceFeeType.getIsActive());
            ((ServiceFeeTypeViewHolder) holder).spType.setAdapter(types);
            ((ServiceFeeTypeViewHolder) holder).spAppType.setAdapter(appTypes);
            ((ServiceFeeTypeViewHolder) holder).spType.setSelection(getTypePositionFromConst(serviceFeeType.getType()));
            ((ServiceFeeTypeViewHolder) holder).spAppType.setSelection(getAppTypePositionFromConst(serviceFeeType.getApplyingType()));
        } else {
            ((ServiceFeeItemViewHolder) holder).etAmount.setText(String.valueOf(items.get(position - 1).getAmount()));
            ((ServiceFeeItemViewHolder) holder).spType.setAdapter(types);
            ((ServiceFeeItemViewHolder) holder).spType.setSelection(getTypePositionFromConst(items.get(position - 1).getType()));
            ((ServiceFeeItemViewHolder) holder).spAppType.setAdapter(appTypes);
            ((ServiceFeeItemViewHolder) holder).spAppType.setSelection(getAppTypePositionFromConst(items.get(position - 1).getApplyingType()));
            ((ServiceFeeItemViewHolder) holder).etReason.setText(String.valueOf(items.get(position - 1).getReason()));
            ((ServiceFeeItemViewHolder) holder).chbActive.setChecked(items.get(position - 1).getIsActive());

            ((ServiceFeeItemViewHolder) holder).etAmount.setSelection(((ServiceFeeItemViewHolder) holder).etAmount.length());
            ((ServiceFeeItemViewHolder) holder).etReason.setSelection(((ServiceFeeItemViewHolder) holder).etReason.length());

            if (changedItemPosition.contains(getItem(position - 1))) {
                ((ServiceFeeItemViewHolder) holder).btnSave.enable();
            } else {
                ((ServiceFeeItemViewHolder) holder).btnSave.disable();
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    public Set<ServiceFee> getChangedItems() {
        return changedItemPosition;
    }

    class ServiceFeeItemViewHolder extends BaseViewHolder {
        @NotEmpty(messageId = R.string.enter_amount)
        @BindView(R.id.etAmount)
        MpEditText etAmount;
        @BindView(R.id.spType)
        MPosSpinner spType;
        @BindView(R.id.etReason)
        MpEditText etReason;
        @BindView(R.id.spAppType)
        MPosSpinner spAppType;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;
        @BindView(R.id.btnSave)
        MpMiniActionButton btnSave;
        @BindView(R.id.btnRemove)
        MpMiniActionButton btnRemove;

        public ServiceFeeItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            RxView.clicks(btnSave).subscribe(o -> {
                UIUtils.closeKeyboard(btnSave, context);
                if (FormValidator.validate(context, this, new MultipleCallback())) {
                    if (spType.getSelectedPosition() == 0 && Double.parseDouble(etAmount.getText().toString()) > 100) {
                        etAmount.setError(context.getString(R.string.percent_can_not_be_more_hunder));
                    } else {
                        if (changedActiveItem.get(getAdapterPosition() - 1) != null) {
                            getItem(getAdapterPosition() - 1).setIsActive(changedActiveItem.get(getItem(getAdapterPosition() - 1).getId()));
                        } else {
                            getItem(getAdapterPosition() - 1).setIsActive(chbActive.isChecked());
                        }
                        listener.onSaveClicked(getItem(getAdapterPosition() - 1));
                        changedItemPosition.remove(getItem(getAdapterPosition() - 1));
                        changedActiveItem.remove(new Long(getItem(getAdapterPosition() - 1).getId()));
                        btnSave.disable();
                    }
                }
            });

            RxView.clicks(btnRemove).subscribe(o -> {
                UIUtils.closeKeyboard(btnRemove, context);
                //Log.d("myLogs", "I am in btnRemove");
                //Log.d("myLogs", "btnRemove state is " + changedActiveItem.contains(getItem(getAdapterPosition() - 1).getId()));

                if (getItem(getAdapterPosition() - 1).getIsActive()) {
                    listener.showActiveItemWarningDialog();
                } else {
                    listener.onDeleteClicked(getItem(getAdapterPosition() - 1));
                    changedItemPosition.remove(getItem(getAdapterPosition() - 1));
                    changedActiveItem.remove(new Long(getItem(getAdapterPosition() - 1).getId()));
                }
            });

            spType.setItemSelectionListener((view, position) -> {
                Log.d("myLogs", "TYPE Object app type position is " + getTypePositionFromConst(getItem(getAdapterPosition() - 1).getType()));
                Log.d("myLogs", "TYPE App type position is " + position);

                if (getTypeFromPosition(position).equals(TYPE_VALUE)) {
                    etAmount.setError(null);
                }

                if (getTypePositionFromConst(getItem(getAdapterPosition() - 1).getType()) != position) {
                    getItem(getAdapterPosition() - 1).setType(getTypeFromPosition(position));
                    changedItemPosition.add(getItem(getAdapterPosition() - 1));
                    btnSave.enable();
                    //notifyItemChanged(getAdapterPosition());
                }
            });

            spAppType.setItemSelectionListener((view, position) -> {
                Log.d("myLogs", "Object app type position is " + getAppTypePositionFromConst(getItem(getAdapterPosition() - 1).getApplyingType()));
                Log.d("myLogs", "App type position is " + position);
                if (getAppTypePositionFromConst(getItem(getAdapterPosition() - 1).getApplyingType()) != position) {
                    getItem(getAdapterPosition() - 1).setApplyingType(getAppTypeFromPosition(position));
                    changedItemPosition.add(getItem(getAdapterPosition() - 1));
                    btnSave.enable();
                    //notifyItemChanged(getAdapterPosition());
                }
            });

            chbActive.setCheckedChangeListener(isChecked -> {
                if (!getItem(getAdapterPosition() - 1).getIsActive().equals(isChecked)) {
                    changedActiveItem.put(new Long(getItem(getAdapterPosition() - 1).getId()), isChecked);
                    changedItemPosition.add(getItem(getAdapterPosition() - 1));
/*
                    if (isChecked) {
                        changedActiveItem.add(new Long(getItem(getAdapterPosition() - 1).getId()));
                    } else {
                        changedActiveItem.remove(new Long(getItem(getAdapterPosition() - 1).getId()));
                    }*/
                    //getItem(getAdapterPosition() - 1).setIsActive(isChecked);
                    btnSave.enable();
                    //changedItemPosition.add(getItem(getAdapterPosition() - 1));
                    //notifyItemChanged(getAdapterPosition());
                }
            });

            initTextWatcher(etAmount);
            initTextWatcher(etReason);
        }

        private void initTextWatcher(EditText editText) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    switch (editText.getId()) {
                        case R.id.etAmount:
                            try {
                                if (getItem(getAdapterPosition() - 1).getType().equals(TYPE_PERCENT) && Double.parseDouble(editText.getText().toString()) > 100) {
                                    etAmount.setError(context.getString(R.string.percent_can_not_be_more_hunder));
                                    return;
                                }

                                if (Double.parseDouble(editText.getText().toString()) != items.get(getAdapterPosition() - 1).getAmount()) {
                                    items.get(getAdapterPosition() - 1).setAmount(Double.parseDouble(editText.getText().toString()));
                                    changedItemPosition.add(getItem(getAdapterPosition() - 1));
                                    btnSave.enable();
                                }
                            } catch (NumberFormatException e) {
                                etAmount.setError(context.getString(R.string.invalid));
                            }
                            break;
                        case R.id.etReason:
                            if (!editText.getText().toString().equals(getItem(getAdapterPosition() - 1).getReason())) {
                                items.get(getAdapterPosition() - 1).setReason(editText.getText().toString());
                                changedItemPosition.add(getItem(getAdapterPosition() - 1));
                                //notifyItemChanged(getAdapterPosition());
                                btnSave.enable();
                            }
                            break;
                    }
                }
            });
        }
    }

    class ServiceFeeTypeViewHolder extends BaseViewHolder {
        @NotEmpty(messageId = R.string.enter_amount)
        @BindView(R.id.etAmount)
        MpEditText etAmount;
        @BindView(R.id.spType)
        MPosSpinner spType;
        @BindView(R.id.etReason)
        EditText etReason;
        @BindView(R.id.spAppType)
        MPosSpinner spAppType;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;
        @BindView(R.id.btnAdd)
        MpActionButton btnAdd;
        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvReason)
        TextView tvReason;
        @BindView(R.id.tvAppType)
        TextView tvAppType;
        @BindView(R.id.tvActive)
        TextView tvActive;

        public ServiceFeeTypeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            initTextWatcher(etAmount);
            initTextWatcher(etReason);

            spType.setItemSelectionListener((view, position) -> {
                if (getTypeFromPosition(position).equals(TYPE_VALUE)) {
                    etAmount.setError(null);
                }

                serviceFeeType.setType(getTypeFromPosition(position));
            });

            spAppType.setItemSelectionListener((view, position) -> {
                serviceFeeType.setApplyingType(getAppTypeFromPosition(position));
            });

            chbActive.setCheckedChangeListener(isChecked -> {
                serviceFeeType.setIsActive(isChecked);
            });

            RxView.clicks(btnAdd).subscribe(o -> {
                UIUtils.closeKeyboard(btnAdd, context);
                if (FormValidator.validate(context, this, new MultipleCallback())) {
                    try {
                        if (spType.getSelectedPosition() == 0 && Double.parseDouble(etAmount.getText().toString()) > 100) {
                            etAmount.setError(context.getString(R.string.percent_can_not_be_more_hunder));
                        } else {
                            ServiceFee serviceFee = new ServiceFee();
                            serviceFee.setAmount(Double.parseDouble(etAmount.getText().toString()));
                            serviceFee.setType(getTypeFromPosition(spType.getSelectedPosition()));
                            serviceFee.setReason(etReason.getText().toString());
                            serviceFee.setApplyingType(getAppTypeFromPosition(spAppType.getSelectedPosition()));
                            serviceFee.setIsActive(chbActive.isChecked());
                            serviceFee.setCreatedDate(System.currentTimeMillis());

                            listener.onAddClicked(serviceFee);

                            serviceFeeType.setAmount(null);
                            serviceFeeType.setReason("");
                            serviceFeeType.setIsActive(true);
                            serviceFeeType.setType(getTypeFromPosition(0));
                            serviceFeeType.setApplyingType(getAppTypeFromPosition(0));

                            etAmount.setText(null);
                            etReason.setText("");
                            chbActive.setChecked(true);
                            spType.setSelection(0);
                            spAppType.setSelection(0);
                            etAmount.requestFocus();
                        }
                    } catch (NumberFormatException e) {
                        etAmount.setError(context.getString(R.string.invalid));
                    }
                }
            });

            RxView.clicks(tvAmount).subscribe(o -> {
                UIUtils.closeKeyboard(tvAmount, context);
                if (isSortedByAmount) {
                    isSortedByAmount = false;
                    tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                    listener.onSortByDefaultClicked(items);
                } else {
                    isSortedByAmount = true;
                    tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.BOLD));
                    listener.onSortByAmountClicked(items);
                }

                isSortedByType = false;
                isSortedByReason = false;
                isSortedByAppType = false;
                isSortedByActive = false;
                tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                tvAppType.setTypeface(Typeface.create(tvAppType.getTypeface(), Typeface.NORMAL));
                tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
            });

            RxView.clicks(tvType).subscribe(o -> {
                UIUtils.closeKeyboard(tvType, context);
                if (isSortedByType) {
                    isSortedByType = false;
                    tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                    listener.onSortByDefaultClicked(items);
                } else {
                    isSortedByType = true;
                    tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.BOLD));
                    listener.onSortByTypeClicked(items);
                }

                isSortedByAmount = false;
                isSortedByReason = false;
                isSortedByAppType = false;
                isSortedByActive = false;
                tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                tvAppType.setTypeface(Typeface.create(tvAppType.getTypeface(), Typeface.NORMAL));
                tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
            });

            RxView.clicks(tvReason).subscribe(o -> {
                UIUtils.closeKeyboard(tvReason, context);
                if (isSortedByReason) {
                    isSortedByReason = false;
                    tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                    listener.onSortByDefaultClicked(items);
                } else {
                    isSortedByReason = true;
                    tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.BOLD));
                    listener.onSortByReasonClicked(items);
                }

                isSortedByAmount = false;
                isSortedByType = false;
                isSortedByAppType = false;
                isSortedByActive = false;
                tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                tvAppType.setTypeface(Typeface.create(tvAppType.getTypeface(), Typeface.NORMAL));
                tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
            });

            RxView.clicks(tvAppType).subscribe(o -> {
                UIUtils.closeKeyboard(tvAppType, context);
                if (isSortedByAppType) {
                    isSortedByAppType = false;
                    tvAppType.setTypeface(Typeface.create(tvAppType.getTypeface(), Typeface.NORMAL));
                    listener.onSortByDefaultClicked(items);
                } else {
                    isSortedByAppType = true;
                    tvAppType.setTypeface(Typeface.create(tvAppType.getTypeface(), Typeface.BOLD));
                    listener.onSortByAppTypeClicked(items);
                }

                isSortedByAmount = false;
                isSortedByType = false;
                isSortedByReason = false;
                isSortedByActive = false;
                tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
            });

            RxView.clicks(tvActive).subscribe(o -> {
                UIUtils.closeKeyboard(tvActive, context);
                if (isSortedByActive) {
                    isSortedByActive = false;
                    tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
                    listener.onSortByDefaultClicked(items);
                } else {
                    isSortedByActive = true;
                    tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.BOLD));
                    listener.onSortByActiveClicked(items);
                }

                isSortedByAmount = false;
                isSortedByType = false;
                isSortedByReason = false;
                isSortedByAppType = false;
                tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                tvAppType.setTypeface(Typeface.create(tvAppType.getTypeface(), Typeface.NORMAL));
            });
        }

        private void initTextWatcher(EditText editText) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    switch (editText.getId()) {
                        case R.id.etAmount:
                            try {
                                if (spType.getSelectedPosition() == 0 && !editText.getText().toString().isEmpty() && Double.parseDouble(editText.getText().toString()) > 100) {
                                    etAmount.setError(context.getString(R.string.percent_can_not_be_more_hunder));
                                    return;
                                }

                                if (serviceFeeType.getAmount() != null && Double.parseDouble(editText.getText().toString()) != serviceFeeType.getAmount()) {
                                    serviceFeeType.setAmount(Double.parseDouble(editText.getText().toString()));
                                }
                            } catch (NumberFormatException e) {
                                editText.setError(context.getString(R.string.invalid));
                            }
                            break;
                        case R.id.etReason:
                            if (!editText.getText().toString().equals(serviceFeeType.getReason())) {
                                serviceFeeType.setReason(editText.getText().toString());
                            }
                            break;
                    }
                }
            });
        }
    }

    private int getTypePositionFromConst(String type) {
        if (type.equals(TYPE_PERCENT)) {
            return 0;
        } else {
            return 1;
        }
    }

    private int getAppTypePositionFromConst(String appType) {
        if (appType.equals(APP_TYPE_ITEM)) {
            return 0;
        } else if (appType.equals(APP_TYPE_ORDER)) {
            return 1;
        } else if (appType.equals(APP_TYPE_ALL)) {
            return 2;
        }

        return 0;
    }

    private String getTypeFromPosition(int position) {
        if (position == 0) {
            return TYPE_PERCENT;
        } else {
            return TYPE_VALUE;
        }
    }

    private String getAppTypeFromPosition(int position) {
        switch (position) {
            case 0:
                return APP_TYPE_ITEM;
            case 1:
                return APP_TYPE_ORDER;
            case 2:
                return APP_TYPE_ALL;
        }

        return "";
    }
}
